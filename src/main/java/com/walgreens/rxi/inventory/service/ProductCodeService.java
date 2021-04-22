package com.walgreens.rxi.inventory.service;

import com.walgreens.rxi.inventory.domain.ProductCode;
import com.walgreens.rxi.inventory.repository.ProductCodeRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductCode}.
 */
@Service
@Transactional
public class ProductCodeService {

    private final Logger log = LoggerFactory.getLogger(ProductCodeService.class);

    private final ProductCodeRepository productCodeRepository;

    public ProductCodeService(ProductCodeRepository productCodeRepository) {
        this.productCodeRepository = productCodeRepository;
    }

    /**
     * Save a productCode.
     *
     * @param productCode the entity to save.
     * @return the persisted entity.
     */
    public ProductCode save(ProductCode productCode) {
        log.debug("Request to save ProductCode : {}", productCode);
        return productCodeRepository.save(productCode);
    }

    /**
     * Partially update a productCode.
     *
     * @param productCode the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductCode> partialUpdate(ProductCode productCode) {
        log.debug("Request to partially update ProductCode : {}", productCode);

        return productCodeRepository
            .findById(productCode.getId())
            .map(
                existingProductCode -> {
                    if (productCode.getUpc() != null) {
                        existingProductCode.setUpc(productCode.getUpc());
                    }
                    if (productCode.getBarcode() != null) {
                        existingProductCode.setBarcode(productCode.getBarcode());
                    }

                    return existingProductCode;
                }
            )
            .map(productCodeRepository::save);
    }

    /**
     * Get all the productCodes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductCode> findAll() {
        log.debug("Request to get all ProductCodes");
        return productCodeRepository.findAll();
    }

    /**
     *  Get all the productCodes where Product is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductCode> findAllWhereProductIsNull() {
        log.debug("Request to get all productCodes where Product is null");
        return StreamSupport
            .stream(productCodeRepository.findAll().spliterator(), false)
            .filter(productCode -> productCode.getProduct() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one productCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductCode> findOne(Long id) {
        log.debug("Request to get ProductCode : {}", id);
        return productCodeRepository.findById(id);
    }

    /**
     * Delete the productCode by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductCode : {}", id);
        productCodeRepository.deleteById(id);
    }
}
