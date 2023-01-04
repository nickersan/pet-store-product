package com.tn.ps.product.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.tn.ps.product.domain.Product;
import com.tn.ps.product.repository.ProductRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProductRestControllerIntegrationTest
{
  private static final Product PRODUCT = new Product(1L, "Test Product", "A product for testing", "TP1", 12.34);
  private static final ParameterizedTypeReference<List<Product>> PRODUCT_LIST = new ParameterizedTypeReference<>() {};

  @MockBean
  ProductRepository productRepository;

  @Autowired
  TestRestTemplate testRestTemplate;

  @Test
  void shouldReturnProductForId()
  {
    when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));

    ResponseEntity<Product> response = testRestTemplate.getForEntity("/{productId}", Product.class, PRODUCT.getId());

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(PRODUCT, response.getBody());
  }

  @Test
  void shouldReturnProductForName()
  {
    when(productRepository.findWhere("name=" + PRODUCT.getName())).thenReturn(List.of(PRODUCT));

    ResponseEntity<List<Product>> response = testRestTemplate.exchange("/?name={productName}", HttpMethod.GET, null, PRODUCT_LIST, PRODUCT.getName());

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(List.of(PRODUCT), response.getBody());
  }

  @Test
  void shouldReturnProductForNameWithQuery()
  {
    when(productRepository.findWhere("name=" + PRODUCT.getName())).thenReturn(List.of(PRODUCT));

    ResponseEntity<List<Product>> response = testRestTemplate.exchange("/?q=name={productName}", HttpMethod.GET, null, PRODUCT_LIST, PRODUCT.getName());

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(List.of(PRODUCT), response.getBody());
  }
}
