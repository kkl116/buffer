package com.gubu.buffer.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductCost {
    private Long id;
    private String name;
    private Double price;
}
