package com.tn.ps.product.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;

import com.tn.ps.product.domain.Product;
import com.tn.query.QueryParser;
import com.tn.query.jpa.AbstractQueryableRepository;

public class ProductRepositoryImpl extends AbstractQueryableRepository<Product>
{
  public ProductRepositoryImpl(EntityManager entityManager, CriteriaQuery<Product> criteriaQuery, QueryParser<Predicate> queryParser)
  {
    super(entityManager, criteriaQuery, queryParser);
  }
}
