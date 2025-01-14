package com.gubu.buffer.infrastructure.database.postgreql.product.repository;

import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductCostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCostRepository extends JpaRepository<ProductCostEntity, Long> {}
