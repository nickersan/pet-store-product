package com.tn.ps.product.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import static com.tn.lang.Strings.isNullOrWhitespace;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public Iterable<Product> productFor(@RequestParam(required = false) MultiValueMap<String, String> params)
  {
    try
    {
      String query = QUERY_BUILDER.build(params);

      return isNullOrWhitespace(query) ? this.productRepository.findAll() : this.productRepository.findWhere(query);
    }
    catch (IllegalParameterException | QueryParseException e)
    {
      throw new ResponseStatusException(BAD_REQUEST, e.getMessage(), e);
    }
  }

  @RequestMapping(method = { POST, PUT },  value = "/")
  public Product save(@RequestBody Product product)
  {
    return this.productRepository.save(product);
  }

  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable("id") long id)
  {
    this.productRepository.deleteById(id);
  }
}
