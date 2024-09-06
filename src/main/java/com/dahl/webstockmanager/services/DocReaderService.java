package com.dahl.webstockmanager.services;

import com.dahl.webstockmanager.util.CSVReader;
import com.dahl.webstockmanager.entities.Exportable;
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
public class DocReaderService {
    
    private final CSVReader csvReader;
    private final ProductService producService;
    private final SupplierService supplierService;
    
    public DocReaderService(CSVReader csvReader, ProductService producService, SupplierService supplierService) {
        this.csvReader = csvReader;
        this.producService = producService;
        this.supplierService = supplierService;
    }

    /**
     *
     * @param file
     * @param from
     */
    public void readCsvAndPersist(MultipartFile file, String from) {
        Class clazz;
        switch (from.toLowerCase()) {
            case "products" -> {
                clazz = Product.class;
                List<Product> products = csvReader.read(file, clazz);
                // persist the information
                for (Product p : products) {
                    
                    var supplier = supplierService.getSupplierById(p.getTempSupplierId());
                    p.setSupplier(supplier);
                    
                    producService.addProduct(p);
                }
            }
            
            case "suppliers" -> {
                clazz = Supplier.class;
                List<Supplier> suppliers = csvReader.read(file, clazz);
                // persist the information
                for (Supplier s : suppliers) {
                    supplierService.addSupplier(s);
                }
            }
            default ->
                throw new AssertionError();
        }
    }
}
