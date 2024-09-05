package com.dahl.webstockmanager.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dahl.webstockmanager.entities.Exportable;
import com.dahl.webstockmanager.services.DocReaderService;
import com.dahl.webstockmanager.services.DocsGenerationService;
import com.dahl.webstockmanager.services.ProductService;
import com.dahl.webstockmanager.services.SupplierService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/docs")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    private final DocsGenerationService docService;
    private final ProductService productService;
    private final SupplierService supplierService;
    private final DocReaderService docReaderService;

    @Autowired
    public DocumentController(ProductService ps, SupplierService ss, DocsGenerationService dgs, DocReaderService drs) {
        this.productService = ps;
        this.supplierService = ss;
        this.docService = dgs;
        this.docReaderService = drs;
    }

    @GetMapping("/export/pdf")
    public void exportToPdf(HttpServletResponse httpServletResponse, @RequestParam(value = "from") String from) {

        List<? extends Exportable> records;

        switch (from.toLowerCase()) {
            case "products" ->
                records = productService.getAllProducts();

            case "suppliers" ->
                records = supplierService.getAllSuppliers();

            default ->
                throw new IllegalArgumentException("Unexpected value: " + from);
        }

        try {

            logger.info("[POST] Trying to convert table to PDF");

            // Convertir cada registro en un Map<String, Object>
            var allRowsContent = records.stream().map(Exportable::ToRowContent).collect(Collectors.toList());

            String[] headers;
            String filename;
            String title;

            if (from.equals("products")) {

                headers = new String[]{"ID", "Code", "Name", "Section", "Price", "Supplier", "Description",
                    "Created At", "Last Update"};
                filename = "products.pdf";
                title = "Products List";

            } else {
                headers = new String[]{"ID", "Name", "Phone Number", "Email", "Address", "Web Site", "Created At",
                    "Last Update"};
                filename = "suppliers.pdf";
                title = "Suppliers List";
            }

            docService.generatePDF(httpServletResponse, allRowsContent, headers, title, filename);

        } catch (IOException e) {
            logger.error("Error while creating empty PDF", e);
            e.printStackTrace();
        }
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse httpServletResponse, @RequestParam(value = "from") String from) {

        List<? extends Exportable> records;

        switch (from.toLowerCase()) {
            case "products" ->
                records = productService.getAllProducts();

            case "suppliers" ->
                records = supplierService.getAllSuppliers();

            default ->
                throw new IllegalArgumentException("Unexpected value: " + from);
        }

        try {
            logger.info("[POST] Trying to convert table to Excel");

            var allRowsContent = records.stream().map(Exportable::toRowMap).collect(Collectors.toList());
            String[] headers;
            String filename;

            if (from.equals("products")) {
                headers = new String[]{"ID", "Code", "Name", "Section", "Price", "Supplier", "Description",
                    "Created At", "Last Update"};
                filename = "products.xlsx";
            } else {
                headers = new String[]{"ID", "Name", "Phone Number", "Email", "Address", "Web Site", "Created At",
                    "Last Update"};
                filename = "suppliers.xlsx";
            }

            docService.generateExcel(httpServletResponse, allRowsContent, headers, filename);

        } catch (IOException e) {
            logger.error("Error while creating empty Excel", e);
            e.printStackTrace();
        }
    }

    @PostMapping("/import/csv")
    public String uploadCsvFile(@RequestParam(value = "csvFile") MultipartFile file, @RequestParam("from") String from,
            @RequestParam(value = "withHeaders", defaultValue = "false") Boolean withHeaders) {

        System.out.println("New file csv was uploaded");
        System.out.println("With headers: " + withHeaders);
        System.out.println("From: " + from);
        System.out.println("With size: " + file.getSize());
        
        docReaderService.readCsvAndPersist(file, withHeaders, from);
        
        return "redirect:/products";
    }
}
