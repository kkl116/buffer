package com.gubu.buffer.domain.product;

import com.gubu.buffer.domain.model.ProductDimension;

public interface ProductDimensionRepositoryAdapter {
    void save(Long productId, ProductDimension productDimension);
}
