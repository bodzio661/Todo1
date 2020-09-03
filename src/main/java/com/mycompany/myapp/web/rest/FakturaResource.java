package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Faktura;
import com.mycompany.myapp.service.FakturaService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Faktura}.
 */
@RestController
@RequestMapping("/api")
public class FakturaResource {

    private final Logger log = LoggerFactory.getLogger(FakturaResource.class);

    private static final String ENTITY_NAME = "faktura";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FakturaService fakturaService;

    public FakturaResource(FakturaService fakturaService) {
        this.fakturaService = fakturaService;
    }

    /**
     * {@code POST  /fakturas} : Create a new faktura.
     *
     * @param faktura the faktura to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new faktura, or with status {@code 400 (Bad Request)} if the faktura has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fakturas")
    public ResponseEntity<Faktura> createFaktura(@Valid @RequestBody Faktura faktura) throws URISyntaxException {
        log.debug("REST request to save Faktura : {}", faktura);
        if (faktura.getId() != null) {
            throw new BadRequestAlertException("A new faktura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Faktura result = fakturaService.save(faktura);
        return ResponseEntity.created(new URI("/api/fakturas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fakturas} : Updates an existing faktura.
     *
     * @param faktura the faktura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated faktura,
     * or with status {@code 400 (Bad Request)} if the faktura is not valid,
     * or with status {@code 500 (Internal Server Error)} if the faktura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fakturas")
    public ResponseEntity<Faktura> updateFaktura(@Valid @RequestBody Faktura faktura) throws URISyntaxException {
        log.debug("REST request to update Faktura : {}", faktura);
        if (faktura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Faktura result = fakturaService.save(faktura);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, faktura.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fakturas} : get all the fakturas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fakturas in body.
     */
    @GetMapping("/fakturas")
    public ResponseEntity<List<Faktura>> getAllFakturas(Pageable pageable) {
        log.debug("REST request to get a page of Fakturas");
        Page<Faktura> page = fakturaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fakturas/:id} : get the "id" faktura.
     *
     * @param id the id of the faktura to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the faktura, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fakturas/{id}")
    public ResponseEntity<Faktura> getFaktura(@PathVariable Long id) {
        log.debug("REST request to get Faktura : {}", id);
        Optional<Faktura> faktura = fakturaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(faktura);
    }

    /**
     * {@code DELETE  /fakturas/:id} : delete the "id" faktura.
     *
     * @param id the id of the faktura to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fakturas/{id}")
    public ResponseEntity<Void> deleteFaktura(@PathVariable Long id) {
        log.debug("REST request to delete Faktura : {}", id);

        fakturaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/fakturas?query=:query} : search for the faktura corresponding
     * to the query.
     *
     * @param query the query of the faktura search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fakturas")
    public ResponseEntity<List<Faktura>> searchFakturas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Fakturas for query {}", query);
        Page<Faktura> page = fakturaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
