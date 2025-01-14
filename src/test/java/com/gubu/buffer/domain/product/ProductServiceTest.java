package com.gubu.buffer.domain.product;

import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.infrastructure.database.postgreql.product.ProductEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.ProductRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.cost.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.cost.ProductCostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;
    private ProductCostRepository productCostRepository;

    @BeforeEach
    void setup() {
        productRepository = Mockito.mock(ProductRepository.class);
        productCostRepository = Mockito.mock(ProductCostRepository.class);
        productService = new ProductService(productRepository, productCostRepository);
    }

    @Test
    void shouldAddProduct() {
        //Given
        when(productRepository.save(any(ProductEntity.class)))
            .thenReturn(ProductEntity.builder().id(1L).name("dummy").build());

        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        ProductRequestDto productRequestDto = new ProductRequestDto("dummy");

        //When
        productService.addProduct(productRequestDto);

        //Then
        Mockito.verify(productRepository).save(captor.capture());
        ProductEntity capturedProduct = captor.getValue();
        assert capturedProduct.getName().equals("dummy");
    }

    @Test
    void shouldDeleteProduct() {
        //Given
        long productId = 1L;

        //When
        productService.deleteProduct(productId);

        //Then
        Mockito.verify(productRepository).deleteById(productId);
    }

    @Test
    void shouldGetAllProducts() {
        //Given
        ProductEntity product1 = ProductEntity.builder().id(1L).name("dummy1").build();
        ProductEntity product2 = ProductEntity.builder().id(2L).name("dummy2").build();
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        //When
        List<ProductEntity> productEntities = productService.getAllProducts();

        //Then
        assertEquals(2, productEntities.size());
        assertEquals(1, productEntities.getFirst().getId());
        assertEquals("dummy1", productEntities.getFirst().getName());
        assertEquals(2, productEntities.get(1).getId());
        assertEquals("dummy2", productEntities.get(1).getName());
    }

    @Test
    void shouldGetProductById() {
        //Given
        Long productId = 1L;
        ProductEntity savedEntity = ProductEntity.builder().id(productId).name("dummy").build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(savedEntity));

        //When
        Optional<ProductEntity> productEntity = productService.getProductById(productId);

        //Then
        assertTrue(productEntity.isPresent());
        assertEquals(productId, productEntity.get().getId());
        assertEquals("dummy", productEntity.get().getName());
    }

    @Test
    void shouldReturnEmptyOptionalIfProductNotFound() {
        //Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //When & Then
        Optional<ProductEntity> productEntity = productService.getProductById(productId);
        assertTrue(productEntity.isEmpty());
    }

    @Test
    void shouldUpdateProduct() {
        //Given
        Long productId = 1L;
        ProductEntity entity = ProductEntity.builder().id(productId).name("old").build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(entity));

        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        ProductRequestDto productRequestDto = new ProductRequestDto("new");

        //When
        productService.updateProduct(productId, productRequestDto);

        //Then
        Mockito.verify(productRepository).save(captor.capture());
        ProductEntity capturedProduct = captor.getValue();
        assertEquals("new", capturedProduct.getName());
    }

    @Test
    void shouldAddProductCost() {
        //Given
        Long productId = 1L;
        ProductEntity savedEntity = ProductEntity.builder()
            .id(productId)
            .name("dummy")
            .productCosts(new ArrayList<>())
            .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(savedEntity));

        ArgumentCaptor<ProductCostEntity> captor = ArgumentCaptor.forClass(ProductCostEntity.class);
        ProductCostRequestDto productCostRequestDto = new ProductCostRequestDto("dummy cost", 100.0);

        //When
        productService.addProductCost(productId, productCostRequestDto);

        //Then
        Mockito.verify(productCostRepository).save(captor.capture());
        ProductCostEntity capturedProduct = captor.getValue();
        assertEquals(1L, capturedProduct.getProduct().getId());
        assertEquals("dummy cost", capturedProduct.getName());
        assertEquals(100.0, capturedProduct.getPrice());
    }

    @Test
    void shouldUpdateProductCost() {
        //Given
        Long productCostId = 1L;
        ProductCostEntity savedEntity = ProductCostEntity.builder()
           .id(productCostId)
           .name("dummy")
           .build();

        when(productCostRepository.findById(productCostId)).thenReturn(Optional.of(savedEntity));

        ArgumentCaptor<ProductCostEntity> captor = ArgumentCaptor.forClass(ProductCostEntity.class);
        ProductCostRequestDto productCostRequestDto = new ProductCostRequestDto("new cost", 200.0);

        //When
        productService.updateProductCost(productCostId, productCostRequestDto);

        //Then
        Mockito.verify(productCostRepository).save(captor.capture());
        ProductCostEntity capturedProductCost = captor.getValue();
        assertEquals("new cost", capturedProductCost.getName());
        assertEquals(200.0, capturedProductCost.getPrice());
    }
}