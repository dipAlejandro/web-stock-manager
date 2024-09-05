package com.dahl.webstockmanager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dahl.webstockmanager.entities.Product;
import com.dahl.webstockmanager.entities.ProductCategory;
import com.dahl.webstockmanager.services.ProductService;
import com.dahl.webstockmanager.services.SupplierService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Controller
public class ProductController {

    private final ProductService productService;
    private final SupplierService supplierService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductService ps, SupplierService ss) {
        this.productService = ps;
        this.supplierService = ss;
    }

    @GetMapping(value = {"/", "/products"})
    public String showProducts(Model model) {
        logger.info("[GET] Showing products");
        model.addAttribute("products", productService.getAllProducts());
        return "products/show-products";
    }

    @GetMapping("/products/{id}")
    public String showProductDetail(@PathVariable(value = "id") Integer id, Model model) {
        logger.info("[GET] Product {} detail", id);
        model.addAttribute("product", productService.getProductById(id));
        return "products/product-detail";
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        logger.info("[GET] Showing new products form");
        model.addAttribute("sections", ProductCategory.values());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("newProduct", new Product());
        return "products/new-product-form";
    }

    @PostMapping("/products/new")
    public String createProduct(@RequestParam("supplierId") String supplierId,
            @Valid @ModelAttribute("newProduct") Product newProduct, BindingResult bResult,
            RedirectAttributes redirectAttributes, Model model) {

        logger.info("[POST] Trying to add new product");

        Integer intSupplierId = Integer.parseInt(supplierId);

        try {
            if (bResult.hasErrors()) {
                logger.warn("[POST] An error has occurred while trying to add a new Product. Cause by form validation error");
                model.addAttribute("sections", ProductCategory.values());
                model.addAttribute("suppliers", supplierService.getAllSuppliers());
                return "products/new-product-form";
            } else {
                newProduct.setSupplier(supplierService.getSupplierById(intSupplierId));
                productService.addProduct(newProduct);
                redirectAttributes.addFlashAttribute("message", "Product added successfully!");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                logger.info("[POST] New product added " + newProduct.toString());
                return "redirect:/products/new";
            }
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", "Error adding product.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            logger.warn("[POST] An error has occurred while trying to add a new Product");
            e.printStackTrace();
            return "products/new-product-form";
        }
    }

    @GetMapping("/products/update/{id}")
    public String updateProductForm(@PathVariable(value = "id") Integer id, Model model) {
        logger.info("[GET] Showing update form");
        model.addAttribute("sections", ProductCategory.values());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("product", productService.getProductById(id));
        return "products/update-product-form";
    }

    @PostMapping("/products/saveupdate")
    public String saveUpdate(@RequestParam("supplierId") String supplierId, @Valid @ModelAttribute("product") Product p,
            BindingResult bResult, Model model) {
        logger.info("[POST] Trying to update product {}", p.getId());

        try {
            if (!bResult.hasErrors()) {
                Integer intSupplierId = Integer.parseInt(supplierId);
                p.setSupplier(supplierService.getSupplierById(intSupplierId));
                var updatedProduct = productService.updateProduct(p);
                logger.info("[POST] The product {} has been updated successfully", p.getId());
                model.addAttribute("productUpdated", updatedProduct);
                return "products/save-update";
            } else {
                logger.warn("[POST] A error ocurred while trying to update the product with ID : {}. Validation Error or Operation Error", p.getId());
                model.addAttribute("sections", ProductCategory.values());
                model.addAttribute("suppliers", supplierService.getAllSuppliers());
                return "products/update-product-form";
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return "products/update-product-form";
        }
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(value = "id") Integer id) {
        logger.info("[DELETE] Trying to delete product with ID: {}", id);

        try {
            productService.deleteProduct(id);
            logger.info("[DELETE] The product with ID: {} was removed successfully", id);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            logger.warn("[DELETE] An error has occurred while trying to delete the Product " + id);

        }
        return "redirect:/products";
    }
}
