package com.dahl.webstockmanager.services;

import com.dahl.webstockmanager.entities.Product;
import com.dahl.webstockmanager.repository.IProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// ProductService.java
@Service
public class ProductService {

    private final IProductRepository repository;

    @Autowired
    public ProductService(IProductRepository repo) {
        this.repository = repo;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Transactional
    public Product addProduct(Product p) {
        if (p == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return repository.save(p);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID " + id + " not found"));
    }

    @Transactional
    public Product updateProduct(Product p) {
        if (p == null || !repository.existsById(p.getId())) {
            throw new EntityNotFoundException("Product with ID " + p.getId() + " not found or Product is null");
        }
        return repository.save(p);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID " + id + " not found"));
        repository.deleteByIdCustom(id);
    }

    public boolean existsById(Integer intId) {
        return repository.existsById(intId);
    }
}
