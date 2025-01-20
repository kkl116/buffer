package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.domain.model.ProductDimensions;
import com.gubu.buffer.domain.product.ProductDimensionRepositoryAdapter;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductDimensionRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import static com.gubu.buffer.infrastructure.database.postgreql.product.adapter.EntityModelMapper.toEntity;

@Component
public class ProductDimensionRepositoryAdapterImpl implements ProductDimensionRepositoryAdapter {

    private final ProductRepository productRepository;
    private final ProductDimensionRepository productDimensionRepository;

    public ProductDimensionRepositoryAdapterImpl(
        ProductRepository productRepository,
        ProductDimensionRepository productDimensionRepository
    ) {
        this.productRepository = productRepository;
        this.productDimensionRepository = productDimensionRepository;
    }

    @Override
    @Transactional
    public void save(Long productId, ProductDimensions productDimension) {
        var productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException(String.format("Product id %s not found", productId)));

        var productDimensionEntity = toEntity(productDimension);

        //should check if productEntity already has a product dimension
        if (productEntity.getDimensions() != null ) {
            throw new RuntimeException("Product already has a product dimension");
        }
        productEntity.setDimensions(productDimensionEntity);
        productDimensionEntity.setProduct(productEntity);

        productDimensionRepository.save(productDimensionEntity);
    }

    @Override
    public void update(Long productId, ProductDimensions productDimension) {
        var productDimensionEntity = productDimensionRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException(String.format("Product id %s not found", productId)));

        if (productDimension.getHeight() != null) {
            productDimensionEntity.setHeight(productDimension.getHeight());
        }

        if(productDimension.getDepth() != null) {
            productDimensionEntity.setDepth(productDimension.getDepth());
        }

        if(productDimension.getWidth() != null) {
            productDimensionEntity.setWidth(productDimension.getWidth());
        }

        productDimensionRepository.save(productDimensionEntity);
    }
}
