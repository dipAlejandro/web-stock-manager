package com.dahl.webstockmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dahl.webstockmanager.entities.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>  {
	
}
