package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Faktura;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Faktura}.
 */
public interface FakturaService {

    /**
     * Save a faktura.
     *
     * @param faktura the entity to save.
     * @return the persisted entity.
     */
    Faktura save(Faktura faktura);

    /**
     * Get all the fakturas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Faktura> findAll(Pageable pageable);


    /**
     * Get the "id" faktura.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Faktura> findOne(Long id);

    /**
     * Delete the "id" faktura.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the faktura corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Faktura> search(String query, Pageable pageable);
}
