package com.dahl.webstockmanager.entities;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Product implements Exportable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CsvBindByName(column = "id")
    private Integer id;

    @Size(min = 8, max = 14, message = "Code must be between 8 and 14 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Code must contain only numbers")
    @CsvBindByName(column = "code")
    private String code;

    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = "Name must contain only upper and lower case characters and numbers")
    @CsvBindByName(column = "name")
    private String name;

    @Size(min = 3, max = 15, message = "Description must be between 3 and 15 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = "Description must contain only upper and lower case characters and numbers")
    @CsvBindByName(column = "description")
    private String description;
    @CsvBindByName(column = "section")
    private String section;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @CsvBindByName(column = "price")
    private double price;

    @CreationTimestamp
    @CsvCustomBindByName(column = "created at", converter = com.dahl.webstockmanager.util.LocalDateTimeConverter.class)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @CsvCustomBindByName(column = "last update", converter = com.dahl.webstockmanager.util.LocalDateTimeConverter.class)
    private LocalDateTime updatedAt;
    @CsvBindByName(column = "supplier id")
    // Usado para relacionar el proveedor al importar desde .csv
    private Integer tempSupplierId;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    //@CsvBindAndJoinByName(column = "supplier", elementType = Supplier.class)
    private Supplier supplier;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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

    public Integer getTempSupplierId() {
        return tempSupplierId;
    }

    public void setTempSupplierId(Integer tempSupplierId) {
        this.tempSupplierId = tempSupplierId;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", code=" + code + ", name=" + name + ", description=" + description + ", section="
                + section + ", price=" + price + ", createAt=" + createdAt + ", updateAt=" + updatedAt + ", supplier="
                + supplier.getName() + "]";
    }

    @Override
    public String[] ToRowContent() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return new String[]{
            String.valueOf(this.id),
            this.code,
            this.name,
            this.description,
            this.section,
            "$ " + this.price,
            this.supplier.getName(),
            this.createdAt.format(formatter),
            this.updatedAt.format(formatter)
        };
    }

    @Override
    public Map<String, Object> toRowMap() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

        Map<String, Object> map = new HashMap<>();
        map.put("ID", id);
        map.put("Code", code);
        map.put("Name", name);
        map.put("Description", description);
        map.put("Section", section);
        map.put("Price", price);
        map.put("Supplier", supplier.getName());
        map.put("Created At", createdAt.format(formatter));
        map.put("Last Update", updatedAt.format(formatter));

        return map;
    }

}
