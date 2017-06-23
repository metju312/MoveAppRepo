package com.matthew.service;

import com.matthew.domain.Statistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Statistics.
 */
public interface StatisticsService {

    /**
     * Save a statistics.
     *
     * @param statistics the entity to save
     * @return the persisted entity
     */
    Statistics save(Statistics statistics);

    /**
     *  Get all the statistics.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Statistics> findAll(Pageable pageable);

    /**
     *  Get the "id" statistics.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Statistics findOne(Long id);

    /**
     *  Delete the "id" statistics.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
