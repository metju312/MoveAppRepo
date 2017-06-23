package com.matthew.service.impl;

import com.matthew.service.NodeService;
import com.matthew.domain.Node;
import com.matthew.repository.NodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Node.
 */
@Service
@Transactional
public class NodeServiceImpl implements NodeService{

    private final Logger log = LoggerFactory.getLogger(NodeServiceImpl.class);

    private final NodeRepository nodeRepository;

    public NodeServiceImpl(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    /**
     * Save a node.
     *
     * @param node the entity to save
     * @return the persisted entity
     */
    @Override
    public Node save(Node node) {
        log.debug("Request to save Node : {}", node);
        return nodeRepository.save(node);
    }

    /**
     *  Get all the nodes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Node> findAll(Pageable pageable) {
        log.debug("Request to get all Nodes");
        return nodeRepository.findAll(pageable);
    }

    /**
     *  Get one node by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Node findOne(Long id) {
        log.debug("Request to get Node : {}", id);
        return nodeRepository.findOne(id);
    }

    /**
     *  Delete the  node by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Node : {}", id);
        nodeRepository.delete(id);
    }
}
