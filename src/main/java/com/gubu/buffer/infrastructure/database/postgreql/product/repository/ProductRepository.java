package com.gubu.buffer.infrastructure.database.postgreql.product.repository;

import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {}
