package com.gubu.buffer.domain.product;

import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.domain.model.ProductRecord;
import com.gubu.buffer.infrastructure.database.postgreql.ProductEntity;
import com.gubu.buffer.infrastructure.database.postgreql.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceTest {

    private static ProductRepository productRepository;
    private static ProductService productService;

    @BeforeAll
    static void setup() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void shouldSaveProduct() {
        //Given
        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        ProductRequestDto productRequestDto = new ProductRequestDto("dummy");

        //When
        productService.saveProduct(productRequestDto);

        //Then
        Mockito.verify(productRepository).save(captor.capture());
        ProductEntity capturedProductEntity = captor.getValue();
        assert capturedProductEntity.getName().equals("dummy");
    }

    @Test
    void shouldDeleteProduct() {
        //Given
        Long productId = 1L;

        //When
        productService.deleteProduct(productId);

        //Then
        Mockito.verify(productRepository).deleteById(productId);
    }

    @Test
    void shouldGetAllProducts() {
        //Given
        ProductEntity productEntity1 = ProductEntity.builder().id(1L).name("dummy1").build();
        ProductEntity productEntity2 = ProductEntity.builder().id(2L).name("dummy2").build();
        Mockito.when(productRepository.findAll()).thenReturn(List.of(productEntity1, productEntity2));

        //When
        List<ProductRecord> productRecords = productService.getAllProducts();

        //Then
        assertEquals(2, productRecords.size());
        assertEquals(1, productRecords.getFirst().id());
        assertEquals("dummy1", productRecords.getFirst().name());
        assertEquals(2, productRecords.get(1).id());
        assertEquals("dummy2", productRecords.get(1).name());
    }
}