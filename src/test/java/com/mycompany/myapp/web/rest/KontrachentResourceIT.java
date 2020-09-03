package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Todo1App;
import com.mycompany.myapp.domain.Kontrachent;
import com.mycompany.myapp.repository.KontrachentRepository;
import com.mycompany.myapp.repository.search.KontrachentSearchRepository;
import com.mycompany.myapp.service.KontrachentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link KontrachentResource} REST controller.
 */
@SpringBootTest(classes = Todo1App.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class KontrachentResourceIT {

    private static final String DEFAULT_NAZWA_KONTRACHENTA = "AAAAAAAAAA";
    private static final String UPDATED_NAZWA_KONTRACHENTA = "BBBBBBBBBB";

    private static final Integer DEFAULT_TERMIN_KONTRACHENTA = 1;
    private static final Integer UPDATED_TERMIN_KONTRACHENTA = 2;

    private static final Boolean DEFAULT_TYP_KONTRACHENTA = false;
    private static final Boolean UPDATED_TYP_KONTRACHENTA = true;

    @Autowired
    private KontrachentRepository kontrachentRepository;

    @Autowired
    private KontrachentService kontrachentService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.KontrachentSearchRepositoryMockConfiguration
     */
    @Autowired
    private KontrachentSearchRepository mockKontrachentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKontrachentMockMvc;

    private Kontrachent kontrachent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kontrachent createEntity(EntityManager em) {
        Kontrachent kontrachent = new Kontrachent()
            .nazwaKontrachenta(DEFAULT_NAZWA_KONTRACHENTA)
            .terminKontrachenta(DEFAULT_TERMIN_KONTRACHENTA)
            .typKontrachenta(DEFAULT_TYP_KONTRACHENTA);
        return kontrachent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kontrachent createUpdatedEntity(EntityManager em) {
        Kontrachent kontrachent = new Kontrachent()
            .nazwaKontrachenta(UPDATED_NAZWA_KONTRACHENTA)
            .terminKontrachenta(UPDATED_TERMIN_KONTRACHENTA)
            .typKontrachenta(UPDATED_TYP_KONTRACHENTA);
        return kontrachent;
    }

    @BeforeEach
    public void initTest() {
        kontrachent = createEntity(em);
    }

    @Test
    @Transactional
    public void createKontrachent() throws Exception {
        int databaseSizeBeforeCreate = kontrachentRepository.findAll().size();
        // Create the Kontrachent
        restKontrachentMockMvc.perform(post("/api/kontrachents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kontrachent)))
            .andExpect(status().isCreated());

        // Validate the Kontrachent in the database
        List<Kontrachent> kontrachentList = kontrachentRepository.findAll();
        assertThat(kontrachentList).hasSize(databaseSizeBeforeCreate + 1);
        Kontrachent testKontrachent = kontrachentList.get(kontrachentList.size() - 1);
        assertThat(testKontrachent.getNazwaKontrachenta()).isEqualTo(DEFAULT_NAZWA_KONTRACHENTA);
        assertThat(testKontrachent.getTerminKontrachenta()).isEqualTo(DEFAULT_TERMIN_KONTRACHENTA);
        assertThat(testKontrachent.isTypKontrachenta()).isEqualTo(DEFAULT_TYP_KONTRACHENTA);

        // Validate the Kontrachent in Elasticsearch
        verify(mockKontrachentSearchRepository, times(1)).save(testKontrachent);
    }

    @Test
    @Transactional
    public void createKontrachentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kontrachentRepository.findAll().size();

        // Create the Kontrachent with an existing ID
        kontrachent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKontrachentMockMvc.perform(post("/api/kontrachents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kontrachent)))
            .andExpect(status().isBadRequest());

        // Validate the Kontrachent in the database
        List<Kontrachent> kontrachentList = kontrachentRepository.findAll();
        assertThat(kontrachentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Kontrachent in Elasticsearch
        verify(mockKontrachentSearchRepository, times(0)).save(kontrachent);
    }


    @Test
    @Transactional
    public void getAllKontrachents() throws Exception {
        // Initialize the database
        kontrachentRepository.saveAndFlush(kontrachent);

        // Get all the kontrachentList
        restKontrachentMockMvc.perform(get("/api/kontrachents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kontrachent.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazwaKontrachenta").value(hasItem(DEFAULT_NAZWA_KONTRACHENTA)))
            .andExpect(jsonPath("$.[*].terminKontrachenta").value(hasItem(DEFAULT_TERMIN_KONTRACHENTA)))
            .andExpect(jsonPath("$.[*].typKontrachenta").value(hasItem(DEFAULT_TYP_KONTRACHENTA.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getKontrachent() throws Exception {
        // Initialize the database
        kontrachentRepository.saveAndFlush(kontrachent);

        // Get the kontrachent
        restKontrachentMockMvc.perform(get("/api/kontrachents/{id}", kontrachent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kontrachent.getId().intValue()))
            .andExpect(jsonPath("$.nazwaKontrachenta").value(DEFAULT_NAZWA_KONTRACHENTA))
            .andExpect(jsonPath("$.terminKontrachenta").value(DEFAULT_TERMIN_KONTRACHENTA))
            .andExpect(jsonPath("$.typKontrachenta").value(DEFAULT_TYP_KONTRACHENTA.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingKontrachent() throws Exception {
        // Get the kontrachent
        restKontrachentMockMvc.perform(get("/api/kontrachents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKontrachent() throws Exception {
        // Initialize the database
        kontrachentService.save(kontrachent);

        int databaseSizeBeforeUpdate = kontrachentRepository.findAll().size();

        // Update the kontrachent
        Kontrachent updatedKontrachent = kontrachentRepository.findById(kontrachent.getId()).get();
        // Disconnect from session so that the updates on updatedKontrachent are not directly saved in db
        em.detach(updatedKontrachent);
        updatedKontrachent
            .nazwaKontrachenta(UPDATED_NAZWA_KONTRACHENTA)
            .terminKontrachenta(UPDATED_TERMIN_KONTRACHENTA)
            .typKontrachenta(UPDATED_TYP_KONTRACHENTA);

        restKontrachentMockMvc.perform(put("/api/kontrachents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedKontrachent)))
            .andExpect(status().isOk());

        // Validate the Kontrachent in the database
        List<Kontrachent> kontrachentList = kontrachentRepository.findAll();
        assertThat(kontrachentList).hasSize(databaseSizeBeforeUpdate);
        Kontrachent testKontrachent = kontrachentList.get(kontrachentList.size() - 1);
        assertThat(testKontrachent.getNazwaKontrachenta()).isEqualTo(UPDATED_NAZWA_KONTRACHENTA);
        assertThat(testKontrachent.getTerminKontrachenta()).isEqualTo(UPDATED_TERMIN_KONTRACHENTA);
        assertThat(testKontrachent.isTypKontrachenta()).isEqualTo(UPDATED_TYP_KONTRACHENTA);

        // Validate the Kontrachent in Elasticsearch
        verify(mockKontrachentSearchRepository, times(2)).save(testKontrachent);
    }

    @Test
    @Transactional
    public void updateNonExistingKontrachent() throws Exception {
        int databaseSizeBeforeUpdate = kontrachentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKontrachentMockMvc.perform(put("/api/kontrachents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kontrachent)))
            .andExpect(status().isBadRequest());

        // Validate the Kontrachent in the database
        List<Kontrachent> kontrachentList = kontrachentRepository.findAll();
        assertThat(kontrachentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Kontrachent in Elasticsearch
        verify(mockKontrachentSearchRepository, times(0)).save(kontrachent);
    }

    @Test
    @Transactional
    public void deleteKontrachent() throws Exception {
        // Initialize the database
        kontrachentService.save(kontrachent);

        int databaseSizeBeforeDelete = kontrachentRepository.findAll().size();

        // Delete the kontrachent
        restKontrachentMockMvc.perform(delete("/api/kontrachents/{id}", kontrachent.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Kontrachent> kontrachentList = kontrachentRepository.findAll();
        assertThat(kontrachentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Kontrachent in Elasticsearch
        verify(mockKontrachentSearchRepository, times(1)).deleteById(kontrachent.getId());
    }

    @Test
    @Transactional
    public void searchKontrachent() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        kontrachentService.save(kontrachent);
        when(mockKontrachentSearchRepository.search(queryStringQuery("id:" + kontrachent.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(kontrachent), PageRequest.of(0, 1), 1));

        // Search the kontrachent
        restKontrachentMockMvc.perform(get("/api/_search/kontrachents?query=id:" + kontrachent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kontrachent.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazwaKontrachenta").value(hasItem(DEFAULT_NAZWA_KONTRACHENTA)))
            .andExpect(jsonPath("$.[*].terminKontrachenta").value(hasItem(DEFAULT_TERMIN_KONTRACHENTA)))
            .andExpect(jsonPath("$.[*].typKontrachenta").value(hasItem(DEFAULT_TYP_KONTRACHENTA.booleanValue())));
    }
}
