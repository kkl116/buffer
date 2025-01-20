package com.gubu.buffer.application;

import com.gubu.buffer.application.dto.response.ProductCostResponseDto;
import com.gubu.buffer.application.dto.response.ProductDimensionsResponseDto;
import com.gubu.buffer.application.dto.response.ProductResponseDto;
import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.model.ProductDimensions;

public class ResponseMapper {

    private ResponseMapper() {}

    public static ProductResponseDto toResponse(Product product) {
        return ProductResponseDto.builder()
            .id(product.getId())
            .name(product.getName())
            .costs(product.getCosts().stream().map(ResponseMapper::toResponse).toList())
            .dimensions(ResponseMapper.toResponse(product.getDimensions()))
            .build();
    }

    public static ProductCostResponseDto toResponse(ProductCost productCost) {
        return ProductCostResponseDto.builder()
            .id(productCost.getId())
            .name(productCost.getName())
            .price(productCost.getPrice())
            .build();
    }

    public static ProductDimensionsResponseDto toResponse(ProductDimensions productDimensions) {
        return ProductDimensionsResponseDto.builder()
            .height(productDimensions.getHeight())
            .width(productDimensions.getWidth())
            .depth(productDimensions.getDepth())
            .build();
    }
}
