package com.matthew.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.matthew.domain.Node;
import com.matthew.service.NodeService;
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
 * REST controller for managing Node.
 */
@RestController
@RequestMapping("/api")
public class NodeResource {

    private final Logger log = LoggerFactory.getLogger(NodeResource.class);

    private static final String ENTITY_NAME = "node";

    private final NodeService nodeService;

    public NodeResource(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    /**
     * POST  /nodes : Create a new node.
     *
     * @param node the node to create
     * @return the ResponseEntity with status 201 (Created) and with body the new node, or with status 400 (Bad Request) if the node has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/nodes")
    @Timed
    public ResponseEntity<Node> createNode(@RequestBody Node node) throws URISyntaxException {
        log.debug("REST request to save Node : {}", node);
        if (node.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new node cannot already have an ID")).body(null);
        }
        Node result = nodeService.save(node);
        return ResponseEntity.created(new URI("/api/nodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /nodes : Updates an existing node.
     *
     * @param node the node to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated node,
     * or with status 400 (Bad Request) if the node is not valid,
     * or with status 500 (Internal Server Error) if the node couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/nodes")
    @Timed
    public ResponseEntity<Node> updateNode(@RequestBody Node node) throws URISyntaxException {
        log.debug("REST request to update Node : {}", node);
        if (node.getId() == null) {
            return createNode(node);
        }
        Node result = nodeService.save(node);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, node.getId().toString()))
            .body(result);
    }

    /**
     * GET  /nodes : get all the nodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of nodes in body
     */
    @GetMapping("/nodes")
    @Timed
    public ResponseEntity<List<Node>> getAllNodes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Nodes");
        Page<Node> page = nodeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/nodes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/nodes/activity/{id}")
    @Timed
    public ResponseEntity<List<Node>> getAllNodesByActivityId(@PathVariable Long id) {
        log.debug("REST request to get a page of Nodes");
        List<Node> page = nodeService.findAllByActivityId(id);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /nodes/:id : get the "id" node.
     *
     * @param id the id of the node to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the node, or with status 404 (Not Found)
     */
    @GetMapping("/nodes/{id}")
    @Timed
    public ResponseEntity<Node> getNode(@PathVariable Long id) {
        log.debug("REST request to get Node : {}", id);
        Node node = nodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(node));
    }

    /**
     * DELETE  /nodes/:id : delete the "id" node.
     *
     * @param id the id of the node to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/nodes/{id}")
    @Timed
    public ResponseEntity<Void> deleteNode(@PathVariable Long id) {
        log.debug("REST request to delete Node : {}", id);
        nodeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
