package com.dahl.webstockmanager.services;

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
     * @param withHeaders
     * @return
     */
    public void readCsvAndPersist(MultipartFile file, Boolean withHeaders, String from) {
        Class clazz;
        switch (from.toLowerCase()) {
            case "prod":
                clazz = Product.class;
                List<Product> products = csvReader.read(file, withHeaders, clazz);
                // persist the information
                for (Product p : products) {
                    producService.addProduct(p);
                }
                break;

            case "supp":
                clazz = Supplier.class;
                List<Supplier> suppliers = csvReader.read(file, withHeaders, clazz);
                break;
            default:
                throw new AssertionError();
        }
    }
}
