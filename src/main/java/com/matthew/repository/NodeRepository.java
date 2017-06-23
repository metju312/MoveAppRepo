package com.matthew.repository;

import com.matthew.domain.Node;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Node entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NodeRepository extends JpaRepository<Node,Long> {
    List<Node> findAllByActivityId(Long activityId);
}
