package com.gubu.buffer.domain.product;

import lombok.Getter;

import java.util.List;

@Getter
public enum ProductField {
    ID("id", List.of()),
    NAME("name", List.of()),
    DESCRIPTION("description", List.of()),
    PRICE("price", List.of()),
    DIMENSIONS("dimensions", List.of()),
    COSTS("costs", List.of()),
    PROFIT("profit", List.of(PRICE, COSTS)),
    TOTAL_COST("total_cost", List.of(COSTS)),
    PROFIT_MARGIN("profit_margin", List.of(PRICE, COSTS));

    private final String value;
    private final List<ProductField> requisiteFields;

    ProductField(String value, List<ProductField> requisiteFields) {
        this.value = value;
        this.requisiteFields = requisiteFields;
    }

    public static ProductField fromString(String fieldName) {
        for (ProductField field : values()) {
            if (field.name().equalsIgnoreCase(fieldName)) {
                return field;
            }
        }
        throw new IllegalArgumentException("Invalid field: " + fieldName);
    }

    public boolean isCalculatedField() {
        return !this.requisiteFields.isEmpty();
    }
}
