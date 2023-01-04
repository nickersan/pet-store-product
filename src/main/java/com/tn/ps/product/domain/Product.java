package com.tn.ps.product.domain;

import java.util.Objects;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "product")
@Cacheable(false)
@Getter
@ToString
public class Product
{
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productId")
  @SequenceGenerator(name = "productId", sequenceName = "product_id", allocationSize = 1)
  @Column(name = "product_id", updatable = false)
  private Long id;

  @Column(name = "product_name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "sku")
  private String sku;

  @Column(name = "price")
  private double price;

  public Product(String name, String description, String sku, double price)
  {
    this(null, name, description, sku, price);
  }

  public Product(Long id, String name, String description, String sku, double price)
  {
    this.id = id;
    this.name = name;
    this.description = description;
    this.sku = sku;
    this.price = price;
  }

  //Required for JPA.
  protected Product() {}

  @Override
  public boolean equals(Object other)
  {
    return this == other || (
      other != null &&
      getClass().equals(other.getClass()) &&
      Objects.equals(this.id, ((Product)other).id) &&
      Objects.equals(this.sku, ((Product)other).sku) &&
      Objects.equals(this.name, ((Product)other).name) &&
      Objects.equals(this.description, ((Product)other).description) &&
      this.price == ((Product)other).price
    );
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(this.id, this.sku, this.name, this.description);
  }
}