package com.tn.ps.product.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tn.ps.product.domain.Product;
import com.tn.ps.product.repository.ProductRepositoryImpl;
import com.tn.query.ValueMappers;
import com.tn.query.jpa.JpaQueryParser;
import com.tn.query.jpa.NameMappings;

@Configuration
class RepositoryConfiguration
{
  @Bean
  ProductRepositoryImpl productRepositoryImpl(EntityManager entityManager)
  {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

    return new ProductRepositoryImpl(
      entityManager,
      criteriaQuery,
      new JpaQueryParser(
        entityManager.getCriteriaBuilder(),
        NameMappings.forFields(Product.class, criteriaQuery),
        ValueMappers.forFields(Product.class)
      )
    );
  }
}
