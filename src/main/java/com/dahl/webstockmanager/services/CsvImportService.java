package com.dahl.webstockmanager.services;

import com.dahl.webstockmanager.dto.ProductDTO;
import com.dahl.webstockmanager.util.CSVReader;
import com.dahl.webstockmanager.entities.Product;
import com.dahl.webstockmanager.entities.Supplier;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Usuario
 */
@Service
public class CsvImportService {

    private final CSVReader reader;
    private final ProductService producService;
    private final SupplierService supplierService;
    private final CategoryService categoryService;

    public CsvImportService(CSVReader csvReader, ProductService producService, SupplierService supplierService, CategoryService categoryService) {
        this.reader = csvReader;
        this.producService = producService;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
    }

    /**
     *
     * @param file
     */
    public void importProducts(MultipartFile file) {

        List<ProductDTO> products = reader.read(file, ProductDTO.class);
        // persist the information
        for (ProductDTO dto : products) {
            
            var category = categoryService.getCategoryByName(dto.getCategoryName());
            var supplier = supplierService.getSupplierByName(dto.getSupplierName());
            
            var product = new Product();
            product.setCode(dto.getCode());
            product.setName(dto.getName());
            product.setCategory(category);
            product.setPrice(dto.getPrice());
            product.setDescription(dto.getDescription());
            product.setSupplier(supplier);
            product.setCreatedAt(dto.getCreatedAt());
            product.setUpdatedAt(dto.getUpdatedAt());
            
            producService.addProduct(product);
        }
    }

    public void importSupplier(MultipartFile file) {
        List<Supplier> suppliers = reader.read(file, Supplier.class);
        // persist the information
        for (Supplier s : suppliers) {
            supplierService.addSupplier(s);
        }
    }
}
