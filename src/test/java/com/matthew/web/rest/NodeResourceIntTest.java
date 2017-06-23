package com.matthew.web.rest;

import com.matthew.MoveApp;

import com.matthew.domain.Node;
import com.matthew.repository.NodeRepository;
import com.matthew.service.NodeService;
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
 * Test class for the NodeResource REST controller.
 *
 * @see NodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoveApp.class)
public class NodeResourceIntTest {

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNodeMockMvc;

    private Node node;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NodeResource nodeResource = new NodeResource(nodeService);
        this.restNodeMockMvc = MockMvcBuilders.standaloneSetup(nodeResource)
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
    public static Node createEntity(EntityManager em) {
        Node node = new Node()
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE);
        return node;
    }

    @Before
    public void initTest() {
        node = createEntity(em);
    }

    @Test
    @Transactional
    public void createNode() throws Exception {
        int databaseSizeBeforeCreate = nodeRepository.findAll().size();

        // Create the Node
        restNodeMockMvc.perform(post("/api/nodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(node)))
            .andExpect(status().isCreated());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeCreate + 1);
        Node testNode = nodeList.get(nodeList.size() - 1);
        assertThat(testNode.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testNode.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    public void createNodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nodeRepository.findAll().size();

        // Create the Node with an existing ID
        node.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNodeMockMvc.perform(post("/api/nodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(node)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNodes() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodeList
        restNodeMockMvc.perform(get("/api/nodes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(node.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));
    }

    @Test
    @Transactional
    public void getNode() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get the node
        restNodeMockMvc.perform(get("/api/nodes/{id}", node.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(node.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNode() throws Exception {
        // Get the node
        restNodeMockMvc.perform(get("/api/nodes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNode() throws Exception {
        // Initialize the database
        nodeService.save(node);

        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();

        // Update the node
        Node updatedNode = nodeRepository.findOne(node.getId());
        updatedNode
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restNodeMockMvc.perform(put("/api/nodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNode)))
            .andExpect(status().isOk());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
        Node testNode = nodeList.get(nodeList.size() - 1);
        assertThat(testNode.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testNode.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void updateNonExistingNode() throws Exception {
        int databaseSizeBeforeUpdate = nodeRepository.findAll().size();

        // Create the Node

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNodeMockMvc.perform(put("/api/nodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(node)))
            .andExpect(status().isCreated());

        // Validate the Node in the database
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNode() throws Exception {
        // Initialize the database
        nodeService.save(node);

        int databaseSizeBeforeDelete = nodeRepository.findAll().size();

        // Get the node
        restNodeMockMvc.perform(delete("/api/nodes/{id}", node.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Node> nodeList = nodeRepository.findAll();
        assertThat(nodeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Node.class);
        Node node1 = new Node();
        node1.setId(1L);
        Node node2 = new Node();
        node2.setId(node1.getId());
        assertThat(node1).isEqualTo(node2);
        node2.setId(2L);
        assertThat(node1).isNotEqualTo(node2);
        node1.setId(null);
        assertThat(node1).isNotEqualTo(node2);
    }
}
