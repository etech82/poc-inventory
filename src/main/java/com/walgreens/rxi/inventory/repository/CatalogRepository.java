package com.walgreens.rxi.inventory.repository;

import com.walgreens.rxi.inventory.domain.Catalog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Catalog entity.
 */
@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    @Query(
        value = "select distinct catalog from Catalog catalog left join fetch catalog.productCodes",
        countQuery = "select count(distinct catalog) from Catalog catalog"
    )
    Page<Catalog> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct catalog from Catalog catalog left join fetch catalog.productCodes")
    List<Catalog> findAllWithEagerRelationships();

    @Query("select catalog from Catalog catalog left join fetch catalog.productCodes where catalog.id =:id")
    Optional<Catalog> findOneWithEagerRelationships(@Param("id") Long id);
}
