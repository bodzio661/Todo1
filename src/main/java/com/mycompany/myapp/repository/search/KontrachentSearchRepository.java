package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Kontrachent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Kontrachent} entity.
 */
public interface KontrachentSearchRepository extends ElasticsearchRepository<Kontrachent, Long> {
}
