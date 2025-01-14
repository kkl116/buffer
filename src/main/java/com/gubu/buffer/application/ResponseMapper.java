package com.gubu.buffer.application;

import com.gubu.buffer.application.dto.response.ProductCostResponseDto;
import com.gubu.buffer.application.dto.response.ProductResponseDto;
import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;

public class ResponseMapper {

    private ResponseMapper() {}

    public static ProductResponseDto toResponse(Product product) {
        return ProductResponseDto.builder()
            .id(product.getId())
            .name(product.getName())
            .build();
    }

    public static ProductCostResponseDto toResponse(ProductCost productCost) {
        return ProductCostResponseDto.builder()
            .id(productCost.getId())
            .build();
    }
}
