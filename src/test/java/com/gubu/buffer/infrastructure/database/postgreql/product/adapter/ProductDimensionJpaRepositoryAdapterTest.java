package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductDimensionRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.gubu.buffer.common.Common.productDimensionFromRequestDto;
import static com.gubu.buffer.common.Common.productEntity1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductDimensionJpaRepositoryAdapterTest {

    private ProductRepository productRepository;
    private ProductDimensionRepository productDimensionRepository;
    private ProductDimensionJpaRepositoryAdapter productDimensionJpaRepository;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productDimensionRepository = Mockito.mock(ProductDimensionRepository.class);
        productDimensionJpaRepository = new ProductDimensionJpaRepositoryAdapter(
            productRepository,
            productDimensionRepository
        );
    }

    @Test
    void save() {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity1()));

        //When
        productDimensionJpaRepository.save(1L, productDimensionFromRequestDto());

        //Then
        verify(productRepository, times(1)).findById(1L);
        verify(productDimensionRepository, times(1)).save(any(ProductDimensionEntity.class));
    }
}