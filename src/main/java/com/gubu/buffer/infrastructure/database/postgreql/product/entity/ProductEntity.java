package com.gubu.buffer.infrastructure.database.postgreql.product.entity;

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

    public ProductEntity() { /*For JPA */ }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @Setter
    @Column
    private String description;

    @Setter
    @Column(nullable = false)
    private Double price;

    @Builder.Default
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "product",
        orphanRemoval = true,
        cascade = CascadeType.ALL
    )
    private List<ProductCostEntity> costs = new ArrayList<>();

    @OneToOne(
        mappedBy = "product",
        orphanRemoval = true,
        cascade = CascadeType.ALL
    )
    @PrimaryKeyJoinColumn
    private ProductDimensionsEntity dimensions;

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

    @PrePersist
    public void prePersist() {
        if (this.price == null) {
            this.price = 0.00;
        }
    }
}
