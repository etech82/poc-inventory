package com.walgreens.rxi.inventory.repository;

import com.walgreens.rxi.inventory.domain.ProductCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductCodeRepository extends JpaRepository<ProductCode, Long> {}
