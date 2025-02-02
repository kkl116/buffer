package com.gubu.buffer.application;

import com.gubu.buffer.application.dto.response.ProductCostResponseDto;
import com.gubu.buffer.application.dto.response.ProductDimensionsResponseDto;
import com.gubu.buffer.application.dto.response.ProductResponseDto;
import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.model.ProductDimensions;
import com.gubu.buffer.domain.product.ProductField;

import java.util.Set;

public class ResponseMapper {

    private ResponseMapper() {}

    public static ProductResponseDto toResponse(Product product, Set<ProductField> requestedFields) {

        if (requestedFields.isEmpty()) {
            return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .costs(product.getCosts().stream().map(ResponseMapper::toResponse).toList())
                .dimensions(toResponse(product.getDimensions()))
                .profit(product.getProfit())
                .profitMargin(product.getProfitMargin())
                .totalCost(product.getTotalCost())
                .build();
        }

        return requestedFields.stream()
            .reduce(ProductResponseDto.builder(),
                (builder, field) -> updateProductResponseDtoBuilder(product, field, builder),
                (builder1, builder2) -> builder2 //dummy combiner
            )
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

    private static ProductResponseDto.ProductResponseDtoBuilder updateProductResponseDtoBuilder(
        Product product,
        ProductField field,
        ProductResponseDto.ProductResponseDtoBuilder productResponseDtoBuilder
    ) {
        return switch (field) {
            case ID -> productResponseDtoBuilder.id(product.getId());
            case NAME -> productResponseDtoBuilder.name(product.getName());
            case DESCRIPTION -> productResponseDtoBuilder.description(product.getDescription());
            case PRICE -> productResponseDtoBuilder.price(product.getPrice());
            case DIMENSIONS -> productResponseDtoBuilder.dimensions(toResponse(product.getDimensions()));
            case COSTS ->
                productResponseDtoBuilder.costs(product.getCosts().stream().map(ResponseMapper::toResponse).toList());
            case PROFIT -> productResponseDtoBuilder.profit(product.getProfit());
            case TOTAL_COST -> productResponseDtoBuilder.totalCost(product.getTotalCost());
            case PROFIT_MARGIN -> productResponseDtoBuilder.profitMargin(product.getProfitMargin());
        };
    }
}
