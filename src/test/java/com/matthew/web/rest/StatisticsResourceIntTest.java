package com.matthew.web.rest;

import com.matthew.MoveApp;

import com.matthew.domain.Statistics;
import com.matthew.repository.StatisticsRepository;
import com.matthew.service.ActivityService;
import com.matthew.service.StatisticsService;
import com.matthew.service.UserService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StatisticsResource REST controller.
 *
 * @see StatisticsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoveApp.class)
public class StatisticsResourceIntTest {

    private static final LocalDate DEFAULT_INITIAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INITIAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FINAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINAL_DATE = LocalDate.now(ZoneId.systemDefault());

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

    private static final Integer DEFAULT_NUMBER_OF_ACTIVITIES = 1;
    private static final Integer UPDATED_NUMBER_OF_ACTIVITIES = 2;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private UserService userService;

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

    private MockMvc restStatisticsMockMvc;

    private Statistics statistics;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StatisticsResource statisticsResource = new StatisticsResource(statisticsService, userService, activityService);
        this.restStatisticsMockMvc = MockMvcBuilders.standaloneSetup(statisticsResource)
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
    public static Statistics createEntity(EntityManager em) {
        Statistics statistics = new Statistics()
            .initialDate(DEFAULT_INITIAL_DATE)
            .finalDate(DEFAULT_FINAL_DATE)
            .distance(DEFAULT_DISTANCE)
            .duration(DEFAULT_DURATION)
            .steps(DEFAULT_STEPS)
            .caloriesBurnt(DEFAULT_CALORIES_BURNT)
            .averageSpeed(DEFAULT_AVERAGE_SPEED)
            .maxSpeed(DEFAULT_MAX_SPEED)
            .numberOfActivities(DEFAULT_NUMBER_OF_ACTIVITIES);
        return statistics;
    }

    @Before
    public void initTest() {
        statistics = createEntity(em);
    }

