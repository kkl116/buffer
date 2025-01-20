package com.gubu.buffer.application.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductResponseDto(
    String name,
    Long id,
    List<ProductCostResponseDto> costs,
    ProductDimensionsResponseDto dimensions
) {}
