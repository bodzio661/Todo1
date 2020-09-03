package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Faktura;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Faktura} entity.
 */
public interface FakturaSearchRepository extends ElasticsearchRepository<Faktura, Long> {
}
