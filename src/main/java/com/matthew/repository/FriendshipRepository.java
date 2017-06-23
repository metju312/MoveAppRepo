package com.matthew.repository;

import com.matthew.domain.Friendship;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Friendship entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    
}
