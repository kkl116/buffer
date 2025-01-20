package com.gubu.buffer.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDimensions {
    private Double height;
    private Double width;
    private Double depth;
}
