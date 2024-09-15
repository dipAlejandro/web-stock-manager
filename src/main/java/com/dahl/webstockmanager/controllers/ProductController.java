package com.dahl.webstockmanager.controllers;

import com.dahl.webstockmanager.dto.ProductDTO;
import com.dahl.webstockmanager.mapper.ProductMapper;
import com.dahl.webstockmanager.services.CategoryService;
import com.dahl.webstockmanager.services.ProductService;
import com.dahl.webstockmanager.services.SupplierService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
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
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductService ps, SupplierService ss, CategoryService cs, com.dahl.webstockmanager.mapper.ProductMapper productMapper) {
        this.productService = ps;
        this.supplierService = ss;
        this.categoryService = cs;
        this.productMapper = productMapper;
    }

    @GetMapping("/all")
    public String showProducts(Model model) {
        logger.info("[GET] Showing products");
        List<ProductDTO> products = productService.getAllProducts().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        return "products/show-products";
    }

    @GetMapping("details/{id}")
    public String showProductDetail(@PathVariable Integer id, Model model) {
        logger.info("[GET] Product {} detail", id);
        model.addAttribute("product", productMapper.toDTO(productService.getProductById(id)));
        return "products/product-detail";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        logger.info("[GET] Showing new product form");
        populateProductModel(model);
        model.addAttribute("newProduct", new ProductDTO());
        return "products/new-product-form";
    }

    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("newProduct") ProductDTO productDtoFromView,
            BindingResult bResult, RedirectAttributes redirectAttributes, Model model) {

        logger.info("[POST] Trying to add new product");

        // Verificar errores
        if (bResult.hasErrors()) {
            logger.warn("[POST] Validation errors while adding new product");
            populateProductModel(model);
            return "products/new-product-form";
        }

        try {

            var newProductToPersist = productMapper.toEntity(productDtoFromView);

            productService.addProduct(newProductToPersist);
            redirectAttributes.addFlashAttribute("message", "Product added successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            logger.info("[POST] New product added {}", newProductToPersist);

            return "redirect:/products/new";

        } catch (NumberFormatException | EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", "Error adding product.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            logger.warn("[POST] Error adding new product", e);
            return "products/new-product-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Unexpected error occurred.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            logger.error("[POST] Unexpected error while adding new product", e);
            return "products/new-product-form";
        }
    }

    @GetMapping("/update/{id}")
    public String updateProductForm(@PathVariable Integer id, Model model) {
        logger.info("[GET] Showing update form for product {}", id);
        populateProductModel(model);
        model.addAttribute("product", productMapper.toDTO(productService.getProductById(id)));
        return "products/update-product-form";
    }

    @PostMapping("/saveupdate")
    public String saveUpdate(@Valid @ModelAttribute("product") ProductDTO productDto,
            @RequestParam("supplierId") String supplierId,
            BindingResult bResult, Model model) {

        logger.info("[POST] Trying to update product {}", productDto.getId());

        if (bResult.hasErrors()) {
            logger.warn("[POST] Validation errors while updating product {}", productDto.getId());
            populateProductModel(model);
            return "products/update-product-form";
        }

        try {
            var product = productMapper.toEntity(productDto);
            Integer intSupplierId = Integer.valueOf(supplierId);
            product.setSupplier(supplierService.getSupplierById(intSupplierId));

            productService.updateProduct(product);
            logger.info("[POST] Product {} updated successfully", product.getId());
            var productDtoUpdated = productMapper.toDTO(product);
            model.addAttribute("productUpdated", productDtoUpdated);
            return "products/save-update";
        } catch (NumberFormatException | EntityNotFoundException e) {
            logger.warn("[POST] Error updating product {}", productDto.getId(), e);
            populateProductModel(model);
            return "products/update-product-form";
        } catch (Exception e) {
            logger.error("[POST] Unexpected error while updating product {}", productDto.getId(), e);
            populateProductModel(model);
            return "products/update-product-form";
        }
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam String id) {
        try {
            Integer intId = Integer.valueOf(id);
            logger.info("[DELETE] Trying to delete product with ID: {}", intId);

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
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
    }
}
