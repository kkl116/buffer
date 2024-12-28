package com.gubu.buffer.domain.product;

import com.gubu.buffer.infrastructure.database.postgreql.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {}
