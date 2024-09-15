package com.dahl.webstockmanager.services;

import com.dahl.webstockmanager.dto.ProductDTO;
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
    private final CategoryService categoryService;

    @Autowired
    public XlsxImportService(ProductService productService, SupplierService supplierService, com.dahl.webstockmanager.services.CategoryService categoryService) {
        this.productService = productService;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
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
                var dto = new ProductDTO();
                //System.out.println("code: "+String.valueOf(row.getCell(1).getNumericCellValue()));
                dto.setCode(row.getCell(1).getStringCellValue().replace("\"", ""));

                dto.setName(row.getCell(2).getStringCellValue());
                dto.setCategoryName(row.getCell(3).getStringCellValue());
                dto.setPrice(row.getCell(4).getNumericCellValue());
                dto.setDescription(row.getCell(5).getStringCellValue());
                dto.setSupplierName(row.getCell(6).getStringCellValue());
                productDataTransfer(dto);

                var product = productDataTransfer(dto);
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

    private Product productDataTransfer(ProductDTO dto) {
        var product = new Product();
        var category = categoryService.getCategoryByName(dto.getCategoryName());
        var supplier = supplierService.getSupplierByName(dto.getSupplierName());
        product.setCode(dto.getCode());
        product.setName(dto.getName());
        product.setCategory(category);
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setSupplier(supplier);
        product.setCreatedAt(dto.getCreatedAt());
        product.setUpdatedAt(dto.getUpdatedAt());
        return product;
    }

    private Supplier supplierDataTransfer() {
        return null;
    }
}
