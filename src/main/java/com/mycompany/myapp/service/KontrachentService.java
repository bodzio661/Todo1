package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Kontrachent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Kontrachent}.
 */
public interface KontrachentService {

    /**
     * Save a kontrachent.
     *
     * @param kontrachent the entity to save.
     * @return the persisted entity.
     */
    Kontrachent save(Kontrachent kontrachent);

    /**
     * Get all the kontrachents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Kontrachent> findAll(Pageable pageable);


    /**
     * Get the "id" kontrachent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Kontrachent> findOne(Long id);

    /**
     * Delete the "id" kontrachent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the kontrachent corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Kontrachent> search(String query, Pageable pageable);
}
