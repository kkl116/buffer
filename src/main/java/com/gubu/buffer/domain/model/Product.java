package com.gubu.buffer.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Product {

    private Long id;
    private String name;

    public Product(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
