package com.walgreens.rxi.inventory.web.rest;

import com.walgreens.rxi.inventory.domain.Packaging;
import com.walgreens.rxi.inventory.repository.PackagingRepository;
import com.walgreens.rxi.inventory.service.PackagingService;
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
 * REST controller for managing {@link com.walgreens.rxi.inventory.domain.Packaging}.
 */
@RestController
@RequestMapping("/api")
public class PackagingResource {

    private final Logger log = LoggerFactory.getLogger(PackagingResource.class);

    private static final String ENTITY_NAME = "inventoryPackaging";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackagingService packagingService;

    private final PackagingRepository packagingRepository;

    public PackagingResource(PackagingService packagingService, PackagingRepository packagingRepository) {
        this.packagingService = packagingService;
        this.packagingRepository = packagingRepository;
    }

    /**
     * {@code POST  /packagings} : Create a new packaging.
     *
     * @param packaging the packaging to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packaging, or with status {@code 400 (Bad Request)} if the packaging has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/packagings")
    public ResponseEntity<Packaging> createPackaging(@Valid @RequestBody Packaging packaging) throws URISyntaxException {
        log.debug("REST request to save Packaging : {}", packaging);
        if (packaging.getId() != null) {
            throw new BadRequestAlertException("A new packaging cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Packaging result = packagingService.save(packaging);
        return ResponseEntity
            .created(new URI("/api/packagings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /packagings/:id} : Updates an existing packaging.
     *
     * @param id the id of the packaging to save.
     * @param packaging the packaging to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packaging,
     * or with status {@code 400 (Bad Request)} if the packaging is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packaging couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/packagings/{id}")
    public ResponseEntity<Packaging> updatePackaging(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Packaging packaging
    ) throws URISyntaxException {
        log.debug("REST request to update Packaging : {}, {}", id, packaging);
        if (packaging.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packaging.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packagingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Packaging result = packagingService.save(packaging);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packaging.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /packagings/:id} : Partial updates given fields of an existing packaging, field will ignore if it is null
     *
     * @param id the id of the packaging to save.
     * @param packaging the packaging to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packaging,
     * or with status {@code 400 (Bad Request)} if the packaging is not valid,
     * or with status {@code 404 (Not Found)} if the packaging is not found,
     * or with status {@code 500 (Internal Server Error)} if the packaging couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/packagings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Packaging> partialUpdatePackaging(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Packaging packaging
    ) throws URISyntaxException {
        log.debug("REST request to partial update Packaging partially : {}, {}", id, packaging);
        if (packaging.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packaging.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packagingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Packaging> result = packagingService.partialUpdate(packaging);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packaging.getId().toString())
        );
    }

    /**
     * {@code GET  /packagings} : get all the packagings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packagings in body.
     */
    @GetMapping("/packagings")
    public List<Packaging> getAllPackagings() {
        log.debug("REST request to get all Packagings");
        return packagingService.findAll();
    }

    /**
     * {@code GET  /packagings/:id} : get the "id" packaging.
     *
     * @param id the id of the packaging to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packaging, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/packagings/{id}")
    public ResponseEntity<Packaging> getPackaging(@PathVariable Long id) {
        log.debug("REST request to get Packaging : {}", id);
        Optional<Packaging> packaging = packagingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(packaging);
    }

    /**
     * {@code DELETE  /packagings/:id} : delete the "id" packaging.
     *
     * @param id the id of the packaging to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/packagings/{id}")
    public ResponseEntity<Void> deletePackaging(@PathVariable Long id) {
        log.debug("REST request to delete Packaging : {}", id);
        packagingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
