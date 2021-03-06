package com.matthew.service.impl;

import com.matthew.service.ActivityService;
import com.matthew.domain.Activity;
import com.matthew.repository.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


/**
 * Service Implementation for managing Activity.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService{

    private final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Save a activity.
     *
     * @param activity the entity to save
     * @return the persisted entity
     */
    @Override
    public Activity save(Activity activity) {
        log.debug("Request to save Activity : {}", activity);
        return activityRepository.save(activity);
    }

    /**
     *  Get all the activities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Activity> findAll(Pageable pageable) {
        log.debug("Request to get all Activities");
        return activityRepository.findByUserIsCurrentUser(pageable);
    }

    /**
     *  Get one activity by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Activity findOne(Long id) {
        log.debug("Request to get Activity : {}", id);
        return activityRepository.findOne(id);
    }

    /**
     *  Delete the  activity by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Activity : {}", id);
        activityRepository.delete(id);
    }

    @Override
    public List<Activity> findAllDateBetween(LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to get all Activities between dates");
        return activityRepository.findByUserIsCurrentUserDateBetween(fromDate,toDate);
    }
}
