package com.gubu.buffer.infrastructure.database.postgreql.product.repository;

import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDimensionRepository extends JpaRepository<ProductDimensionsEntity, Long> {}

