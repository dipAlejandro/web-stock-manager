package com.dahl.webstockmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.dahl.webstockmanager.entities.Supplier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISupplierRepository extends CrudRepository<Supplier, Integer>{
}
