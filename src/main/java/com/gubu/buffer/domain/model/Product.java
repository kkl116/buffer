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
    private String description;
    private Double price;
    private List<ProductCost> costs;
    private ProductDimensions dimensions;

    public Double getTotalCost() {
        return this.costs.stream().mapToDouble(ProductCost::getPrice).sum();
    }

    public Double getProfit() {
        return this.price - this.getTotalCost();
    }

    public Double getProfitMargin() {
        if (this.price.equals(0.0)) {
            return 0.0;
        }
        return this.getProfit() / this.price;
    }
}
