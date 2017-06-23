package com.matthew.repository;

import com.matthew.domain.FriendRequest;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FriendRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {
    
}
