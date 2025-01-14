package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductEntity;

public class EntityModelMapper {

    private EntityModelMapper() {}

    protected static ProductEntity toEntity(Product product) {
        return ProductEntity.builder()
            .id(product.getId())
            .name(product.getName())
            .productCosts(product.getProductCosts().stream().map(EntityModelMapper::toEntity).toList())
            .build();
    }

    protected static ProductCostEntity toEntity(ProductCost productCost) {
        return ProductCostEntity.builder()
            .id(productCost.getId())
            .name(productCost.getName())
            .price(productCost.getPrice())
            .build();
    }

    protected static Product toModel(ProductEntity productEntity) {
        return Product.builder()
            .id(productEntity.getId())
            .name(productEntity.getName())
            .productCosts(productEntity.getProductCosts().stream().map(EntityModelMapper::toModel).toList())
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
