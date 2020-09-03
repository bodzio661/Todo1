package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.FakturaService;
import com.mycompany.myapp.domain.Faktura;
import com.mycompany.myapp.repository.FakturaRepository;
import com.mycompany.myapp.repository.search.FakturaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Faktura}.
 */
@Service
@Transactional
public class FakturaServiceImpl implements FakturaService {

    private final Logger log = LoggerFactory.getLogger(FakturaServiceImpl.class);

    private final FakturaRepository fakturaRepository;

    private final FakturaSearchRepository fakturaSearchRepository;

    public FakturaServiceImpl(FakturaRepository fakturaRepository, FakturaSearchRepository fakturaSearchRepository) {
        this.fakturaRepository = fakturaRepository;
        this.fakturaSearchRepository = fakturaSearchRepository;
    }

    /**
     * Save a faktura.
     *
     * @param faktura the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Faktura save(Faktura faktura) {
        log.debug("Request to save Faktura : {}", faktura);
        Faktura result = fakturaRepository.save(faktura);
        fakturaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the fakturas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Faktura> findAll(Pageable pageable) {
        log.debug("Request to get all Fakturas");
        return fakturaRepository.findAll(pageable);
    }


    /**
     * Get one faktura by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Faktura> findOne(Long id) {
        log.debug("Request to get Faktura : {}", id);
        return fakturaRepository.findById(id);
    }

    /**
     * Delete the faktura by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Faktura : {}", id);

        fakturaRepository.deleteById(id);
        fakturaSearchRepository.deleteById(id);
    }

    /**
     * Search for the faktura corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Faktura> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Fakturas for query {}", query);
        return fakturaSearchRepository.search(queryStringQuery(query), pageable);    }
}
