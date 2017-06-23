package com.matthew.web.rest;

import com.matthew.MoveApp;

import com.matthew.domain.FriendRequest;
import com.matthew.repository.FriendRequestRepository;
import com.matthew.service.FriendRequestService;
import com.matthew.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FriendRequestResource REST controller.
 *
 * @see FriendRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoveApp.class)
public class FriendRequestResourceIntTest {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFriendRequestMockMvc;

    private FriendRequest friendRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FriendRequestResource friendRequestResource = new FriendRequestResource(friendRequestService);
        this.restFriendRequestMockMvc = MockMvcBuilders.standaloneSetup(friendRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FriendRequest createEntity(EntityManager em) {
        FriendRequest friendRequest = new FriendRequest();
        return friendRequest;
    }

    @Before
    public void initTest() {
        friendRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createFriendRequest() throws Exception {
        int databaseSizeBeforeCreate = friendRequestRepository.findAll().size();

        // Create the FriendRequest
        restFriendRequestMockMvc.perform(post("/api/friend-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(friendRequest)))
            .andExpect(status().isCreated());

        // Validate the FriendRequest in the database
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        assertThat(friendRequestList).hasSize(databaseSizeBeforeCreate + 1);
        FriendRequest testFriendRequest = friendRequestList.get(friendRequestList.size() - 1);
    }

    @Test
    @Transactional
    public void createFriendRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = friendRequestRepository.findAll().size();

        // Create the FriendRequest with an existing ID
        friendRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFriendRequestMockMvc.perform(post("/api/friend-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(friendRequest)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        assertThat(friendRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFriendRequests() throws Exception {
        // Initialize the database
        friendRequestRepository.saveAndFlush(friendRequest);

        // Get all the friendRequestList
        restFriendRequestMockMvc.perform(get("/api/friend-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(friendRequest.getId().intValue())));
    }

    @Test
    @Transactional
    public void getFriendRequest() throws Exception {
        // Initialize the database
        friendRequestRepository.saveAndFlush(friendRequest);

        // Get the friendRequest
        restFriendRequestMockMvc.perform(get("/api/friend-requests/{id}", friendRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(friendRequest.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFriendRequest() throws Exception {
        // Get the friendRequest
        restFriendRequestMockMvc.perform(get("/api/friend-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFriendRequest() throws Exception {
        // Initialize the database
        friendRequestService.save(friendRequest);

        int databaseSizeBeforeUpdate = friendRequestRepository.findAll().size();

        // Update the friendRequest
        FriendRequest updatedFriendRequest = friendRequestRepository.findOne(friendRequest.getId());

        restFriendRequestMockMvc.perform(put("/api/friend-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFriendRequest)))
            .andExpect(status().isOk());

        // Validate the FriendRequest in the database
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        assertThat(friendRequestList).hasSize(databaseSizeBeforeUpdate);
        FriendRequest testFriendRequest = friendRequestList.get(friendRequestList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingFriendRequest() throws Exception {
        int databaseSizeBeforeUpdate = friendRequestRepository.findAll().size();

        // Create the FriendRequest

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFriendRequestMockMvc.perform(put("/api/friend-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(friendRequest)))
            .andExpect(status().isCreated());

        // Validate the FriendRequest in the database
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        assertThat(friendRequestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFriendRequest() throws Exception {
        // Initialize the database
        friendRequestService.save(friendRequest);

        int databaseSizeBeforeDelete = friendRequestRepository.findAll().size();

        // Get the friendRequest
        restFriendRequestMockMvc.perform(delete("/api/friend-requests/{id}", friendRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        assertThat(friendRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FriendRequest.class);
        FriendRequest friendRequest1 = new FriendRequest();
        friendRequest1.setId(1L);
        FriendRequest friendRequest2 = new FriendRequest();
        friendRequest2.setId(friendRequest1.getId());
        assertThat(friendRequest1).isEqualTo(friendRequest2);
        friendRequest2.setId(2L);
        assertThat(friendRequest1).isNotEqualTo(friendRequest2);
        friendRequest1.setId(null);
        assertThat(friendRequest1).isNotEqualTo(friendRequest2);
    }
}
