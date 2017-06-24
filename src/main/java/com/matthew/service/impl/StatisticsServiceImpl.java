package com.matthew.service.impl;

import com.matthew.service.StatisticsService;
import com.matthew.domain.Statistics;
import com.matthew.repository.StatisticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Statistics.
 */
@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService{

    private final Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    private final StatisticsRepository statisticsRepository;

    public StatisticsServiceImpl(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * Save a statistics.
     *
     * @param statistics the entity to save
     * @return the persisted entity
     */
    @Override
    public Statistics save(Statistics statistics) {
        log.debug("Request to save Statistics : {}", statistics);
        return statisticsRepository.save(statistics);
    }

    /**
     *  Get all the statistics.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Statistics> findAll(Pageable pageable) {
        log.debug("Request to get all Statistics");
        return statisticsRepository.findByUserIsCurrentUser(pageable);
    }

    /**
     *  Get one statistics by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Statistics findOne(Long id) {
        log.debug("Request to get Statistics : {}", id);
        return statisticsRepository.findOne(id);
    }

    /**
     *  Delete the  statistics by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Statistics : {}", id);
        statisticsRepository.delete(id);
    }
}
