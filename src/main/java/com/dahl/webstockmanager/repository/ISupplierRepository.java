package com.dahl.webstockmanager.repository;


import com.dahl.webstockmanager.entities.Supplier;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dahl
 */
@Repository
public interface ISupplierRepository extends JpaRepository<Supplier, Integer>{
    @Query("SELECT s FROM Supplier s WHERE s.name = :name")
    public Optional<Supplier> getSupplierByName(@Param("name") String name);
}
