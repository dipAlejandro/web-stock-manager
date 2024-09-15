package com.dahl.webstockmanager.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dahl.webstockmanager.entities.Supplier;
import com.dahl.webstockmanager.repository.ISupplierRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SupplierService {

    private ISupplierRepository repository;

    @Autowired
    public SupplierService(ISupplierRepository repo) {
        this.repository = repo;
    }

    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliersList = new ArrayList<>();
        for (Supplier element : repository.findAll()) {
            suppliersList.add(element);
        }
        return suppliersList;
    }

    @Transactional
    public Supplier addSupplier(Supplier s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }
        return repository.save(s);
    }

    @Transactional(readOnly = true)
    public Supplier getSupplierById(Integer id) throws EntityNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier with ID " + id + " not found"));
    }

    public Supplier getSupplierByName(String name) throws EntityNotFoundException {
        return repository.getSupplierByName(name).orElseThrow(() -> new EntityNotFoundException("Supplier with name " + name + " not found"));
    }

    @Transactional
    public Supplier updateSupplier(Supplier s) throws EntityNotFoundException {
        if (s == null || !repository.existsById(s.getId())) {
            throw new EntityNotFoundException("Supplier with ID " + s.getId() + " not found or Supplier is null");
        }
        return repository.save(s);
    }

    @Transactional
    public void deleteSupplier(Integer id) throws EntityNotFoundException {

        repository.deleteById(id);
    }
}
