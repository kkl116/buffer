package com.gubu.buffer.infrastructure.database.postgreql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity(name = "Product")
@Table(name = "product_table")
@Builder
@AllArgsConstructor
public class ProductEntity {

    public ProductEntity() { /*For JPA */ }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;
}
