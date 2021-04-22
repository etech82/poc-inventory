package com.walgreens.rxi.inventory.web.rest;

import com.walgreens.rxi.inventory.domain.Catalog;
import com.walgreens.rxi.inventory.repository.CatalogRepository;
import com.walgreens.rxi.inventory.service.CatalogService;
import com.walgreens.rxi.inventory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.walgreens.rxi.inventory.domain.Catalog}.
 */
@RestController
@RequestMapping("/api")
public class CatalogResource {

    private final Logger log = LoggerFactory.getLogger(CatalogResource.class);

    private static final String ENTITY_NAME = "inventoryCatalog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogService catalogService;

    private final CatalogRepository catalogRepository;

    public CatalogResource(CatalogService catalogService, CatalogRepository catalogRepository) {
        this.catalogService = catalogService;
        this.catalogRepository = catalogRepository;
    }

    /**
     * {@code POST  /catalogs} : Create a new catalog.
     *
     * @param catalog the catalog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalog, or with status {@code 400 (Bad Request)} if the catalog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalogs")
    public ResponseEntity<Catalog> createCatalog(@Valid @RequestBody Catalog catalog) throws URISyntaxException {
        log.debug("REST request to save Catalog : {}", catalog);
        if (catalog.getId() != null) {
            throw new BadRequestAlertException("A new catalog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Catalog result = catalogService.save(catalog);
        return ResponseEntity
            .created(new URI("/api/catalogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalogs/:id} : Updates an existing catalog.
     *
     * @param id the id of the catalog to save.
     * @param catalog the catalog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalog,
     * or with status {@code 400 (Bad Request)} if the catalog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalogs/{id}")
    public ResponseEntity<Catalog> updateCatalog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Catalog catalog
    ) throws URISyntaxException {
        log.debug("REST request to update Catalog : {}, {}", id, catalog);
        if (catalog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Catalog result = catalogService.save(catalog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalogs/:id} : Partial updates given fields of an existing catalog, field will ignore if it is null
     *
     * @param id the id of the catalog to save.
     * @param catalog the catalog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalog,
     * or with status {@code 400 (Bad Request)} if the catalog is not valid,
     * or with status {@code 404 (Not Found)} if the catalog is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalogs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Catalog> partialUpdateCatalog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Catalog catalog
    ) throws URISyntaxException {
        log.debug("REST request to partial update Catalog partially : {}, {}", id, catalog);
        if (catalog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Catalog> result = catalogService.partialUpdate(catalog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalog.getId().toString())
        );
    }

    /**
     * {@code GET  /catalogs} : get all the catalogs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogs in body.
     */
    @GetMapping("/catalogs")
    public List<Catalog> getAllCatalogs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Catalogs");
        return catalogService.findAll();
    }

    /**
     * {@code GET  /catalogs/:id} : get the "id" catalog.
     *
     * @param id the id of the catalog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalogs/{id}")
    public ResponseEntity<Catalog> getCatalog(@PathVariable Long id) {
        log.debug("REST request to get Catalog : {}", id);
        Optional<Catalog> catalog = catalogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalog);
    }

    /**
     * {@code DELETE  /catalogs/:id} : delete the "id" catalog.
     *
     * @param id the id of the catalog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalogs/{id}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long id) {
        log.debug("REST request to delete Catalog : {}", id);
        catalogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
