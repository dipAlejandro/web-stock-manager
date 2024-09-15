package com.dahl.webstockmanager.dto;

import com.dahl.webstockmanager.entities.Exportable;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author dahl
 */
@Component
public class ProductDTO implements Exportable {

    @CsvBindByName(column = "id")
    private Integer id;

    @CsvBindByName(column = "code")
    private String code;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "price")
    private double price;

    @CsvBindByName(column = "description")
    private String description;

    @CsvCustomBindByName(column = "created at", converter = com.dahl.webstockmanager.util.LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    @CsvCustomBindByName(column = "last update", converter = com.dahl.webstockmanager.util.LocalDateTimeConverter.class)
    private LocalDateTime updatedAt;

    @CsvBindByName(column = "supplier")
    private String supplierName;

    @CsvBindByName(column = "category")
    private String categoryName;

    public ProductDTO() {
    }

    public ProductDTO(Integer id, String code, String name, double price, String description, LocalDateTime createdAt, LocalDateTime updatedAt, String supplierName, String categoryName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.supplierName = supplierName;
        this.categoryName = categoryName;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String[] ToRowContent() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return new String[]{
            String.valueOf(this.id),
            this.code,
            this.name,
            this.categoryName != null && !categoryName.isBlank() ? this.categoryName : "No category",
            "$ " + this.price,
            this.supplierName != null && !supplierName.isBlank() ? this.supplierName : "No supplier",
            this.description,
            this.createdAt != null ? this.createdAt.format(formatter) : "N/A",
            this.updatedAt != null ? this.updatedAt.format(formatter) : "N/A"
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
        map.put("Category", categoryName != null && !categoryName.isBlank() ? this.categoryName : "No category");
        map.put("Price", price);
        map.put("Supplier", supplierName != null && !supplierName.isBlank() ? supplierName : "No supplier");
        map.put("Created At", createdAt != null ? createdAt.format(formatter) : "N/A");
        map.put("Last Update", updatedAt != null ? updatedAt.format(formatter) : "N/A");

        return map;
    }
}
