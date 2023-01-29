package com.tn.ps.product.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
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
  void shouldReturnAllProducts()
  {
    when(productRepository.findAll()).thenReturn(List.of(PRODUCT));

    ResponseEntity<List<Product>> response = testRestTemplate.exchange("/", GET, null, PRODUCT_LIST);

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(List.of(PRODUCT), response.getBody());
  }

  @Test
  void shouldReturnProductForName()
  {
    when(productRepository.findWhere("name=" + PRODUCT.getName())).thenReturn(List.of(PRODUCT));

    ResponseEntity<List<Product>> response = testRestTemplate.exchange("/?name={productName}", GET, null, PRODUCT_LIST, PRODUCT.getName());

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(List.of(PRODUCT), response.getBody());
  }

  @Test
  void shouldReturnProductForNameWithQuery()
  {
    when(productRepository.findWhere("name=" + PRODUCT.getName())).thenReturn(List.of(PRODUCT));

    ResponseEntity<List<Product>> response = testRestTemplate.exchange("/?q=name={productName}", GET, null, PRODUCT_LIST, PRODUCT.getName());

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(List.of(PRODUCT), response.getBody());
  }

  @Test
  void shouldReturnErrorForGetWithInvalidParam()
  {
    ResponseEntity<Void> response = testRestTemplate.exchange("/?invalid=X", GET, null, Void.class);

    assertEquals(BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void shouldReturnErrorForGetWithInvalidQuery()
  {
    ResponseEntity<Void> response = testRestTemplate.exchange("/?q=invalid=X", GET, null, Void.class);

    assertEquals(BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void shouldReturnErrorForGetOnUncaughtException()
  {
    when(productRepository.findWhere("name=X")).thenThrow(new RuntimeException());

    ResponseEntity<Void> response = testRestTemplate.exchange("/?name=X", GET, null, Void.class);

    assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void shouldSaveProductFromPost()
  {
    when(productRepository.save(PRODUCT)).thenReturn(PRODUCT);

    ResponseEntity<Product> response = testRestTemplate.exchange("/", POST, new HttpEntity<>(PRODUCT), Product.class);

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(PRODUCT, response.getBody());
  }

  @Test
  void shouldReturnErrorForPostOnUncaughtException()
  {
    when(productRepository.save(PRODUCT)).thenThrow(new RuntimeException());

    ResponseEntity<Void> response = testRestTemplate.exchange("/", POST, new HttpEntity<>(PRODUCT), Void.class);

    assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void shouldSaveProductFromPut()
  {
    when(productRepository.save(PRODUCT)).thenReturn(PRODUCT);

    ResponseEntity<Product> response = testRestTemplate.exchange("/", PUT, new HttpEntity<>(PRODUCT), Product.class);

    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(PRODUCT, response.getBody());
  }

  @Test
  void shouldReturnErrorForPutOnUncaughtException()
  {
    when(productRepository.save(PRODUCT)).thenThrow(new RuntimeException());

    ResponseEntity<Void> response = testRestTemplate.exchange("/", PUT, new HttpEntity<>(PRODUCT), Void.class);

    assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void shouldDeleteProductFromDelete()
  {
    ResponseEntity<Void> response = testRestTemplate.exchange("/{productId}", DELETE, new HttpEntity<>(PRODUCT), Void.class, PRODUCT.getId());

    assertTrue(response.getStatusCode().is2xxSuccessful());

    verify(productRepository).deleteById(PRODUCT.getId());
  }

  @Test
  void shouldReturnErrorForDeleteOnUncaughtException()
  {
    doThrow(new RuntimeException()).when(productRepository).deleteById(PRODUCT.getId());

    ResponseEntity<Void> response = testRestTemplate.exchange("/{productId}", DELETE, new HttpEntity<>(PRODUCT), Void.class, PRODUCT.getId());

    assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
