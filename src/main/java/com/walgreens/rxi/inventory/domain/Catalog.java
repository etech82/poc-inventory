package com.walgreens.rxi.inventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walgreens.rxi.inventory.domain.enumeration.CatalogStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Catalog.
 */
@Entity
@Table(name = "catalog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Catalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CatalogStatus status;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_catalog__product_code",
        joinColumns = @JoinColumn(name = "catalog_id"),
        inverseJoinColumns = @JoinColumn(name = "product_code_id")
    )
    @JsonIgnoreProperties(value = { "productCode", "category", "packaging", "catalogs" }, allowSetters = true)
    private Set<Product> productCodes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Catalog id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Catalog code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CatalogStatus getStatus() {
        return this.status;
    }

    public Catalog status(CatalogStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CatalogStatus status) {
        this.status = status;
    }

    public Set<Product> getProductCodes() {
        return this.productCodes;
    }

    public Catalog productCodes(Set<Product> products) {
        this.setProductCodes(products);
        return this;
    }

    public Catalog addProductCode(Product product) {
        this.productCodes.add(product);
        product.getCatalogs().add(this);
        return this;
    }

    public Catalog removeProductCode(Product product) {
        this.productCodes.remove(product);
        product.getCatalogs().remove(this);
        return this;
    }

    public void setProductCodes(Set<Product> products) {
        this.productCodes = products;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalog)) {
            return false;
        }
        return id != null && id.equals(((Catalog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catalog{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
