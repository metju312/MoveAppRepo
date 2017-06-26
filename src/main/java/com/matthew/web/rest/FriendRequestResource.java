package com.matthew.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.matthew.domain.FriendRequest;
import com.matthew.domain.Friendship;
import com.matthew.domain.User;
import com.matthew.security.SecurityUtils;
import com.matthew.service.FriendRequestService;
import com.matthew.service.UserService;
import com.matthew.web.rest.util.HeaderUtil;
import com.matthew.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FriendRequest.
 */
@RestController
@RequestMapping("/api")
public class FriendRequestResource {

    private final Logger log = LoggerFactory.getLogger(FriendRequestResource.class);

    private static final String ENTITY_NAME = "friendRequest";

    private final UserService userService;

    private final FriendRequestService friendRequestService;

    public FriendRequestResource(UserService userService, FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendRequestService = friendRequestService;
    }

    /**
     * POST  /friend-requests : Create a new friendRequest.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new friendRequest, or with status 400 (Bad Request) if the friendRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/friend-requests/{login}")
    @Timed
    public ResponseEntity<FriendRequest> createFriendRequest(@PathVariable String login) throws URISyntaxException {
        FriendRequest friendRequest = new FriendRequest();
        log.debug("REST request to save FriendRequest using login: " + login);

        User user = userService.findOneByLogin(login);
        if (user == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "User: " + login + " doesn't exists")).body(null);
        }

        friendRequest.setUser1(user);
        friendRequest.setUser2(userService.findOneByLogin(SecurityUtils.getCurrentUserLogin()));
        FriendRequest result = friendRequestService.save(friendRequest);
        return ResponseEntity.created(new URI("/api/friend-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /friend-requests : Updates an existing friendRequest.
     *
     * @param friendRequest the friendRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated friendRequest,
     * or with status 400 (Bad Request) if the friendRequest is not valid,
     * or with status 500 (Internal Server Error) if the friendRequest couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/friend-requests")
    @Timed
    public ResponseEntity<FriendRequest> updateFriendRequest(@RequestBody FriendRequest friendRequest) throws URISyntaxException {
        log.debug("REST request to update FriendRequest : {}", friendRequest);
        if (friendRequest.getId() == null) {
            return createFriendRequest("");
        }
        FriendRequest result = friendRequestService.save(friendRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, friendRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /friend-requests : get all the friendRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of friendRequests in body
     */
    @GetMapping("/friend-requests")
    @Timed
    public ResponseEntity<List<FriendRequest>> getAllFriendRequests(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of FriendRequests");
        Page<FriendRequest> page = friendRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/friend-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /friend-requests/:id : get the "id" friendRequest.
     *
     * @param id the id of the friendRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the friendRequest, or with status 404 (Not Found)
     */
    @GetMapping("/friend-requests/{id}")
    @Timed
    public ResponseEntity<FriendRequest> getFriendRequest(@PathVariable Long id) {
        log.debug("REST request to get FriendRequest : {}", id);
        FriendRequest friendRequest = friendRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(friendRequest));
    }

    @GetMapping("/friend-requests/to-me")
    @Timed
    public ResponseEntity<List<String>> getFriendRequestToMe() {
        log.debug("REST request to get FriendRequest to me");
        User sessionUser = userService.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        List<String> list = friendRequestService.findAllToSessionUserLogin(sessionUser.getLogin());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * DELETE  /friend-requests/:id : delete the "id" friendRequest.
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/friend-requests/{login}")
    @Timed
    public ResponseEntity<Void> deleteFriendRequest(@PathVariable String login) {
        log.debug("REST request to delete FriendRequest with user1 login" + login);
        User user1 = userService.findOneByLogin(login);
        User user2 = userService.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        FriendRequest friendRequest = friendRequestService.findByUser1AndUser2(user1, user2);
        friendRequestService.delete(friendRequest.getId());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "")).build();
    }
}
