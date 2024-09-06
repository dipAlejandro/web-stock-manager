package com.dahl.webstockmanager.services;

import com.dahl.webstockmanager.entities.Product;
import com.dahl.webstockmanager.entities.Supplier;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Usuario
 */
@Service
public class XlsxImportService {

    private final ProductService productService;
    private final SupplierService supplierService;

    @Autowired
    public XlsxImportService(ProductService productService, SupplierService supplierService) {
        this.productService = productService;
        this.supplierService = supplierService;
    }

    /**
     *
     * @param file
     * @throws IOException
     */
    public void importProducts(MultipartFile file) throws IOException {
        try (Workbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet productSheet = wb.getSheet("products");

            for (int i = 1; i <= productSheet.getLastRowNum(); i++) {
                var row = productSheet.getRow(i);
                var product = new Product();

                //System.out.println("code: "+String.valueOf(row.getCell(1).getNumericCellValue()));
                product.setCode(row.getCell(1).getStringCellValue().replace("\"", ""));

                product.setName(row.getCell(2).getStringCellValue());
                product.setSection(row.getCell(3).getStringCellValue());
                product.setPrice(row.getCell(4).getNumericCellValue());
                product.setDescription(row.getCell(5).getStringCellValue());

                int supplierId = (int) row.getCell(6).getNumericCellValue();
                product.setSupplier(supplierService.getSupplierById(supplierId));

                productService.addProduct(product);
            }
        }

    }

    public void importSuppliers(MultipartFile file) throws IOException {
        try (Workbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet supplierSheet = wb.getSheet("suppliers");

            for (int i = 1; i <= supplierSheet.getLastRowNum(); i++) {
                var row = supplierSheet.getRow(i);
                var supplier = new Supplier();

                supplier.setName(row.getCell(1).getStringCellValue());
                supplier.setPhoneNumber(row.getCell(2).getStringCellValue().replace("\"", ""));
                supplier.setEmail(row.getCell(3).getStringCellValue());
                supplier.setAddress(row.getCell(4).getStringCellValue());
                supplier.setWebsite((row.getCell(5).getStringCellValue() == null) ? "N/A" : row.getCell(5).getStringCellValue().replace("\"", ""));

                supplierService.addSupplier(supplier);
            }
        }
    }
}
