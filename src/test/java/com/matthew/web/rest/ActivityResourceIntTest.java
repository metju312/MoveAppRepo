package com.matthew.web.rest;

import com.matthew.MoveApp;

import com.matthew.domain.Activity;
import com.matthew.repository.ActivityRepository;
import com.matthew.service.ActivityService;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoveApp.class)
public class ActivityResourceIntTest {

    private static final String DEFAULT_ACTIVITY_TYPE = "A";
    private static final String UPDATED_ACTIVITY_TYPE = "B";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_DISTANCE = 1F;
    private static final Float UPDATED_DISTANCE = 2F;

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Integer DEFAULT_STEPS = 1;
    private static final Integer UPDATED_STEPS = 2;

    private static final Float DEFAULT_CALORIES_BURNT = 1F;
    private static final Float UPDATED_CALORIES_BURNT = 2F;

    private static final Float DEFAULT_AVERAGE_SPEED = 1F;
    private static final Float UPDATED_AVERAGE_SPEED = 2F;

    private static final Float DEFAULT_MAX_SPEED = 1F;
    private static final Float UPDATED_MAX_SPEED = 2F;

    private static final Integer DEFAULT_SHARED = 1;
    private static final Integer UPDATED_SHARED = 2;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivityMockMvc;

    private Activity activity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActivityResource activityResource = new ActivityResource(activityService);
        this.restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource)
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
    public static Activity createEntity(EntityManager em) {
        Activity activity = new Activity()
            .activityType(DEFAULT_ACTIVITY_TYPE)
            .date(DEFAULT_DATE)
            .distance(DEFAULT_DISTANCE)
            .duration(DEFAULT_DURATION)
            .steps(DEFAULT_STEPS)
            .caloriesBurnt(DEFAULT_CALORIES_BURNT)
            .averageSpeed(DEFAULT_AVERAGE_SPEED)
            .maxSpeed(DEFAULT_MAX_SPEED)
            .shared(DEFAULT_SHARED);
        return activity;
    }

    @Before
    public void initTest() {
        activity = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivity() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().size();

        // Create the Activity
        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activity)))
            .andExpect(status().isCreated());

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeCreate + 1);
        Activity testActivity = activityList.get(activityList.size() - 1);
        assertThat(testActivity.getActivityType()).isEqualTo(DEFAULT_ACTIVITY_TYPE);
        assertThat(testActivity.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testActivity.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testActivity.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testActivity.getSteps()).isEqualTo(DEFAULT_STEPS);
        assertThat(testActivity.getCaloriesBurnt()).isEqualTo(DEFAULT_CALORIES_BURNT);
        assertThat(testActivity.getAverageSpeed()).isEqualTo(DEFAULT_AVERAGE_SPEED);
        assertThat(testActivity.getMaxSpeed()).isEqualTo(DEFAULT_MAX_SPEED);
        assertThat(testActivity.getShared()).isEqualTo(DEFAULT_SHARED);
    }

    @Test
    @Transactional
    public void createActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().size();

        // Create the Activity with an existing ID
        activity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activity)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllActivities() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList
        restActivityMockMvc.perform(get("/api/activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].steps").value(hasItem(DEFAULT_STEPS)))
            .andExpect(jsonPath("$.[*].caloriesBurnt").value(hasItem(DEFAULT_CALORIES_BURNT.doubleValue())))
            .andExpect(jsonPath("$.[*].averageSpeed").value(hasItem(DEFAULT_AVERAGE_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].maxSpeed").value(hasItem(DEFAULT_MAX_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].shared").value(hasItem(DEFAULT_SHARED)));
    }

    @Test
    @Transactional
    public void getActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get the activity
        restActivityMockMvc.perform(get("/api/activities/{id}", activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activity.getId().intValue()))
            .andExpect(jsonPath("$.activityType").value(DEFAULT_ACTIVITY_TYPE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.steps").value(DEFAULT_STEPS))
            .andExpect(jsonPath("$.caloriesBurnt").value(DEFAULT_CALORIES_BURNT.doubleValue()))
            .andExpect(jsonPath("$.averageSpeed").value(DEFAULT_AVERAGE_SPEED.doubleValue()))
            .andExpect(jsonPath("$.maxSpeed").value(DEFAULT_MAX_SPEED.doubleValue()))
            .andExpect(jsonPath("$.shared").value(DEFAULT_SHARED));
    }

    @Test
    @Transactional
    public void getNonExistingActivity() throws Exception {
        // Get the activity
        restActivityMockMvc.perform(get("/api/activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivity() throws Exception {
        // Initialize the database
        activityService.save(activity);

        int databaseSizeBeforeUpdate = activityRepository.findAll().size();

        // Update the activity
        Activity updatedActivity = activityRepository.findOne(activity.getId());
        updatedActivity
            .activityType(UPDATED_ACTIVITY_TYPE)
            .date(UPDATED_DATE)
            .distance(UPDATED_DISTANCE)
            .duration(UPDATED_DURATION)
            .steps(UPDATED_STEPS)
            .caloriesBurnt(UPDATED_CALORIES_BURNT)
            .averageSpeed(UPDATED_AVERAGE_SPEED)
            .maxSpeed(UPDATED_MAX_SPEED)
            .shared(UPDATED_SHARED);

        restActivityMockMvc.perform(put("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActivity)))
            .andExpect(status().isOk());

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activityList.get(activityList.size() - 1);
        assertThat(testActivity.getActivityType()).isEqualTo(UPDATED_ACTIVITY_TYPE);
        assertThat(testActivity.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testActivity.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testActivity.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testActivity.getSteps()).isEqualTo(UPDATED_STEPS);
        assertThat(testActivity.getCaloriesBurnt()).isEqualTo(UPDATED_CALORIES_BURNT);
        assertThat(testActivity.getAverageSpeed()).isEqualTo(UPDATED_AVERAGE_SPEED);
        assertThat(testActivity.getMaxSpeed()).isEqualTo(UPDATED_MAX_SPEED);
        assertThat(testActivity.getShared()).isEqualTo(UPDATED_SHARED);
    }

    @Test
    @Transactional
    public void updateNonExistingActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().size();

        // Create the Activity

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActivityMockMvc.perform(put("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activity)))
            .andExpect(status().isCreated());

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteActivity() throws Exception {
        // Initialize the database
        activityService.save(activity);

        int databaseSizeBeforeDelete = activityRepository.findAll().size();

        // Get the activity
        restActivityMockMvc.perform(delete("/api/activities/{id}", activity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activity.class);
        Activity activity1 = new Activity();
        activity1.setId(1L);
        Activity activity2 = new Activity();
        activity2.setId(activity1.getId());
        assertThat(activity1).isEqualTo(activity2);
        activity2.setId(2L);
        assertThat(activity1).isNotEqualTo(activity2);
        activity1.setId(null);
        assertThat(activity1).isNotEqualTo(activity2);
    }
}
