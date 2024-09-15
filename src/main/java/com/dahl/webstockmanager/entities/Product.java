package com.dahl.webstockmanager.entities;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;

    @Size(min = 8, max = 15, message = "Code must be between 8 and 20 characters")
    @Pattern(regexp = "^[0-9A-za-z]+$", message = "Code must contain only numbers and characters")
    private String code;

    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = "Name must contain only upper and lower case characters and numbers")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @CsvBindByName(column = "price")
    private double price;

    @Size(max = 50, message = "Description must not be grater than 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = "Description must contain only upper and lower case characters and numbers")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "fk_supplier", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "fk_category")
    private ProductCategory category;

    public Product() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", code=" + code + ", name=" + name + ", description=" + description + ", section/category="
                + category.getName() + ", price=" + price + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", supplier="
                + (supplier != null ? supplier.getName() : "No supplier") + "]";
    }

}
