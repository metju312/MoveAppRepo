package com.matthew.service;

import com.matthew.domain.FriendRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing FriendRequest.
 */
public interface FriendRequestService {

    /**
     * Save a friendRequest.
     *
     * @param friendRequest the entity to save
     * @return the persisted entity
     */
    FriendRequest save(FriendRequest friendRequest);

    /**
     *  Get all the friendRequests.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FriendRequest> findAll(Pageable pageable);

    /**
     *  Get the "id" friendRequest.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FriendRequest findOne(Long id);

    /**
     *  Delete the "id" friendRequest.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
