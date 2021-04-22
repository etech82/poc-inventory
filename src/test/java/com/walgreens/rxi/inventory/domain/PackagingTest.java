package com.walgreens.rxi.inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.walgreens.rxi.inventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PackagingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Packaging.class);
        Packaging packaging1 = new Packaging();
        packaging1.setId(1L);
        Packaging packaging2 = new Packaging();
        packaging2.setId(packaging1.getId());
        assertThat(packaging1).isEqualTo(packaging2);
        packaging2.setId(2L);
        assertThat(packaging1).isNotEqualTo(packaging2);
        packaging1.setId(null);
        assertThat(packaging1).isNotEqualTo(packaging2);
    }
}
