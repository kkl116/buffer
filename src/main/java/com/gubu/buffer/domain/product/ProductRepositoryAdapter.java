package com.gubu.buffer.domain.product;

import com.gubu.buffer.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepositoryAdapter {
    List<Product> findAll(Set<ProductField> fields);

    Product save(Product product);

    void deleteById(Long productId);

    Optional<Product> findById(Long productId, Set<ProductField> fields);

    void deleteAll();

    void updateProduct(Long productId, Product product);
}
