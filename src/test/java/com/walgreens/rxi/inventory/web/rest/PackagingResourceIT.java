package com.walgreens.rxi.inventory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.walgreens.rxi.inventory.IntegrationTest;
import com.walgreens.rxi.inventory.domain.Packaging;
import com.walgreens.rxi.inventory.repository.PackagingRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PackagingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PackagingResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Double DEFAULT_GROS_WEIGHT = 1D;
    private static final Double UPDATED_GROS_WEIGHT = 2D;

    private static final Double DEFAULT_NET_WEIGHT = 1D;
    private static final Double UPDATED_NET_WEIGHT = 2D;

    private static final Double DEFAULT_LENGTH = 1D;
    private static final Double UPDATED_LENGTH = 2D;

    private static final Double DEFAULT_WIDTH = 1D;
    private static final Double UPDATED_WIDTH = 2D;

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;

    private static final String ENTITY_API_URL = "/api/packagings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PackagingRepository packagingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPackagingMockMvc;

    private Packaging packaging;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Packaging createEntity(EntityManager em) {
        Packaging packaging = new Packaging()
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .grosWeight(DEFAULT_GROS_WEIGHT)
            .netWeight(DEFAULT_NET_WEIGHT)
            .length(DEFAULT_LENGTH)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT);
        return packaging;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Packaging createUpdatedEntity(EntityManager em) {
        Packaging packaging = new Packaging()
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .grosWeight(UPDATED_GROS_WEIGHT)
            .netWeight(UPDATED_NET_WEIGHT)
            .length(UPDATED_LENGTH)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT);
        return packaging;
    }

    @BeforeEach
    public void initTest() {
        packaging = createEntity(em);
    }

    @Test
    @Transactional
    void createPackaging() throws Exception {
        int databaseSizeBeforeCreate = packagingRepository.findAll().size();
        // Create the Packaging
        restPackagingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packaging))
            )
            .andExpect(status().isCreated());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeCreate + 1);
        Packaging testPackaging = packagingList.get(packagingList.size() - 1);
        assertThat(testPackaging.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPackaging.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testPackaging.getGrosWeight()).isEqualTo(DEFAULT_GROS_WEIGHT);
        assertThat(testPackaging.getNetWeight()).isEqualTo(DEFAULT_NET_WEIGHT);
        assertThat(testPackaging.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testPackaging.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testPackaging.getHeight()).isEqualTo(DEFAULT_HEIGHT);
    }

    @Test
    @Transactional
    void createPackagingWithExistingId() throws Exception {
        // Create the Packaging with an existing ID
        packaging.setId(1L);

        int databaseSizeBeforeCreate = packagingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackagingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packaging))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = packagingRepository.findAll().size();
        // set the field null
        packaging.setName(null);

        // Create the Packaging, which fails.

        restPackagingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packaging))
            )
            .andExpect(status().isBadRequest());

        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = packagingRepository.findAll().size();
        // set the field null
        packaging.setQuantity(null);

        // Create the Packaging, which fails.

        restPackagingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packaging))
            )
            .andExpect(status().isBadRequest());

        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPackagings() throws Exception {
        // Initialize the database
        packagingRepository.saveAndFlush(packaging);

        // Get all the packagingList
        restPackagingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packaging.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].grosWeight").value(hasItem(DEFAULT_GROS_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].netWeight").value(hasItem(DEFAULT_NET_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())));
    }

    @Test
    @Transactional
    void getPackaging() throws Exception {
        // Initialize the database
        packagingRepository.saveAndFlush(packaging);

        // Get the packaging
        restPackagingMockMvc
            .perform(get(ENTITY_API_URL_ID, packaging.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(packaging.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.grosWeight").value(DEFAULT_GROS_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.netWeight").value(DEFAULT_NET_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH.doubleValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPackaging() throws Exception {
        // Get the packaging
        restPackagingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPackaging() throws Exception {
        // Initialize the database
        packagingRepository.saveAndFlush(packaging);

        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();

        // Update the packaging
        Packaging updatedPackaging = packagingRepository.findById(packaging.getId()).get();
        // Disconnect from session so that the updates on updatedPackaging are not directly saved in db
        em.detach(updatedPackaging);
        updatedPackaging
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .grosWeight(UPDATED_GROS_WEIGHT)
            .netWeight(UPDATED_NET_WEIGHT)
            .length(UPDATED_LENGTH)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT);

        restPackagingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPackaging.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPackaging))
            )
            .andExpect(status().isOk());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate);
        Packaging testPackaging = packagingList.get(packagingList.size() - 1);
        assertThat(testPackaging.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPackaging.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPackaging.getGrosWeight()).isEqualTo(UPDATED_GROS_WEIGHT);
        assertThat(testPackaging.getNetWeight()).isEqualTo(UPDATED_NET_WEIGHT);
        assertThat(testPackaging.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testPackaging.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPackaging.getHeight()).isEqualTo(UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void putNonExistingPackaging() throws Exception {
        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();
        packaging.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackagingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, packaging.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packaging))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPackaging() throws Exception {
        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();
        packaging.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackagingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packaging))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPackaging() throws Exception {
        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();
        packaging.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackagingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packaging))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePackagingWithPatch() throws Exception {
        // Initialize the database
        packagingRepository.saveAndFlush(packaging);

        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();

        // Update the packaging using partial update
        Packaging partialUpdatedPackaging = new Packaging();
        partialUpdatedPackaging.setId(packaging.getId());

        partialUpdatedPackaging.name(UPDATED_NAME).quantity(UPDATED_QUANTITY).netWeight(UPDATED_NET_WEIGHT).height(UPDATED_HEIGHT);

        restPackagingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPackaging.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPackaging))
            )
            .andExpect(status().isOk());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate);
        Packaging testPackaging = packagingList.get(packagingList.size() - 1);
        assertThat(testPackaging.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPackaging.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPackaging.getGrosWeight()).isEqualTo(DEFAULT_GROS_WEIGHT);
        assertThat(testPackaging.getNetWeight()).isEqualTo(UPDATED_NET_WEIGHT);
        assertThat(testPackaging.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testPackaging.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testPackaging.getHeight()).isEqualTo(UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void fullUpdatePackagingWithPatch() throws Exception {
        // Initialize the database
        packagingRepository.saveAndFlush(packaging);

        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();

        // Update the packaging using partial update
        Packaging partialUpdatedPackaging = new Packaging();
        partialUpdatedPackaging.setId(packaging.getId());

        partialUpdatedPackaging
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .grosWeight(UPDATED_GROS_WEIGHT)
            .netWeight(UPDATED_NET_WEIGHT)
            .length(UPDATED_LENGTH)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT);

        restPackagingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPackaging.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPackaging))
            )
            .andExpect(status().isOk());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate);
        Packaging testPackaging = packagingList.get(packagingList.size() - 1);
        assertThat(testPackaging.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPackaging.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPackaging.getGrosWeight()).isEqualTo(UPDATED_GROS_WEIGHT);
        assertThat(testPackaging.getNetWeight()).isEqualTo(UPDATED_NET_WEIGHT);
        assertThat(testPackaging.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testPackaging.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPackaging.getHeight()).isEqualTo(UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void patchNonExistingPackaging() throws Exception {
        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();
        packaging.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackagingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, packaging.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packaging))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPackaging() throws Exception {
        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();
        packaging.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackagingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packaging))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPackaging() throws Exception {
        int databaseSizeBeforeUpdate = packagingRepository.findAll().size();
        packaging.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackagingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packaging))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Packaging in the database
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePackaging() throws Exception {
        // Initialize the database
        packagingRepository.saveAndFlush(packaging);

        int databaseSizeBeforeDelete = packagingRepository.findAll().size();

        // Delete the packaging
        restPackagingMockMvc
            .perform(delete(ENTITY_API_URL_ID, packaging.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Packaging> packagingList = packagingRepository.findAll();
        assertThat(packagingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
