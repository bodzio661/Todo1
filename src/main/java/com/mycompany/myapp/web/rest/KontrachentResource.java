package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Kontrachent;
import com.mycompany.myapp.service.KontrachentService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Kontrachent}.
 */
@RestController
@RequestMapping("/api")
public class KontrachentResource {

    private final Logger log = LoggerFactory.getLogger(KontrachentResource.class);

    private static final String ENTITY_NAME = "kontrachent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KontrachentService kontrachentService;

    public KontrachentResource(KontrachentService kontrachentService) {
        this.kontrachentService = kontrachentService;
    }

    /**
     * {@code POST  /kontrachents} : Create a new kontrachent.
     *
     * @param kontrachent the kontrachent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kontrachent, or with status {@code 400 (Bad Request)} if the kontrachent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kontrachents")
    public ResponseEntity<Kontrachent> createKontrachent(@RequestBody Kontrachent kontrachent) throws URISyntaxException {
        log.debug("REST request to save Kontrachent : {}", kontrachent);
        if (kontrachent.getId() != null) {
            throw new BadRequestAlertException("A new kontrachent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Kontrachent result = kontrachentService.save(kontrachent);
        return ResponseEntity.created(new URI("/api/kontrachents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kontrachents} : Updates an existing kontrachent.
     *
     * @param kontrachent the kontrachent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kontrachent,
     * or with status {@code 400 (Bad Request)} if the kontrachent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kontrachent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kontrachents")
    public ResponseEntity<Kontrachent> updateKontrachent(@RequestBody Kontrachent kontrachent) throws URISyntaxException {
        log.debug("REST request to update Kontrachent : {}", kontrachent);
        if (kontrachent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Kontrachent result = kontrachentService.save(kontrachent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kontrachent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /kontrachents} : get all the kontrachents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kontrachents in body.
     */
    @GetMapping("/kontrachents")
    public ResponseEntity<List<Kontrachent>> getAllKontrachents(Pageable pageable) {
        log.debug("REST request to get a page of Kontrachents");
        Page<Kontrachent> page = kontrachentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kontrachents/:id} : get the "id" kontrachent.
     *
     * @param id the id of the kontrachent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kontrachent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kontrachents/{id}")
    public ResponseEntity<Kontrachent> getKontrachent(@PathVariable Long id) {
        log.debug("REST request to get Kontrachent : {}", id);
        Optional<Kontrachent> kontrachent = kontrachentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kontrachent);
    }

    /**
     * {@code DELETE  /kontrachents/:id} : delete the "id" kontrachent.
     *
     * @param id the id of the kontrachent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kontrachents/{id}")
    public ResponseEntity<Void> deleteKontrachent(@PathVariable Long id) {
        log.debug("REST request to delete Kontrachent : {}", id);

        kontrachentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/kontrachents?query=:query} : search for the kontrachent corresponding
     * to the query.
     *
     * @param query the query of the kontrachent search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/kontrachents")
    public ResponseEntity<List<Kontrachent>> searchKontrachents(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Kontrachents for query {}", query);
        Page<Kontrachent> page = kontrachentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
