package com.matthew.service;

import com.matthew.domain.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Node.
 */
public interface NodeService {

    /**
     * Save a node.
     *
     * @param node the entity to save
     * @return the persisted entity
     */
    Node save(Node node);

    /**
     *  Get all the nodes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Node> findAll(Pageable pageable);

    List<Node> findAllByActivityId(Long id);

    /**
     *  Get the "id" node.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Node findOne(Long id);

    /**
     *  Delete the "id" node.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
