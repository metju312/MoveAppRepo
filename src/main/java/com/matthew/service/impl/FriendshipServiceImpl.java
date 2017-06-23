package com.matthew.service.impl;

import com.matthew.service.FriendshipService;
import com.matthew.domain.Friendship;
import com.matthew.repository.FriendshipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Friendship.
 */
@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService{

    private final Logger log = LoggerFactory.getLogger(FriendshipServiceImpl.class);

    private final FriendshipRepository friendshipRepository;

    public FriendshipServiceImpl(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * Save a friendship.
     *
     * @param friendship the entity to save
     * @return the persisted entity
     */
    @Override
    public Friendship save(Friendship friendship) {
        log.debug("Request to save Friendship : {}", friendship);
        return friendshipRepository.save(friendship);
    }

    /**
     *  Get all the friendships.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Friendship> findAll(Pageable pageable) {
        log.debug("Request to get all Friendships");
        return friendshipRepository.findAll(pageable);
    }

    /**
     *  Get one friendship by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Friendship findOne(Long id) {
        log.debug("Request to get Friendship : {}", id);
        return friendshipRepository.findOne(id);
    }

    /**
     *  Delete the  friendship by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Friendship : {}", id);
        friendshipRepository.delete(id);
    }
}
