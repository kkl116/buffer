package com.gubu.buffer.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCost {
    private Long id;
    private String name;
    private Double price;
}
