package com.walgreens.rxi.inventory.repository;

import com.walgreens.rxi.inventory.domain.Packaging;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Packaging entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackagingRepository extends JpaRepository<Packaging, Long> {}
