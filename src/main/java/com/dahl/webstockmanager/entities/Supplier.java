package com.dahl.webstockmanager.entities;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "supplier")
public class Supplier implements Exportable, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CsvBindByName(column = "id")
    private Integer id;

    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    @Column(name = "name", nullable = false)
    @CsvBindByName(column = "name")
    private String name;

    @Size(min = 3, message = "Phone Number must be greater than or equal to 3")
    @Pattern(regexp = "^[0-9]+$", message = "Phone Number must contain only numbers")
    @Column(name = "phone_number")
    @CsvBindByName(column = "phone number")
    private String phoneNumber;

    @Email
    @Column(name = "email")
    @CsvBindByName(column = "email")
    private String email;

    @Size(min = 5, max = 50, message = "Address must be between 5 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ]*$", message = "Address must contain only upper and lower case characters and numbers")
    @Column(name = "address")
    @CsvBindByName(column = "address")
    private String address;

    @Column(name = "website")
    @CsvBindByName(column = "website")
    private String website;

    @Column(name = "created_at", nullable = false)
    @CsvCustomBindByName(column = "created at", converter = com.dahl.webstockmanager.util.LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @CsvCustomBindByName(column = "last update", converter = com.dahl.webstockmanager.util.LocalDateTimeConverter.class)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> products;

    public Supplier() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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
        StringBuilder builder = new StringBuilder();
        builder.append("Supplier [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", phoneNumber=");
        builder.append(phoneNumber);
        builder.append(", email=");
        builder.append(email);
        builder.append(", address=");
        builder.append(address);
        builder.append(", website=");
        builder.append(website);
        builder.append(", createdAt=");
        builder.append(createdAt);
        builder.append(", updatedAt=");
        builder.append(updatedAt);
        builder.append(", products=");
        builder.append(products);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String[] ToRowContent() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return new String[]{String.valueOf(this.id), this.name, this.phoneNumber, this.email, this.address,
            this.website, this.createdAt != null ? this.createdAt.format(formatter) : "N/A",
            this.updatedAt != null ? this.updatedAt.format(formatter) : "N/A"};
    }

    @Override
    public Map<String, Object> toRowMap() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        map.put("ID", id);
        map.put("Name", name);
        map.put("Phone Number", phoneNumber);
        map.put("Email", email);
        map.put("Address", address);
        map.put("Web Site", website);
        map.put("Created At", createdAt.format(formatter));
        map.put("Last Update", updatedAt.format(formatter));

        return map;
    }
}
