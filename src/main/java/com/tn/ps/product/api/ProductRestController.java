package com.tn.ps.product.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Collection;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tn.ps.product.domain.Product;
import com.tn.ps.product.repository.ProductRepository;
import com.tn.query.QueryParseException;
import com.tn.service.api.IllegalParameterException;
import com.tn.service.api.query.QueryBuilder;

@RestController
@CrossOrigin(origins = "${ps.product.origins:*}", allowedHeaders = "${ps.product.allowed-headers:*}")
public class ProductRestController
{
  private static final QueryBuilder QUERY_BUILDER = new QueryBuilder(Product.class);

  private final ProductRepository productRepository;

  public ProductRestController(ProductRepository productRepository)
  {
    this.productRepository = productRepository;
  }

  @GetMapping(value = "/{id}")
  public Product productForId(@PathVariable("id") long id)
  {
    return this.productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found for ID: " + id));
  }

  @GetMapping(value = "/")
  public Collection<Product> productFor(@RequestParam(required = false) MultiValueMap<String, String> params)
  {
    try
    {
      return this.productRepository.findWhere(QUERY_BUILDER.build(params));
    }
    catch (IllegalParameterException | QueryParseException e)
    {
      throw new ResponseStatusException(BAD_REQUEST, e.getMessage(), e);
    }
  }
}
