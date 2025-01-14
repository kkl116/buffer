package com.gubu.buffer.infrastructure.database.postgreql.product.repository;

import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDimensionRepository extends JpaRepository<ProductDimensionEntity, Long> {}

