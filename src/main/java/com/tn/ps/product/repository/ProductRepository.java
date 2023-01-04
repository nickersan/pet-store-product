package com.tn.ps.product.repository;

import org.springframework.data.repository.CrudRepository;

import com.tn.ps.product.domain.Product;
import com.tn.query.jpa.QueryableRepository;

public interface ProductRepository extends CrudRepository<Product, Long>, QueryableRepository<Product> {}