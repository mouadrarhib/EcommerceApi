package com.ecommerce.api.EcommerceAPI.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByName(String name);
    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByActiveTrue();
    List<Product> findByActiveTrueAndCategoryId(UUID categoryId);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    // Add more custom query methods as needed for filtering, sorting, etc.
}