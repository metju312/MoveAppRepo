package com.matthew.repository;

import com.matthew.domain.FriendRequest;
import com.matthew.domain.User;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the FriendRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {

    List<FriendRequest> findAll();
}
