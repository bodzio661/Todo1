package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Faktura;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Faktura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FakturaRepository extends JpaRepository<Faktura, Long> {
}
