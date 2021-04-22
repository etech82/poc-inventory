package com.walgreens.rxi.inventory.web.rest;

import com.walgreens.rxi.inventory.domain.ProductCode;
import com.walgreens.rxi.inventory.repository.ProductCodeRepository;
import com.walgreens.rxi.inventory.service.ProductCodeService;
import com.walgreens.rxi.inventory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.walgreens.rxi.inventory.domain.ProductCode}.
 */
@RestController
@RequestMapping("/api")
public class ProductCodeResource {

    private final Logger log = LoggerFactory.getLogger(ProductCodeResource.class);

    private static final String ENTITY_NAME = "inventoryProductCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductCodeService productCodeService;

    private final ProductCodeRepository productCodeRepository;

    public ProductCodeResource(ProductCodeService productCodeService, ProductCodeRepository productCodeRepository) {
        this.productCodeService = productCodeService;
        this.productCodeRepository = productCodeRepository;
    }

    /**
     * {@code POST  /product-codes} : Create a new productCode.
     *
     * @param productCode the productCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productCode, or with status {@code 400 (Bad Request)} if the productCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-codes")
    public ResponseEntity<ProductCode> createProductCode(@Valid @RequestBody ProductCode productCode) throws URISyntaxException {
        log.debug("REST request to save ProductCode : {}", productCode);
        if (productCode.getId() != null) {
            throw new BadRequestAlertException("A new productCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductCode result = productCodeService.save(productCode);
        return ResponseEntity
            .created(new URI("/api/product-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-codes/:id} : Updates an existing productCode.
     *
     * @param id the id of the productCode to save.
     * @param productCode the productCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCode,
     * or with status {@code 400 (Bad Request)} if the productCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-codes/{id}")
    public ResponseEntity<ProductCode> updateProductCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductCode productCode
    ) throws URISyntaxException {
        log.debug("REST request to update ProductCode : {}, {}", id, productCode);
        if (productCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductCode result = productCodeService.save(productCode);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-codes/:id} : Partial updates given fields of an existing productCode, field will ignore if it is null
     *
     * @param id the id of the productCode to save.
     * @param productCode the productCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCode,
     * or with status {@code 400 (Bad Request)} if the productCode is not valid,
     * or with status {@code 404 (Not Found)} if the productCode is not found,
     * or with status {@code 500 (Internal Server Error)} if the productCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-codes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductCode> partialUpdateProductCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductCode productCode
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductCode partially : {}, {}", id, productCode);
        if (productCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductCode> result = productCodeService.partialUpdate(productCode);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productCode.getId().toString())
        );
    }

    /**
     * {@code GET  /product-codes} : get all the productCodes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productCodes in body.
     */
    @GetMapping("/product-codes")
    public List<ProductCode> getAllProductCodes(@RequestParam(required = false) String filter) {
        if ("product-is-null".equals(filter)) {
            log.debug("REST request to get all ProductCodes where product is null");
            return productCodeService.findAllWhereProductIsNull();
        }
        log.debug("REST request to get all ProductCodes");
        return productCodeService.findAll();
    }

    /**
     * {@code GET  /product-codes/:id} : get the "id" productCode.
     *
     * @param id the id of the productCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-codes/{id}")
    public ResponseEntity<ProductCode> getProductCode(@PathVariable Long id) {
        log.debug("REST request to get ProductCode : {}", id);
        Optional<ProductCode> productCode = productCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productCode);
    }

    /**
     * {@code DELETE  /product-codes/:id} : delete the "id" productCode.
     *
     * @param id the id of the productCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-codes/{id}")
    public ResponseEntity<Void> deleteProductCode(@PathVariable Long id) {
        log.debug("REST request to delete ProductCode : {}", id);
        productCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
