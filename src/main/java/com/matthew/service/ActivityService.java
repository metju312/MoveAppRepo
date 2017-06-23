package com.matthew.service;

import com.matthew.domain.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Activity.
 */
public interface ActivityService {

    /**
     * Save a activity.
     *
     * @param activity the entity to save
     * @return the persisted entity
     */
    Activity save(Activity activity);

    /**
     *  Get all the activities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Activity> findAll(Pageable pageable);

    /**
     *  Get the "id" activity.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Activity findOne(Long id);

    /**
     *  Delete the "id" activity.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
