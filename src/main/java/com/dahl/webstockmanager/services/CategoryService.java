package com.dahl.webstockmanager.services;

import com.dahl.webstockmanager.entities.ProductCategory;
import com.dahl.webstockmanager.repository.IProductCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// CategoryService.java
@Service
public class CategoryService {

    private final IProductCategoryRepository repo;

    @Autowired
    public CategoryService(IProductCategoryRepository categoryRepository) {
        this.repo = categoryRepository;
    }

    public List<ProductCategory> getAllCategories() {
        return repo.findAll();
    }

    public ProductCategory getCategoryById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " not found"));
    }

    public ProductCategory getCategoryByName(String name) {
        return repo.getCategoryByName(name);
    }

    public ProductCategory addCategory(ProductCategory pc) {
        if (pc == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        return repo.save(pc);
    }

    public ProductCategory updateCategory(ProductCategory pc) {
        if (pc == null || !repo.existsById(pc.getCategoryId())) {
            throw new EntityNotFoundException("Category with ID " + pc.getCategoryId() + " not found");
        }
        return repo.save(pc);
    }

    public void deleteCategory(Integer id) {
        repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Category with ID " + id + " not found"));
        repo.deleteById(id);
    }
}
