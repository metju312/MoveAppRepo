package com.matthew.repository;

import com.matthew.domain.Statistics;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Statistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatisticsRepository extends JpaRepository<Statistics,Long> {

    @Query("select statistics from Statistics statistics where statistics.user.login = ?#{principal.username}")
    List<Statistics> findByUserIsCurrentUser();
    
}
