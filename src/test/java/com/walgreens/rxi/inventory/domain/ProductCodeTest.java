package com.walgreens.rxi.inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.walgreens.rxi.inventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCode.class);
        ProductCode productCode1 = new ProductCode();
        productCode1.setId(1L);
        ProductCode productCode2 = new ProductCode();
        productCode2.setId(productCode1.getId());
        assertThat(productCode1).isEqualTo(productCode2);
        productCode2.setId(2L);
        assertThat(productCode1).isNotEqualTo(productCode2);
        productCode1.setId(null);
        assertThat(productCode1).isNotEqualTo(productCode2);
    }
}
