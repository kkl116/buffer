package com.gubu.buffer.infrastructure.database.postgreql;

import com.gubu.buffer.domain.model.ProductRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity(name = "Product")
@Table(name = "product_table")
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
}
