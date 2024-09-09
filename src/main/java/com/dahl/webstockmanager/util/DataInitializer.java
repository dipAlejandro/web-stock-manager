package com.dahl.webstockmanager.util;

import com.dahl.webstockmanager.entities.Supplier;
import com.dahl.webstockmanager.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author Usuario
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private final SupplierService supplierService;
    
    /**
     *
     * @param supplierService
     */
    @Autowired
    public DataInitializer(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        
        if(supplierService.getAllSuppliers().isEmpty()){
            var defautlSupplier = new Supplier();
            defautlSupplier.setName("Default Supplier");
            defautlSupplier.setPhoneNumber("555");
            defautlSupplier.setAddress("Institution");
            defautlSupplier.setEmail("myemail@example.com");
            defautlSupplier.setWebsite("N/A");
            
            supplierService.addSupplier(defautlSupplier);
            
        }
        
    }
    
}
