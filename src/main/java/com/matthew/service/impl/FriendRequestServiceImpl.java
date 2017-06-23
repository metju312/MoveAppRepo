package com.matthew.service.impl;

import com.matthew.service.FriendRequestService;
import com.matthew.domain.FriendRequest;
import com.matthew.repository.FriendRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing FriendRequest.
 */
@Service
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService{

    private final Logger log = LoggerFactory.getLogger(FriendRequestServiceImpl.class);

    private final FriendRequestRepository friendRequestRepository;

    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    /**
     * Save a friendRequest.
     *
     * @param friendRequest the entity to save
     * @return the persisted entity
     */
    @Override
    public FriendRequest save(FriendRequest friendRequest) {
        log.debug("Request to save FriendRequest : {}", friendRequest);
        return friendRequestRepository.save(friendRequest);
    }

    /**
     *  Get all the friendRequests.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FriendRequest> findAll(Pageable pageable) {
        log.debug("Request to get all FriendRequests");
        return friendRequestRepository.findAll(pageable);
    }

    /**
     *  Get one friendRequest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FriendRequest findOne(Long id) {
        log.debug("Request to get FriendRequest : {}", id);
        return friendRequestRepository.findOne(id);
    }

    /**
     *  Delete the  friendRequest by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FriendRequest : {}", id);
        friendRequestRepository.delete(id);
    }
}
