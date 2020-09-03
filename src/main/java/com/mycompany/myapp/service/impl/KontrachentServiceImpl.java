package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.KontrachentService;
import com.mycompany.myapp.domain.Kontrachent;
import com.mycompany.myapp.repository.KontrachentRepository;
import com.mycompany.myapp.repository.search.KontrachentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Kontrachent}.
 */
@Service
@Transactional
public class KontrachentServiceImpl implements KontrachentService {

    private final Logger log = LoggerFactory.getLogger(KontrachentServiceImpl.class);

    private final KontrachentRepository kontrachentRepository;

    private final KontrachentSearchRepository kontrachentSearchRepository;

    public KontrachentServiceImpl(KontrachentRepository kontrachentRepository, KontrachentSearchRepository kontrachentSearchRepository) {
        this.kontrachentRepository = kontrachentRepository;
        this.kontrachentSearchRepository = kontrachentSearchRepository;
    }

    /**
     * Save a kontrachent.
     *
     * @param kontrachent the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Kontrachent save(Kontrachent kontrachent) {
        log.debug("Request to save Kontrachent : {}", kontrachent);
        Kontrachent result = kontrachentRepository.save(kontrachent);
        kontrachentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the kontrachents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Kontrachent> findAll(Pageable pageable) {
        log.debug("Request to get all Kontrachents");
        return kontrachentRepository.findAll(pageable);
    }


    /**
     * Get one kontrachent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Kontrachent> findOne(Long id) {
        log.debug("Request to get Kontrachent : {}", id);
        return kontrachentRepository.findById(id);
    }

    /**
     * Delete the kontrachent by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Kontrachent : {}", id);

        kontrachentRepository.deleteById(id);
        kontrachentSearchRepository.deleteById(id);
    }

    /**
     * Search for the kontrachent corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Kontrachent> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Kontrachents for query {}", query);
        return kontrachentSearchRepository.search(queryStringQuery(query), pageable);    }
}
