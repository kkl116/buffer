package com.gubu.buffer.domain.product;

import com.gubu.buffer.domain.model.ProductCost;

import java.util.Optional;

public interface ProductCostRepositoryAdapter {
    Optional<ProductCost> findById(Long id);

    ProductCost save(Long productId, ProductCost productCost);

    void deleteById(Long id);

    void deleteAll();

    void update(Long costId, ProductCost productCost);
}
