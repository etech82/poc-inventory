package com.walgreens.rxi.inventory.service;

import com.walgreens.rxi.inventory.domain.Packaging;
import com.walgreens.rxi.inventory.repository.PackagingRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Packaging}.
 */
@Service
@Transactional
public class PackagingService {

    private final Logger log = LoggerFactory.getLogger(PackagingService.class);

    private final PackagingRepository packagingRepository;

    public PackagingService(PackagingRepository packagingRepository) {
        this.packagingRepository = packagingRepository;
    }

    /**
     * Save a packaging.
     *
     * @param packaging the entity to save.
     * @return the persisted entity.
     */
    public Packaging save(Packaging packaging) {
        log.debug("Request to save Packaging : {}", packaging);
        return packagingRepository.save(packaging);
    }

    /**
     * Partially update a packaging.
     *
     * @param packaging the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Packaging> partialUpdate(Packaging packaging) {
        log.debug("Request to partially update Packaging : {}", packaging);

        return packagingRepository
            .findById(packaging.getId())
            .map(
                existingPackaging -> {
                    if (packaging.getName() != null) {
                        existingPackaging.setName(packaging.getName());
                    }
                    if (packaging.getQuantity() != null) {
                        existingPackaging.setQuantity(packaging.getQuantity());
                    }
                    if (packaging.getGrosWeight() != null) {
                        existingPackaging.setGrosWeight(packaging.getGrosWeight());
                    }
                    if (packaging.getNetWeight() != null) {
                        existingPackaging.setNetWeight(packaging.getNetWeight());
                    }
                    if (packaging.getLength() != null) {
                        existingPackaging.setLength(packaging.getLength());
                    }
                    if (packaging.getWidth() != null) {
                        existingPackaging.setWidth(packaging.getWidth());
                    }
                    if (packaging.getHeight() != null) {
                        existingPackaging.setHeight(packaging.getHeight());
                    }

                    return existingPackaging;
                }
            )
            .map(packagingRepository::save);
    }

    /**
     * Get all the packagings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Packaging> findAll() {
        log.debug("Request to get all Packagings");
        return packagingRepository.findAll();
    }

    /**
     * Get one packaging by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Packaging> findOne(Long id) {
        log.debug("Request to get Packaging : {}", id);
        return packagingRepository.findById(id);
    }

    /**
     * Delete the packaging by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Packaging : {}", id);
        packagingRepository.deleteById(id);
    }
}
