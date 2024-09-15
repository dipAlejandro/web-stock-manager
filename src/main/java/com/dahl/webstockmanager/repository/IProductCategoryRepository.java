package com.dahl.webstockmanager.repository;

import com.dahl.webstockmanager.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dahl
 */
@Repository
public interface IProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    /**
     *
     * @param name
     * @return
     */
    @Query("SELECT c FROM ProductCategory c WHERE c.name = :name")
    public ProductCategory getCategoryByName(@Param("name") String name);
}
