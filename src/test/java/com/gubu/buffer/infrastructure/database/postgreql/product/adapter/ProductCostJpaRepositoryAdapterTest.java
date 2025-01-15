package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductCostRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static com.gubu.buffer.common.Common.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductCostJpaRepositoryAdapterTest {

    private ProductRepository productRepository;
    private ProductCostRepository productCostRepository;
    private ProductCostJpaRepositoryAdapter productCostJpaRepository;

    @BeforeEach
    void setup() {
        productRepository = Mockito.mock(ProductRepository.class);
        productCostRepository = Mockito.mock(ProductCostRepository.class);
        productCostJpaRepository = new ProductCostJpaRepositoryAdapter(productRepository, productCostRepository);
    }

    @Test
    void findById() {
        //Given
        when(productCostRepository.findById(1L)).thenReturn(Optional.of(productCostEntity1()));

        //When
        var productCost = productCostJpaRepository.findById(1L).get();

        //Then
        verify(productCostRepository, times(1)).findById(1L);
        assertEquals(1L, productCost.getId());
        assertEquals("product cost 1", productCost.getName());
        assertEquals(100.0, productCost.getPrice());
    }

    @Test
    void save() {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity1()));

        var captor = ArgumentCaptor.forClass(ProductCostEntity.class);
        //When
        productCostJpaRepository.save(1L, productCost());

        //Then
        verify(productRepository, times(1)).findById(1L);
        verify(productCostRepository, times(1)).save(captor.capture());
        var capturedProductCost = captor.getValue();
        assertEquals(1L, capturedProductCost.getProduct().getId());
        assertEquals("product cost 1", capturedProductCost.getName());
        assertEquals(100.0, capturedProductCost.getPrice());
        assertEquals(1L, capturedProductCost.getProduct().getId());
    }

    @Test
    void deleteById() {
        //Given
        when(productCostRepository.findById(any(Long.class)))
            .thenReturn(Optional.of(productCostEntity1()));

        //When
        productCostJpaRepository.deleteById(1L);

        //Then
        verify(productCostRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAll() {
        //When
        productCostJpaRepository.deleteAll();

        //Then
        verify(productCostRepository, times(1)).deleteAll();
    }

    @Test
    void update() {
        //Given
        when(productCostRepository.findById(1L)).thenReturn(Optional.of(productCostEntity1()));

        var captor = ArgumentCaptor.forClass(ProductCostEntity.class);

        //When
        productCostJpaRepository.update(1L, productCostFromRequestDto());

        //Then
        verify(productCostRepository, times(1)).save(captor.capture());
        var capturedProductCost = captor.getValue();
        assertEquals("new cost", capturedProductCost.getName());
        assertEquals(69.00, capturedProductCost.getPrice());
    }
}