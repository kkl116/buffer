package com.gubu.buffer.common;

import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.model.ProductDimensions;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionsEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductEntity;

import java.util.ArrayList;

public class Common {

    private Common() {}

    //Domain
    public static Product product() {
        return Product.builder()
            .id(1L)
            .name("product 1")
            .costs(new ArrayList<>())
            .build();
    }

    public static Product productFromRequestDto() {
        return Product.builder()
            .id(2L)
            .name("product 2")
            .build();
    }

    public static ProductCost productCost() {
        return ProductCost.builder()
            .id(1L)
            .name("product cost 1")
            .price(100.0)
            .build();
    }

    public static ProductCost productCostFromRequestDto() {
        return ProductCost.builder()
            .id(1L)
            .name("new cost")
            .price(69.00)
            .build();
    }

    //Entities
    public static ProductEntity productEntity1() {
        return ProductEntity.builder()
            .id(1L)
            .name("product 1")
            .costs(new ArrayList<>())
            .dimensions(new ProductDimensionsEntity(1L, 1.00, 1.00, 1.00, new ProductEntity()))
            .build();
    }

    public static ProductEntity productEntity2() {
        return ProductEntity.builder()
            .id(2L)
            .name("product 2")
            .costs(new ArrayList<>())
            .dimensions(new ProductDimensionsEntity(2L, 1.00, 1.00, 1.00, new ProductEntity()))
            .build();
    }

    public static ProductEntity productEntityFromRequestDto() {
        return ProductEntity.builder()
            .id(1L)
            .name("product 1")
            .costs(new ArrayList<>())
            .build();
    }

    public static ProductCostEntity productCostEntity1() {
        return ProductCostEntity.builder()
            .id(1L)
            .name("product cost 1")
            .price(100.0)
            .product(productEntity1())
            .build();
    }

    public static ProductDimensionsEntity productDimensionEntity1() {
        return ProductDimensionsEntity.builder()
            .id(1L)
            .height(0.0)
            .width(0.0)
            .depth(0.0)
            .build();
    }

    public static ProductDimensions productDimensionFromRequestDto() {
        return ProductDimensions.builder()
            .height(1.00)
            .width(null)
            .depth(null)
            .build();
    }
}
