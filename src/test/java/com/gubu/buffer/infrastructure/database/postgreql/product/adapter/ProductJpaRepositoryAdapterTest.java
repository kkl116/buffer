package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.gubu.buffer.common.Common.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductJpaRepositoryAdapterTest {

    private static ProductRepository productRepository;
    private ProductJpaRepositoryAdapter productJpaRepository;

    @BeforeEach
    void setup() {
        productRepository = Mockito.mock(ProductRepository.class);
        productJpaRepository = new ProductJpaRepositoryAdapter(productRepository);
    }

    @Test
    void findAll() {
        //Given
        when(productRepository.findAll()).thenReturn(List.of(productEntity1(), productEntity2()));

        //When
        var productEntities = productJpaRepository.findAll();

        //Then
        verify(productRepository, times(1)).findAll();
        assertEquals(2, productEntities.size());
        assertEquals("product 1", productEntities.getFirst().getName());
        assertEquals("product 2", productEntities.get(1).getName());
    }

    @Test
    void save() {
        //given
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity1());

        //When
        productJpaRepository.save(product());

        //Then
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void deleteById() {
        //When
        productJpaRepository.deleteById(1L);

        //Then
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void findById() {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity1()));

        //When
        productJpaRepository.findById(1L);

        //Then
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void deleteAll() {
        //When
        productJpaRepository.deleteAll();

        //Then
        verify(productRepository, times(1)).deleteAll();
    }

    @Test
    void updateProduct() {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity1()));

        var captor = ArgumentCaptor.forClass(ProductEntity.class);

        //When
        productJpaRepository.updateProduct(1L, productFromRequestDto());

        //Then
        verify(productRepository, times(1)).save(captor.capture());
        var capturedProduct = captor.getValue();
        assertEquals("product 2", capturedProduct.getName());
    }
}