package com.gubu.buffer.infrastructure.database.postgreql.product.cost;

import com.gubu.buffer.infrastructure.database.postgreql.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_costs")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductCostEntity {

    public ProductCostEntity() { /*For JPA */ }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
