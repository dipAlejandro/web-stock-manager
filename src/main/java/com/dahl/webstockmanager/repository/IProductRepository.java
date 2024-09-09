package com.dahl.webstockmanager.repository;

import org.springframework.stereotype.Repository;

import com.dahl.webstockmanager.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

    /**
     *
     * @param id
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.id = :id")
    void deleteByIdCustom(@Param("id") Integer id);
}
