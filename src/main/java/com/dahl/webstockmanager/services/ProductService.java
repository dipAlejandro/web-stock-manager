package com.dahl.webstockmanager.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dahl.webstockmanager.entities.Product;
import com.dahl.webstockmanager.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    private ProductRepository repository;

    public ProductService(ProductRepository repo) {
        this.repository = repo;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        for (Product element : repository.findAll()) {
            productList.add(element);
        }
        return productList;
    }

    @Transactional
    public Product addProduct(Product p) throws IllegalArgumentException {
        if (p == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return repository.save(p);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Integer id) throws EntityNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID " + id + " not found"));
    }

    @Transactional
    public Product updateProduct(Product p) throws EntityNotFoundException {
        if (p == null || !repository.existsById(p.getId())) {
            throw new EntityNotFoundException("Product with ID " + p.getId() + " not found or Product is null");
        }
        return repository.save(p);
    }

    @Transactional
    public void deleteProduct(Integer id) throws EntityNotFoundException {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Product with ID " + id + " not found");
        }
        repository.delete(getProductById(id));
    }
}
