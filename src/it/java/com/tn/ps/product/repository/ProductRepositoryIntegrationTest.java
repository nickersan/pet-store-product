package com.tn.ps.product.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tn.ps.product.domain.Product;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ProductRepositoryIntegrationTest
{
  @Autowired
  ProductRepository productRepository;

  @Test
  void shouldSupportCrud()
  {
    Product product = this.productRepository.save(new Product("Test Product", "A shiny new product for testing", "PD1234", 12.95));
    assertNotNull(product.getId());

    assertEquals(product, this.productRepository.findById(product.getId()).orElseThrow());
    assertEquals(List.of(product), this.productRepository.findWhere("sku = PD1234"));
    assertEquals(List.of(product), this.productRepository.findWhere("name ≈ Test*"));
    assertEquals(List.of(product), this.productRepository.findWhere("description ≈ A * new product *"));
    assertEquals(List.of(product), this.productRepository.findWhere("price > 12"));

    this.productRepository.delete(product);

    assertTrue(this.productRepository.findById(product.getId()).isEmpty());
  }
}
