package com.gubu.buffer.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Product {
    private Long id;
    private String name;
    private List<ProductCost> productCosts;
}
