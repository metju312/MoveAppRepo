package com.matthew.repository;

import com.matthew.domain.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long> {

    @Query("select activity from Activity activity where activity.user.login = ?#{principal.username}")
    Page<Activity> findByUserIsCurrentUser(Pageable pageable)
        ;
    @Query("select activity from Activity activity where activity.user.login = ?#{principal.username}")
    List<Activity> findByUserIsCurrentUserDateBetween(LocalDate fromDate, LocalDate toDate);

    //List<Activity> findAllDateBetween(LocalDate fromDate, LocalDate toDate);
}
