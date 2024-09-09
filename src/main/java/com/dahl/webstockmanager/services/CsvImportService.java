package com.dahl.webstockmanager.services;

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

    public CsvImportService(CSVReader csvReader, ProductService producService, SupplierService supplierService) {
        this.reader = csvReader;
        this.producService = producService;
        this.supplierService = supplierService;
    }

    /**
     *
     * @param file
     */
    public void importProducts(MultipartFile file) {

        List<Product> products = reader.read(file, Product.class);
        // persist the information
        for (Product p : products) {

            var supplier = supplierService.getSupplierById(p.getTempSupplierId());
            p.setSupplier(supplier);

            producService.addProduct(p);
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
