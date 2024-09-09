package com.dahl.webstockmanager.controllers;

import com.dahl.webstockmanager.entities.Product;
import com.dahl.webstockmanager.entities.ProductCategory;
import com.dahl.webstockmanager.services.ProductService;
import com.dahl.webstockmanager.services.SupplierService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = {"/", "/products"})
public class ProductController {

    private final ProductService productService;
    private final SupplierService supplierService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductService ps, SupplierService ss) {
        this.productService = ps;
        this.supplierService = ss;
    }

    @GetMapping("/all")
    public String showProducts(Model model) {
        logger.info("[GET] Showing products");
        model.addAttribute("products", productService.getAllProducts());
        return "products/show-products";
    }

    @GetMapping("details/{id}")
    public String showProductDetail(@PathVariable Integer id, Model model) {
        logger.info("[GET] Product {} detail", id);
        model.addAttribute("product", productService.getProductById(id));
        return "products/product-detail";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        logger.info("[GET] Showing new product form");
        populateProductModel(model);
        model.addAttribute("newProduct", new Product());
        return "products/new-product-form";
    }

    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("newProduct") Product newProduct,
            @RequestParam("supplierId") String supplierId,
            BindingResult bResult, RedirectAttributes redirectAttributes, Model model) {

        logger.info("[POST] Trying to add new product");

        if (bResult.hasErrors()) {
            logger.warn("[POST] Validation errors while adding new product");
            populateProductModel(model);
            return "products/new-product-form";
        }

        try {
            Integer intSupplierId = Integer.parseInt(supplierId);
            newProduct.setSupplier(supplierService.getSupplierById(intSupplierId));
            productService.addProduct(newProduct);
            redirectAttributes.addFlashAttribute("message", "Product added successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            logger.info("[POST] New product added {}", newProduct);
            return "redirect:/products/new";
        } catch (NumberFormatException | EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", "Error adding product.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            logger.warn("[POST] Error adding new product", e);
            return "products/new-product-form";
        }
    }

    @GetMapping("/update/{id}")
    public String updateProductForm(@PathVariable Integer id, Model model) {
        logger.info("[GET] Showing update form for product {}", id);
        populateProductModel(model);
        model.addAttribute("product", productService.getProductById(id));
        return "products/update-product-form";
    }

    @PostMapping("/saveupdate")
    public String saveUpdate(@Valid @ModelAttribute("product") Product p,
            @RequestParam("supplierId") String supplierId,
            BindingResult bResult, Model model) {
        logger.info("[POST] Trying to update product {}", p.getId());

        if (bResult.hasErrors()) {
            logger.warn("[POST] Validation errors while updating product {}", p.getId());
            populateProductModel(model);
            return "products/update-product-form";
        }

        try {
            Integer intSupplierId = Integer.parseInt(supplierId);
            p.setSupplier(supplierService.getSupplierById(intSupplierId));
            var updatedProduct = productService.updateProduct(p);
            logger.info("[POST] Product {} updated successfully", p.getId());
            model.addAttribute("productUpdated", updatedProduct);
            return "products/save-update";
        } catch (NumberFormatException | EntityNotFoundException e) {
            logger.warn("[POST] Error updating product {}", p.getId(), e);
            populateProductModel(model);
            return "products/update-product-form";
        }
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam String id) {
        try {
            Integer intId = Integer.valueOf(id);
            logger.info("[DELETE] Trying to delete product with ID: {}", intId);

            // Verificar si el producto existe antes de intentar eliminarlo
            if (!productService.existsById(intId)) {
                logger.warn("[DELETE] Product with ID: {} does not exist", intId);
                return "redirect:/products/all";
            }

            productService.deleteProduct(intId);
            logger.info("[DELETE] Product with ID: {} removed successfully", intId);
        } catch (NumberFormatException e) {
            logger.warn("[DELETE] Invalid ID format: {}", id, e);
        } catch (EntityNotFoundException e) {
            logger.warn("[DELETE] Error deleting product with ID: {}", id, e);
        } catch (Exception e) {
            logger.error("[DELETE] Unexpected error while deleting product with ID: {}", id, e);
        }
        return "redirect:/products/all";
    }

    private void populateProductModel(Model model) {
        model.addAttribute("sections", ProductCategory.values());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
    }
}
