package com.matthew.service;

import com.matthew.domain.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Friendship.
 */
public interface FriendshipService {

    /**
     * Save a friendship.
     *
     * @param friendship the entity to save
     * @return the persisted entity
     */
    Friendship save(Friendship friendship);

    /**
     *  Get all the friendships.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Friendship> findAll(Pageable pageable);

    /**
     *  Get the "id" friendship.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Friendship findOne(Long id);

    /**
     *  Delete the "id" friendship.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<String> findAllByLogin(String sessionUserLogin);
}
