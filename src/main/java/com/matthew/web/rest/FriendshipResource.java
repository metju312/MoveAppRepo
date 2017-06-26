package com.matthew.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.matthew.domain.Friendship;
import com.matthew.domain.User;
import com.matthew.security.SecurityUtils;
import com.matthew.service.FriendshipService;
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
 * REST controller for managing Friendship.
 */
@RestController
@RequestMapping("/api")
public class FriendshipResource {

    private final Logger log = LoggerFactory.getLogger(FriendshipResource.class);

    private static final String ENTITY_NAME = "friendship";

    private final UserService userService;
    private final FriendshipService friendshipService;

    public FriendshipResource(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    /**
     * POST  /friendships : Create a new friendship.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new friendship, or with status 400 (Bad Request) if the friendship has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/friendships/{login}")
    @Timed
    public ResponseEntity<Friendship> createFriendship(@PathVariable String login) throws URISyntaxException {
        Friendship friendship = new Friendship();
        log.debug("REST request to save Friendship with user with login: " + login);
        User user = userService.findOneByLogin(login);
        if (user == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "User: " + login + " doesn't exists")).body(null);
        }
        friendship.setUser1(user);
        friendship.setUser2(userService.findOneByLogin(SecurityUtils.getCurrentUserLogin()));
        Friendship result = friendshipService.save(friendship);
        return ResponseEntity.created(new URI("/api/friendships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /friendships : Updates an existing friendship.
     *
     * @param friendship the friendship to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated friendship,
     * or with status 400 (Bad Request) if the friendship is not valid,
     * or with status 500 (Internal Server Error) if the friendship couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/friendships")
    @Timed
    public ResponseEntity<Friendship> updateFriendship(@RequestBody Friendship friendship) throws URISyntaxException {
        log.debug("REST request to update Friendship : {}", friendship);
        if (friendship.getId() == null) {
            return createFriendship("");
        }
        Friendship result = friendshipService.save(friendship);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, friendship.getId().toString()))
            .body(result);
    }

    /**
     * GET  /friendships : get all the friendships.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of friendships in body
     */
    @GetMapping("/friendships")
    @Timed
    public ResponseEntity<List<Friendship>> getAllFriendships(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Friendships");
        Page<Friendship> page = friendshipService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/friendships");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /friendships/:id : get the "id" friendship.
     *
     * @param id the id of the friendship to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the friendship, or with status 404 (Not Found)
     */
    @GetMapping("/friendships/{id}")
    @Timed
    public ResponseEntity<Friendship> getFriendship(@PathVariable Long id) {
        log.debug("REST request to get Friendship : {}", id);
        Friendship friendship = friendshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(friendship));
    }

    /**
     * DELETE  /friendships/:id : delete the "id" friendship.
     *
     * @param id the id of the friendship to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/friendships/{id}")
    @Timed
    public ResponseEntity<Void> deleteFriendship(@PathVariable Long id) {
        log.debug("REST request to delete Friendship : {}", id);
        friendshipService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
