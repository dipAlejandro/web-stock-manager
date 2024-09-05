package com.dahl.webstockmanager.controllers;

import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dahl.webstockmanager.entities.Supplier;
import com.dahl.webstockmanager.services.SupplierService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supService;

    private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @GetMapping("/show-all")
    public String showSuppliers(Model model) {
        logger.info("[GET] Showing Suppliers");
        List<Supplier> suppliers = supService.getAllSuppliers();

        model.addAttribute("suppliers", suppliers);

        return "suppliers/show-suppliers";
    }

    @GetMapping("/new")
    public String newSupplier(Model model) {

        logger.info("[GET] Showing new supplier form");

        model.addAttribute("newSupplier", new Supplier());

        return "suppliers/new-supplier-form";
    }

    @PostMapping("/new")
    public String createSupplier(@Valid @ModelAttribute("newSupplier") Supplier newSup, BindingResult bResult,
            RedirectAttributes redirectAttributes) throws Exception {

        logger.info("[POST] Trying to add new supplier");

        if (bResult.hasErrors()) {
            logger.warn("[POST] An error occurred while trying to add a new supplier. Cause by form validation error");
            return "suppliers/new-supplier-form";
        }

        if (newSup != null) {

            supService.addSupplier(newSup);

            redirectAttributes.addFlashAttribute("message", "Supplier added successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");

            logger.info("[POST] New supplier added " + newSup.toString());

        } else {
            redirectAttributes.addFlashAttribute("message", "Error adding supplier.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            logger.warn("[POST] An error has occurred while trying to add a new Supplier");
        }

        return "redirect:/suppliers/new";
    }

    @GetMapping("/show/{id}")
    public String showSupplierDetail(@PathVariable(value = "id") Integer id, Model model) {
        logger.info("[GET] Supplier {} detail", id);
        model.addAttribute("supplier", supService.getSupplierById(id));
        return "suppliers/supplier-detail";
    }

    @GetMapping("/update/{id}")
    public String updateSupplierForm(@PathVariable(value = "id") Integer id, Model model) {

        logger.info("[GET] Showing update form");

        model.addAttribute("supplier", supService.getSupplierById(id));

        return "suppliers/update-supplier-form";
    }

    @PostMapping("/saveupdate")
    public String saveUpdate(@Valid @ModelAttribute("supplier") Supplier s, BindingResult bResult, Model model) {

        logger.info("[POST] Trying to update supplier with ID: {}", s.getId());

        try {

            if (!bResult.hasErrors()) { // Success

                var updatedSup = supService.updateSupplier(s);

                logger.info("[POST] The supplier {} has been updated successfully.", updatedSup.getId());

                model.addAttribute("supplierUpdated", updatedSup);

                return "suppliers/save-update";

            } else { // Error
                logger.warn(
                        "[POST] A error ocurred while trying to update the supplier with ID : {}. Validation Error o Operation Error",
                        s.getId());
                return "suppliers/update-supplier-form";
            }

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return "suppliers/update-supplier-form";
        }

    }

    @PostMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable(value = "id") Integer id) {
        logger.info("[DELETE] Trying to delete supplier with ID: {}", id);

        try {

            supService.deleteSupplier(id);
            logger.info("[DELETE] The supplier with ID: {} was removed successfully", id);

        } catch (EntityNotFoundException e) {

            logger.warn("[DELETE] An error has occurred while trying to delete the supplier with ID: {} ", id);
            e.printStackTrace();
        }
        return "redirect:/suppliers/show-all";
    }
}
