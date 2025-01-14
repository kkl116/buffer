package com.gubu.buffer.domain.product;

import com.gubu.buffer.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryAdapter {
    List<Product> findAll();

    Product save(Product product);

    void deleteById(Long productId);

    Optional<Product> findById(Long productId);

    void deleteAll();

    void updateProduct(Long productId, Product product);
}
