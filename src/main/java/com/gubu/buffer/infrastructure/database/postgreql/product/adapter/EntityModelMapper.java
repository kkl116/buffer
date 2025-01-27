package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.model.ProductDimensions;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionsEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductEntity;

public class EntityModelMapper {

    private EntityModelMapper() {}

    protected static ProductEntity toEntity(Product product) {
        //should only update things that come in from request dto
        return ProductEntity.builder()
            .name(product.getName())
            .description(product.getDescription())
            .build();
    }

    protected static ProductCostEntity toEntity(ProductCost productCost) {
        //should only update things that come in from request dto
        return ProductCostEntity.builder()
            .name(productCost.getName())
            .price(productCost.getPrice())
            .build();
    }

    protected static ProductDimensionsEntity toEntity(ProductDimensions productDimensions) {
        return ProductDimensionsEntity.builder()
            .height(productDimensions.getHeight())
            .width(productDimensions.getWidth())
            .depth(productDimensions.getDepth())
            .build();
    }

    protected static Product toModel(ProductEntity productEntity) {
        return Product.builder()
            .id(productEntity.getId())
            .name(productEntity.getName())
            .description(productEntity.getDescription())
            .costs(productEntity.getCosts().stream().map(EntityModelMapper::toModel).toList())
            .dimensions(toModel(productEntity.getDimensions()))
            .build();
    }

    protected static ProductCost toModel(ProductCostEntity productCostEntity) {
        return ProductCost.builder()
            .id(productCostEntity.getId())
            .name(productCostEntity.getName())
            .price(productCostEntity.getPrice())
            .build();
    }

    protected static ProductDimensions toModel(ProductDimensionsEntity productDimensionsEntity) {
        return ProductDimensions.builder()
            .height(productDimensionsEntity.getHeight())
            .width(productDimensionsEntity.getWidth())
            .depth(productDimensionsEntity.getDepth())
            .build();
    }
}
