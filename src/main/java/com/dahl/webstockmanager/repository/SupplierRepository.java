package com.dahl.webstockmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.dahl.webstockmanager.entities.Supplier;

public interface SupplierRepository extends CrudRepository<Supplier, Integer>{

}
