package com.gubu.buffer.application;

import com.gubu.buffer.application.dto.response.ProductCostResponseDto;
import com.gubu.buffer.application.dto.response.ProductDimensionsResponseDto;
import com.gubu.buffer.application.dto.response.ProductResponseDto;
import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.model.ProductDimensions;

import java.util.List;
import java.util.Optional;

public class ResponseMapper {

    private ResponseMapper() {}

    public static ProductResponseDto toResponse(Product product) {

        //fields can be nullable depending on which were requested, so just provide defaults
        List<ProductCostResponseDto> costDtos = Optional.ofNullable(product.getCosts())
            .map(costs -> costs.stream().map(ResponseMapper::toResponse).toList())
            .orElse(null);

        ProductDimensionsResponseDto dimensionsDto = Optional.ofNullable(product.getDimensions())
            .map(ResponseMapper::toResponse)
            .orElse(null);

        return ProductResponseDto.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .costs(costDtos)
            .dimensions(dimensionsDto)
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
