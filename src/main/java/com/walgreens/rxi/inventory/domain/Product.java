package com.walgreens.rxi.inventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walgreens.rxi.inventory.domain.enumeration.ProductStatus;
import com.walgreens.rxi.inventory.domain.enumeration.ProductType;
import com.walgreens.rxi.inventory.domain.enumeration.StorageType;
import com.walgreens.rxi.inventory.domain.enumeration.UnitOfMeasurement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "company")
    private String company;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProductType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "storage_type", nullable = false)
    private StorageType storageType;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "sales_unit")
    private UnitOfMeasurement salesUnit;

    @Column(name = "sales_quantity", precision = 21, scale = 2)
    private BigDecimal salesQuantity;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ProductCode productCode;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Category category;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Packaging packaging;

    @ManyToMany(mappedBy = "productCodes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productCodes" }, allowSetters = true)
    private Set<Catalog> catalogs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return this.company;
    }

    public Product company(String company) {
        this.company = company;
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public ProductType getType() {
        return this.type;
    }

    public Product type(ProductType type) {
        this.type = type;
        return this;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public StorageType getStorageType() {
        return this.storageType;
    }

    public Product storageType(StorageType storageType) {
        this.storageType = storageType;
        return this;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public UnitOfMeasurement getSalesUnit() {
        return this.salesUnit;
    }

    public Product salesUnit(UnitOfMeasurement salesUnit) {
        this.salesUnit = salesUnit;
        return this;
    }

    public void setSalesUnit(UnitOfMeasurement salesUnit) {
        this.salesUnit = salesUnit;
    }

    public BigDecimal getSalesQuantity() {
        return this.salesQuantity;
    }

    public Product salesQuantity(BigDecimal salesQuantity) {
        this.salesQuantity = salesQuantity;
        return this;
    }

    public void setSalesQuantity(BigDecimal salesQuantity) {
        this.salesQuantity = salesQuantity;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Product image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Product imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public ProductStatus getStatus() {
        return this.status;
    }

    public Product status(ProductStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public ProductCode getProductCode() {
        return this.productCode;
    }

    public Product productCode(ProductCode productCode) {
        this.setProductCode(productCode);
        return this;
    }

    public void setProductCode(ProductCode productCode) {
        this.productCode = productCode;
    }

    public Category getCategory() {
        return this.category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Packaging getPackaging() {
        return this.packaging;
    }

    public Product packaging(Packaging packaging) {
        this.setPackaging(packaging);
        return this;
    }

    public void setPackaging(Packaging packaging) {
        this.packaging = packaging;
    }

    public Set<Catalog> getCatalogs() {
        return this.catalogs;
    }

    public Product catalogs(Set<Catalog> catalogs) {
        this.setCatalogs(catalogs);
        return this;
    }

    public Product addCatalog(Catalog catalog) {
        this.catalogs.add(catalog);
        catalog.getProductCodes().add(this);
        return this;
    }

    public Product removeCatalog(Catalog catalog) {
        this.catalogs.remove(catalog);
        catalog.getProductCodes().remove(this);
        return this;
    }

    public void setCatalogs(Set<Catalog> catalogs) {
        if (this.catalogs != null) {
            this.catalogs.forEach(i -> i.removeProductCode(this));
        }
        if (catalogs != null) {
            catalogs.forEach(i -> i.addProductCode(this));
        }
        this.catalogs = catalogs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", company='" + getCompany() + "'" +
            ", type='" + getType() + "'" +
            ", storageType='" + getStorageType() + "'" +
            ", price=" + getPrice() +
            ", salesUnit='" + getSalesUnit() + "'" +
            ", salesQuantity=" + getSalesQuantity() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