    @Test
    @Transactional
    public void createStatistics() throws Exception {
        int databaseSizeBeforeCreate = statisticsRepository.findAll().size();

        // Create the Statistics
        restStatisticsMockMvc.perform(post("/api/statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statistics)))
            .andExpect(status().isCreated());

        // Validate the Statistics in the database
        List<Statistics> statisticsList = statisticsRepository.findAll();
        assertThat(statisticsList).hasSize(databaseSizeBeforeCreate + 1);
        Statistics testStatistics = statisticsList.get(statisticsList.size() - 1);
        assertThat(testStatistics.getInitialDate()).isEqualTo(DEFAULT_INITIAL_DATE);
        assertThat(testStatistics.getFinalDate()).isEqualTo(DEFAULT_FINAL_DATE);
        assertThat(testStatistics.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testStatistics.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testStatistics.getSteps()).isEqualTo(DEFAULT_STEPS);
        assertThat(testStatistics.getCaloriesBurnt()).isEqualTo(DEFAULT_CALORIES_BURNT);
        assertThat(testStatistics.getAverageSpeed()).isEqualTo(DEFAULT_AVERAGE_SPEED);
        assertThat(testStatistics.getMaxSpeed()).isEqualTo(DEFAULT_MAX_SPEED);
        assertThat(testStatistics.getNumberOfActivities()).isEqualTo(DEFAULT_NUMBER_OF_ACTIVITIES);
    }

    @Test
    @Transactional
    public void createStatisticsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = statisticsRepository.findAll().size();

        // Create the Statistics with an existing ID
        statistics.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatisticsMockMvc.perform(post("/api/statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statistics)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Statistics> statisticsList = statisticsRepository.findAll();
        assertThat(statisticsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStatistics() throws Exception {
        // Initialize the database
        statisticsRepository.saveAndFlush(statistics);

        // Get all the statisticsList
        restStatisticsMockMvc.perform(get("/api/statistics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].initialDate").value(hasItem(DEFAULT_INITIAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].finalDate").value(hasItem(DEFAULT_FINAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].steps").value(hasItem(DEFAULT_STEPS)))
            .andExpect(jsonPath("$.[*].caloriesBurnt").value(hasItem(DEFAULT_CALORIES_BURNT.doubleValue())))
            .andExpect(jsonPath("$.[*].averageSpeed").value(hasItem(DEFAULT_AVERAGE_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].maxSpeed").value(hasItem(DEFAULT_MAX_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].numberOfActivities").value(hasItem(DEFAULT_NUMBER_OF_ACTIVITIES)));
    }

    @Test
    @Transactional
    public void getStatistics() throws Exception {
        // Initialize the database
        statisticsRepository.saveAndFlush(statistics);

        // Get the statistics
        restStatisticsMockMvc.perform(get("/api/statistics/{id}", statistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(statistics.getId().intValue()))
            .andExpect(jsonPath("$.initialDate").value(DEFAULT_INITIAL_DATE.toString()))
            .andExpect(jsonPath("$.finalDate").value(DEFAULT_FINAL_DATE.toString()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.steps").value(DEFAULT_STEPS))
            .andExpect(jsonPath("$.caloriesBurnt").value(DEFAULT_CALORIES_BURNT.doubleValue()))
            .andExpect(jsonPath("$.averageSpeed").value(DEFAULT_AVERAGE_SPEED.doubleValue()))
            .andExpect(jsonPath("$.maxSpeed").value(DEFAULT_MAX_SPEED.doubleValue()))
            .andExpect(jsonPath("$.numberOfActivities").value(DEFAULT_NUMBER_OF_ACTIVITIES));
    }

    @Test
    @Transactional
    public void getNonExistingStatistics() throws Exception {
        // Get the statistics
        restStatisticsMockMvc.perform(get("/api/statistics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatistics() throws Exception {
        // Initialize the database
        statisticsService.save(statistics);

        int databaseSizeBeforeUpdate = statisticsRepository.findAll().size();

        // Update the statistics
        Statistics updatedStatistics = statisticsRepository.findOne(statistics.getId());
        updatedStatistics
            .initialDate(UPDATED_INITIAL_DATE)
            .finalDate(UPDATED_FINAL_DATE)
            .distance(UPDATED_DISTANCE)
            .duration(UPDATED_DURATION)
            .steps(UPDATED_STEPS)
            .caloriesBurnt(UPDATED_CALORIES_BURNT)
            .averageSpeed(UPDATED_AVERAGE_SPEED)
            .maxSpeed(UPDATED_MAX_SPEED)
            .numberOfActivities(UPDATED_NUMBER_OF_ACTIVITIES);

        restStatisticsMockMvc.perform(put("/api/statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStatistics)))
            .andExpect(status().isOk());

        // Validate the Statistics in the database
        List<Statistics> statisticsList = statisticsRepository.findAll();
        assertThat(statisticsList).hasSize(databaseSizeBeforeUpdate);
        Statistics testStatistics = statisticsList.get(statisticsList.size() - 1);
        assertThat(testStatistics.getInitialDate()).isEqualTo(UPDATED_INITIAL_DATE);
        assertThat(testStatistics.getFinalDate()).isEqualTo(UPDATED_FINAL_DATE);
        assertThat(testStatistics.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testStatistics.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testStatistics.getSteps()).isEqualTo(UPDATED_STEPS);
        assertThat(testStatistics.getCaloriesBurnt()).isEqualTo(UPDATED_CALORIES_BURNT);
        assertThat(testStatistics.getAverageSpeed()).isEqualTo(UPDATED_AVERAGE_SPEED);
        assertThat(testStatistics.getMaxSpeed()).isEqualTo(UPDATED_MAX_SPEED);
        assertThat(testStatistics.getNumberOfActivities()).isEqualTo(UPDATED_NUMBER_OF_ACTIVITIES);
    }

    @Test
    @Transactional
    public void updateNonExistingStatistics() throws Exception {
        int databaseSizeBeforeUpdate = statisticsRepository.findAll().size();

        // Create the Statistics

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStatisticsMockMvc.perform(put("/api/statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statistics)))
            .andExpect(status().isCreated());

        // Validate the Statistics in the database
        List<Statistics> statisticsList = statisticsRepository.findAll();
        assertThat(statisticsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStatistics() throws Exception {
        // Initialize the database
        statisticsService.save(statistics);

        int databaseSizeBeforeDelete = statisticsRepository.findAll().size();

        // Get the statistics
        restStatisticsMockMvc.perform(delete("/api/statistics/{id}", statistics.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Statistics> statisticsList = statisticsRepository.findAll();
        assertThat(statisticsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Statistics.class);
        Statistics statistics1 = new Statistics();
        statistics1.setId(1L);
        Statistics statistics2 = new Statistics();
        statistics2.setId(statistics1.getId());
        assertThat(statistics1).isEqualTo(statistics2);
        statistics2.setId(2L);
        assertThat(statistics1).isNotEqualTo(statistics2);
        statistics1.setId(null);
        assertThat(statistics1).isNotEqualTo(statistics2);
    }
}
