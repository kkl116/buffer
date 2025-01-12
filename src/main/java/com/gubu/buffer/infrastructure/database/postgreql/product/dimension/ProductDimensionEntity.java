package com.gubu.buffer.infrastructure.database.postgreql.product.dimension;

import com.gubu.buffer.infrastructure.database.postgreql.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_dimensions")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductDimensionEntity {

    public ProductDimensionEntity() { /*For JPA */ }

    @Id
    private Long id;

    private Double height;

    private Double width;

    private Double depth;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private ProductEntity product;
}
