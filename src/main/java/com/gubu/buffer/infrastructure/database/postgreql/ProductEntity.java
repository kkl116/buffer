package com.gubu.buffer.infrastructure.database.postgreql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity(name = "Product")
@Table(name = "product_table")
@Builder
@AllArgsConstructor
public class ProductEntity {

    public ProductEntity() { /*For JPA */ }

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;
}
