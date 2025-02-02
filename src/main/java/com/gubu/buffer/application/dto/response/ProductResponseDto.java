package com.gubu.buffer.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponseDto(
    Long id,
    String name,
    String description,
    Double price,
    List<ProductCostResponseDto> costs,
    ProductDimensionsResponseDto dimensions,
    Double profit,
    Double totalCost,
    Double profitMargin
) {}
