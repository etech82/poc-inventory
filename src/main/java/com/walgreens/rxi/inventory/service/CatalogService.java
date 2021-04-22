package com.walgreens.rxi.inventory.service;

import com.walgreens.rxi.inventory.domain.Catalog;
import com.walgreens.rxi.inventory.repository.CatalogRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Catalog}.
 */
@Service
@Transactional
public class CatalogService {

    private final Logger log = LoggerFactory.getLogger(CatalogService.class);

    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    /**
     * Save a catalog.
     *
     * @param catalog the entity to save.
     * @return the persisted entity.
     */
    public Catalog save(Catalog catalog) {
        log.debug("Request to save Catalog : {}", catalog);
        return catalogRepository.save(catalog);
    }

    /**
     * Partially update a catalog.
     *
     * @param catalog the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Catalog> partialUpdate(Catalog catalog) {
        log.debug("Request to partially update Catalog : {}", catalog);

        return catalogRepository
            .findById(catalog.getId())
            .map(
                existingCatalog -> {
                    if (catalog.getCode() != null) {
                        existingCatalog.setCode(catalog.getCode());
                    }
                    if (catalog.getStatus() != null) {
                        existingCatalog.setStatus(catalog.getStatus());
                    }

                    return existingCatalog;
                }
            )
            .map(catalogRepository::save);
    }

    /**
     * Get all the catalogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Catalog> findAll() {
        log.debug("Request to get all Catalogs");
        return catalogRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the catalogs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Catalog> findAllWithEagerRelationships(Pageable pageable) {
        return catalogRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one catalog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Catalog> findOne(Long id) {
        log.debug("Request to get Catalog : {}", id);
        return catalogRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the catalog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Catalog : {}", id);
        catalogRepository.deleteById(id);
    }
}
