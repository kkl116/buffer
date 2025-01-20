package com.gubu.buffer.domain.product;

import com.gubu.buffer.domain.model.ProductDimensions;

public interface ProductDimensionRepositoryAdapter {

    void save(Long productId, ProductDimensions productDimension);

    void update(Long productId, ProductDimensions productDimension);
}
