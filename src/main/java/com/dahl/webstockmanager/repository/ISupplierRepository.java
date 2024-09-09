package com.dahl.webstockmanager.repository;


import com.dahl.webstockmanager.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dahl
 */
@Repository
public interface ISupplierRepository extends JpaRepository<Supplier, Integer>{
}
