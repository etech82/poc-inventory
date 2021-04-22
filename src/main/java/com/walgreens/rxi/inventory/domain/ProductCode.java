package com.walgreens.rxi.inventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductCode.
 */
@Entity
@Table(name = "product_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "upc", nullable = false)
    private String upc;

    @Column(name = "barcode")
    private String barcode;

    @JsonIgnoreProperties(value = { "productCode", "category", "packaging", "catalogs" }, allowSetters = true)
    @OneToOne(mappedBy = "productCode")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductCode id(Long id) {
        this.id = id;
        return this;
    }

    public String getUpc() {
        return this.upc;
    }

    public ProductCode upc(String upc) {
        this.upc = upc;
        return this;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public ProductCode barcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Product getProduct() {
        return this.product;
    }

    public ProductCode product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        if (this.product != null) {
            this.product.setProductCode(null);
        }
        if (product != null) {
            product.setProductCode(this);
        }
        this.product = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCode)) {
            return false;
        }
        return id != null && id.equals(((ProductCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCode{" +
            "id=" + getId() +
            ", upc='" + getUpc() + "'" +
            ", barcode='" + getBarcode() + "'" +
            "}";
    }
}
