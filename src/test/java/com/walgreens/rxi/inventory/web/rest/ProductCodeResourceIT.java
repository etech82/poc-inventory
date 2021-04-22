package com.walgreens.rxi.inventory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.walgreens.rxi.inventory.IntegrationTest;
import com.walgreens.rxi.inventory.domain.ProductCode;
import com.walgreens.rxi.inventory.repository.ProductCodeRepository;
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
 * Integration tests for the {@link ProductCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductCodeResourceIT {

    private static final String DEFAULT_UPC = "AAAAAAAAAA";
    private static final String UPDATED_UPC = "BBBBBBBBBB";

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductCodeRepository productCodeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductCodeMockMvc;

    private ProductCode productCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCode createEntity(EntityManager em) {
        ProductCode productCode = new ProductCode().upc(DEFAULT_UPC).barcode(DEFAULT_BARCODE);
        return productCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCode createUpdatedEntity(EntityManager em) {
        ProductCode productCode = new ProductCode().upc(UPDATED_UPC).barcode(UPDATED_BARCODE);
        return productCode;
    }

    @BeforeEach
    public void initTest() {
        productCode = createEntity(em);
    }

    @Test
    @Transactional
    void createProductCode() throws Exception {
        int databaseSizeBeforeCreate = productCodeRepository.findAll().size();
        // Create the ProductCode
        restProductCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCode))
            )
            .andExpect(status().isCreated());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeCreate + 1);
        ProductCode testProductCode = productCodeList.get(productCodeList.size() - 1);
        assertThat(testProductCode.getUpc()).isEqualTo(DEFAULT_UPC);
        assertThat(testProductCode.getBarcode()).isEqualTo(DEFAULT_BARCODE);
    }

    @Test
    @Transactional
    void createProductCodeWithExistingId() throws Exception {
        // Create the ProductCode with an existing ID
        productCode.setId(1L);

        int databaseSizeBeforeCreate = productCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUpcIsRequired() throws Exception {
        int databaseSizeBeforeTest = productCodeRepository.findAll().size();
        // set the field null
        productCode.setUpc(null);

        // Create the ProductCode, which fails.

        restProductCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCode))
            )
            .andExpect(status().isBadRequest());

        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductCodes() throws Exception {
        // Initialize the database
        productCodeRepository.saveAndFlush(productCode);

        // Get all the productCodeList
        restProductCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].upc").value(hasItem(DEFAULT_UPC)))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)));
    }

    @Test
    @Transactional
    void getProductCode() throws Exception {
        // Initialize the database
        productCodeRepository.saveAndFlush(productCode);

        // Get the productCode
        restProductCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, productCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productCode.getId().intValue()))
            .andExpect(jsonPath("$.upc").value(DEFAULT_UPC))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE));
    }

    @Test
    @Transactional
    void getNonExistingProductCode() throws Exception {
        // Get the productCode
        restProductCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductCode() throws Exception {
        // Initialize the database
        productCodeRepository.saveAndFlush(productCode);

        int databaseSizeBeforeUpdate = productCodeRepository.findAll().size();

        // Update the productCode
        ProductCode updatedProductCode = productCodeRepository.findById(productCode.getId()).get();
        // Disconnect from session so that the updates on updatedProductCode are not directly saved in db
        em.detach(updatedProductCode);
        updatedProductCode.upc(UPDATED_UPC).barcode(UPDATED_BARCODE);

        restProductCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductCode.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductCode))
            )
            .andExpect(status().isOk());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeUpdate);
        ProductCode testProductCode = productCodeList.get(productCodeList.size() - 1);
        assertThat(testProductCode.getUpc()).isEqualTo(UPDATED_UPC);
        assertThat(testProductCode.getBarcode()).isEqualTo(UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void putNonExistingProductCode() throws Exception {
        int databaseSizeBeforeUpdate = productCodeRepository.findAll().size();
        productCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productCode.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductCode() throws Exception {
        int databaseSizeBeforeUpdate = productCodeRepository.findAll().size();
        productCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductCode() throws Exception {
        int databaseSizeBeforeUpdate = productCodeRepository.findAll().size();
        productCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCodeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCode))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductCodeWithPatch() throws Exception {
        // Initialize the database
        productCodeRepository.saveAndFlush(productCode);

        int databaseSizeBeforeUpdate = productCodeRepository.findAll().size();

        // Update the productCode using partial update
        ProductCode partialUpdatedProductCode = new ProductCode();
        partialUpdatedProductCode.setId(productCode.getId());

        restProductCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCode.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductCode))
            )
            .andExpect(status().isOk());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeUpdate);
        ProductCode testProductCode = productCodeList.get(productCodeList.size() - 1);
        assertThat(testProductCode.getUpc()).isEqualTo(DEFAULT_UPC);
        assertThat(testProductCode.getBarcode()).isEqualTo(DEFAULT_BARCODE);
    }

    @Test
    @Transactional
    void fullUpdateProductCodeWithPatch() throws Exception {
        // Initialize the database
        productCodeRepository.saveAndFlush(productCode);

        int databaseSizeBeforeUpdate = productCodeRepository.findAll().size();

        // Update the productCode using partial update
        ProductCode partialUpdatedProductCode = new ProductCode();
        partialUpdatedProductCode.setId(productCode.getId());

        partialUpdatedProductCode.upc(UPDATED_UPC).barcode(UPDATED_BARCODE);

        restProductCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCode.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductCode))
            )
            .andExpect(status().isOk());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeUpdate);
        ProductCode testProductCode = productCodeList.get(productCodeList.size() - 1);
        assertThat(testProductCode.getUpc()).isEqualTo(UPDATED_UPC);
        assertThat(testProductCode.getBarcode()).isEqualTo(UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void patchNonExistingProductCode() throws Exception {
        int databaseSizeBeforeUpdate = productCodeRepository.findAll().size();
        productCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productCode.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductCode() throws Exception {
        int databaseSizeBeforeUpdate = productCodeRepository.findAll().size();
        productCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductCode() throws Exception {
        int databaseSizeBeforeUpdate = productCodeRepository.findAll().size();
        productCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCode))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCode in the database
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductCode() throws Exception {
        // Initialize the database
        productCodeRepository.saveAndFlush(productCode);

        int databaseSizeBeforeDelete = productCodeRepository.findAll().size();

        // Delete the productCode
        restProductCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productCode.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductCode> productCodeList = productCodeRepository.findAll();
        assertThat(productCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
