package com.gubu.buffer.common;

import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.model.ProductDimension;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionEntity;
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
            .productCosts(new ArrayList<>())
            .build();
    }

    public static ProductEntity productEntity2() {
        return ProductEntity.builder()
            .id(2L)
            .name("product 2")
            .productCosts(new ArrayList<>())
            .productDimension(new ProductDimensionEntity())
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

    public static ProductDimensionEntity productDimensionEntity1() {
        return ProductDimensionEntity.builder()
            .id(1L)
            .height(0.0)
            .width(0.0)
            .depth(0.0)
            .build();
    }

    public static ProductDimension productDimensionFromRequestDto() {
        return ProductDimension.builder()
            .height(1.00)
            .width(null)
            .depth(null)
            .build();
    }
}
