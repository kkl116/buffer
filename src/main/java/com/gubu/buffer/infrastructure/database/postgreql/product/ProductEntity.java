package com.gubu.buffer.infrastructure.database.postgreql.product;

import com.gubu.buffer.infrastructure.database.postgreql.product.cost.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.dimension.ProductDimensionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductEntity {

    protected ProductEntity() { /*For JPA */ }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "product",
        orphanRemoval = true,
        cascade = CascadeType.ALL
    )
    private List<ProductCostEntity> productCosts;

    @OneToOne(
        fetch = FetchType.LAZY,
        mappedBy = "product",
        orphanRemoval = true,
        cascade = CascadeType.ALL
    )
    private ProductDimensionEntity productDimension;
}
