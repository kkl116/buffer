package com.gubu.buffer.infrastructure.database.postgreql.product;

import com.gubu.buffer.infrastructure.database.postgreql.product.cost.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.dimension.ProductDimensionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @Builder.Default
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "product",
        orphanRemoval = true,
        cascade = CascadeType.ALL
    )
    private List<ProductCostEntity> productCosts = new ArrayList<>();

    @OneToOne(
        mappedBy = "product",
        orphanRemoval = true,
        cascade = CascadeType.ALL
    )
    private ProductDimensionEntity productDimension;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass =
            o instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer()
                .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass =
            this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer()
                .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        ProductEntity that = (ProductEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer()
            .getPersistentClass()
            .hashCode() : getClass().hashCode();
    }
}
