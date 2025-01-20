package com.gubu.buffer.application.dto.response;

import lombok.Builder;

@Builder
public record ProductDimensionsResponseDto(
    Double height,
    Double width,
    Double depth
) {
}
