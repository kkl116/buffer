package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductDimensionRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static com.gubu.buffer.common.Common.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void shouldThrowExceptionIfProductAlreadyHasDimension() {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity2()));

        //When Then
        assertThrows(RuntimeException.class,
            () -> productDimensionJpaRepository.save(1L, productDimensionFromRequestDto()));


    }

    @Test
    void update() {
        //Given
        when(productDimensionRepository.findById(1L)).thenReturn(Optional.of(productDimensionEntity1()));

        var captor = ArgumentCaptor.forClass(ProductDimensionEntity.class);

        //When
        productDimensionJpaRepository.update(1L, productDimensionFromRequestDto());

        //Then
        verify(productDimensionRepository, times(1)).save(captor.capture());
        assertEquals(1.00, captor.getValue().getHeight());
        assertEquals(0.00, captor.getValue().getWidth());
        assertEquals(0.00, captor.getValue().getDepth());
    }
}