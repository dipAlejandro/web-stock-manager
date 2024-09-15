package com.dahl.webstockmanager.mapper;

import com.dahl.webstockmanager.dto.ProductDTO;
import com.dahl.webstockmanager.entities.Product;
import com.dahl.webstockmanager.entities.ProductCategory;
import com.dahl.webstockmanager.entities.Supplier;
import com.dahl.webstockmanager.repository.IProductCategoryRepository;
import com.dahl.webstockmanager.repository.ISupplierRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductMapper {

    @Autowired
    private IProductCategoryRepository categoryRepository;

    @Autowired
    private ISupplierRepository supplierRepository;

    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setCode(product.getCode());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setCreatedAt(product.getCreatedAt());
        productDTO.setUpdatedAt(product.getUpdatedAt());

        if (product.getCategory() != null) {
            productDTO.setCategoryName(product.getCategory().getName());
        }
        if (product.getSupplier() != null) {
            productDTO.setSupplierName(product.getSupplier().getName());
        }

        return productDTO;
    }

    public List<ProductDTO> allToDTO(List<Product> products) {
        List<ProductDTO> list = new ArrayList<>();
        for (Product p : products) {
            list.add(toDTO(p));
        }
        return list;
    }

    public Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productDTO.getId());
        product.setCode(productDTO.getCode());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCreatedAt(productDTO.getCreatedAt());
        product.setUpdatedAt(productDTO.getUpdatedAt());

        // Lookup and set category if the category name is present
        if (productDTO.getCategoryName() != null) {
            ProductCategory category = categoryRepository.getCategoryByName(productDTO.getCategoryName());
            product.setCategory(category);
        }

        // Lookup and set supplier if the supplier name is present and valid
        if (productDTO.getSupplierName() != null && !productDTO.getSupplierName().isEmpty()) {
            Optional<Supplier> supplierOpt = supplierRepository.getSupplierByName(productDTO.getSupplierName());
            if (supplierOpt.isPresent()) {
                product.setSupplier(supplierOpt.get());
            } else {
                throw new IllegalArgumentException("Supplier not found for name: " + productDTO.getSupplierName());
            }
        } else {
            product.setSupplier(null); // Or handle appropriately if necessary
        }

        return product;
    }

    public List<Product> allToEntity(List<ProductDTO> dtos) {
        List<Product> list = new ArrayList<>();
        for (ProductDTO dto : dtos) {
            list.add(toEntity(dto));
        }
        return list;
    }
}
