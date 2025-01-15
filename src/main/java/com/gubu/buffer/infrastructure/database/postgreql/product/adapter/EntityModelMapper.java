package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.model.ProductDimension;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductEntity;

public class EntityModelMapper {

    private EntityModelMapper() {}

    protected static ProductEntity toEntity(Product product) {
        //should only update things that come in from request dto
        return ProductEntity.builder()
            .name(product.getName())
            .build();
    }

    protected static ProductCostEntity toEntity(ProductCost productCost) {
        //should only update things that come in from request dto
        return ProductCostEntity.builder()
            .name(productCost.getName())
            .price(productCost.getPrice())
            .build();
    }

    protected static ProductDimensionEntity toEntity(ProductDimension productDimension) {
        return ProductDimensionEntity.builder()
            .height(productDimension.getHeight())
            .width(productDimension.getWidth())
            .depth(productDimension.getDepth())
            .build();
    }

    protected static Product toModel(ProductEntity productEntity) {
        return Product.builder()
            .id(productEntity.getId())
            .name(productEntity.getName())
            .costs(productEntity.getProductCosts().stream().map(EntityModelMapper::toModel).toList())
            .build();
    }

    protected static ProductCost toModel(ProductCostEntity productCostEntity) {
        return ProductCost.builder()
            .id(productCostEntity.getId())
            .name(productCostEntity.getName())
            .price(productCostEntity.getPrice())
            .build();
    }
}
