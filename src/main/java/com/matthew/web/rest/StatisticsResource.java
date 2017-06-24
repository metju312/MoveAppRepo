package com.matthew.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.matthew.domain.Activity;
import com.matthew.domain.Statistics;
import com.matthew.security.SecurityUtils;
import com.matthew.service.ActivityService;
import com.matthew.service.StatisticsService;
import com.matthew.service.UserService;
import com.matthew.web.rest.util.HeaderUtil;
import com.matthew.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Statistics.
 */
@RestController
@RequestMapping("/api")
public class StatisticsResource {

    private final Logger log = LoggerFactory.getLogger(StatisticsResource.class);

    private static final String ENTITY_NAME = "statistics";

    private final StatisticsService statisticsService;

    private final UserService userService;

    private final ActivityService activityService;

    public StatisticsResource(StatisticsService statisticsService, UserService userService, ActivityService activityService) {
        this.statisticsService = statisticsService;
        this.userService = userService;
        this.activityService = activityService;
    }

    /**
     * POST  /statistics : Create a new statistics.
     *
     * @param statistics the statistics to create
     * @return the ResponseEntity with status 201 (Created) and with body the new statistics, or with status 400 (Bad Request) if the statistics has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/statistics")
    @Timed
    public ResponseEntity<Statistics> createStatistics(@RequestBody Statistics statistics) throws URISyntaxException {
        log.debug("REST request to save Statistics : {}", statistics);
        if (statistics.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new statistics cannot already have an ID")).body(null);
        }
        statistics.setUser(userService.findOneByLogin(SecurityUtils.getCurrentUserLogin()));
        List<Activity> activities = activityService.findAllDateBetween(statistics.getInitialDate(), statistics.getFinalDate());
        activities = activitiesDateBetween(activities, statistics.getInitialDate(), statistics.getFinalDate());
        statistics = sumStatistics(statistics, activities);
        Statistics result = statisticsService.save(statistics);
        return ResponseEntity.created(new URI("/api/statistics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private List<Activity> activitiesDateBetween(List<Activity> activities, LocalDate initialDate, LocalDate finalDate) {
        List<Activity> activitiesFiltered = new ArrayList<>();
        for (Activity activity: activities) {
            if(activity.getDate().isBefore(finalDate.atStartOfDay().toInstant(ZoneOffset.UTC))){
                if(activity.getDate().isAfter(initialDate.atStartOfDay().toInstant(ZoneOffset.UTC))){
                    activitiesFiltered.add(activity);
                }
            }
        }
        return activitiesFiltered;
    }

    private Statistics sumStatistics(Statistics statistics, List<Activity> activities) {
        Float distance = 0f;
        Integer duration = 0;
        Integer steps = 0;
        Float caloriesBurnt = 0f;
        Float averageSpeed = 0f;
        Integer averageSpeedCount = 0;
        Float maxSpeed = 0f;
        Integer numberOfActivities = 0;
        for (Activity activity : activities) {
            if(activity.getDistance() != null){
                distance+=activity.getDistance();
            }
            if(activity.getDuration() != null){
                duration+=activity.getDuration();
            }
            if(activity.getSteps() != null){
                steps+=activity.getSteps();
            }
            if(activity.getCaloriesBurnt() != null){
                caloriesBurnt+=activity.getCaloriesBurnt();
            }
            if(activity.getAverageSpeed() != null){
                averageSpeedCount++;
                averageSpeed+=activity.getAverageSpeed();
            }
            if(activity.getMaxSpeed() != null){
                if(activity.getMaxSpeed() > maxSpeed){
                    maxSpeed = activity.getMaxSpeed();
                }
            }
            numberOfActivities++;
        }
        statistics.setDistance(distance);
        statistics.setDuration(duration);
        statistics.setSteps(steps);
        statistics.setCaloriesBurnt(caloriesBurnt);
        statistics.setAverageSpeed(averageSpeed/averageSpeedCount);
        statistics.setMaxSpeed(maxSpeed);
        statistics.setNumberOfActivities(numberOfActivities);
        return statistics;
    }


    /**
     * PUT  /statistics : Updates an existing statistics.
     *
     * @param statistics the statistics to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated statistics,
     * or with status 400 (Bad Request) if the statistics is not valid,
     * or with status 500 (Internal Server Error) if the statistics couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/statistics")
    @Timed
    public ResponseEntity<Statistics> updateStatistics(@RequestBody Statistics statistics) throws URISyntaxException {
        log.debug("REST request to update Statistics : {}", statistics);
        if (statistics.getId() == null) {
            return createStatistics(statistics);
        }
        Statistics result = statisticsService.save(statistics);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, statistics.getId().toString()))
            .body(result);
    }

    /**
     * GET  /statistics : get all the statistics.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of statistics in body
     */
    @GetMapping("/statistics")
    @Timed
    public ResponseEntity<List<Statistics>> getAllStatistics(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Statistics");
        Page<Statistics> page = statisticsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/statistics");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /statistics/:id : get the "id" statistics.
     *
     * @param id the id of the statistics to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the statistics, or with status 404 (Not Found)
     */
    @GetMapping("/statistics/{id}")
    @Timed
    public ResponseEntity<Statistics> getStatistics(@PathVariable Long id) {
        log.debug("REST request to get Statistics : {}", id);
        Statistics statistics = statisticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(statistics));
    }

    /**
     * DELETE  /statistics/:id : delete the "id" statistics.
     *
     * @param id the id of the statistics to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/statistics/{id}")
    @Timed
    public ResponseEntity<Void> deleteStatistics(@PathVariable Long id) {
        log.debug("REST request to delete Statistics : {}", id);
        statisticsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
