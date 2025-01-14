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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private static final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private static final ProductCostRepository productCostRepository = Mockito.mock(ProductCostRepository.class);
    private ProductService productService;

    @BeforeEach
    void setup() {
        productService = new ProductService(productRepository, productCostRepository);

        //clear mock interactions
        Mockito.reset(productRepository);
        Mockito.reset(productCostRepository);
    }

    @Test
    void shouldAddProduct() {
        //Given
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity1());

        var captor = ArgumentCaptor.forClass(ProductEntity.class);
        var productRequestDto = new ProductRequestDto("dummy");

        //When
        productService.addProduct(productRequestDto);

        //Then
        verify(productRepository, times(1)).save(captor.capture());
        var capturedProduct = captor.getValue();
        assert capturedProduct.getName().equals("dummy");
    }

    @Test
    void shouldDeleteProduct() {
        //When
        productService.deleteProduct(1L);

        //Then
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldGetAllProducts() {
        //Given
        when(productRepository.findAll()).thenReturn(List.of(productEntity1(), productEntity2()));

        //When
        var productEntities = productService.getAllProducts();

        //Then
        assertEquals(2, productEntities.size());
        assertEquals(1, productEntities.getFirst().getId());
        assertEquals("product 1", productEntities.getFirst().getName());
        assertEquals(2, productEntities.get(1).getId());
        assertEquals("product 2", productEntities.get(1).getName());
    }

    @Test
    void shouldGetProductById() {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity1()));

        //When
        var productEntity = productService.getProductById(1L);

        //Then
        assertTrue(productEntity.isPresent());
        assertEquals(1L, productEntity.get().getId());
        assertEquals("product 1", productEntity.get().getName());
    }

    @Test
    void shouldReturnEmptyOptionalIfProductNotFound() {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        //When & Then
        var productEntity = productService.getProductById(1L);
        assertTrue(productEntity.isEmpty());
    }

    @Test
    void shouldUpdateProduct() {
        //Given
        //create a different product - or else pollutes the other tests
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity1()));

        var captor = ArgumentCaptor.forClass(ProductEntity.class);
        var productRequestDto = new ProductRequestDto("new");

        //When
        productService.updateProduct(1L, productRequestDto);

        //Then
        verify(productRepository, times(1)).save(captor.capture());
        var capturedProduct = captor.getValue();
        assertEquals("new", capturedProduct.getName());
    }

    @Test
    void shouldAddProductCost() {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity1()));

        var captor = ArgumentCaptor.forClass(ProductCostEntity.class);
        var productCostRequestDto = new ProductCostRequestDto("dummy cost", 100.0);

        //When
        productService.addProductCost(1L, productCostRequestDto);

        //Then
        verify(productCostRepository, times(1)).save(captor.capture());
        var capturedProduct = captor.getValue();
        assertEquals(1L, capturedProduct.getProduct().getId());
        assertEquals("dummy cost", capturedProduct.getName());
        assertEquals(100.0, capturedProduct.getPrice());
    }

    @Test
    void shouldUpdateProductCost() {
        //Given
        when(productCostRepository.findById(1L)).thenReturn(Optional.of(productCostEntity1()));

        var captor = ArgumentCaptor.forClass(ProductCostEntity.class);
        var productCostRequestDto = new ProductCostRequestDto("new cost", 200.0);

        //When
        productService.updateProductCost(1L, productCostRequestDto);

        //Then
        verify(productCostRepository, times(1)).save(captor.capture());
        var capturedProductCost = captor.getValue();
        assertEquals("new cost", capturedProductCost.getName());
        assertEquals(200.0, capturedProductCost.getPrice());
    }

    @Test
    void shouldDeleteProductCost() {
        //Given
        var productCostId = 1L;
        when(productCostRepository.findById(productCostId)).thenReturn(Optional.of(productCostEntity1()));

        //When
        productService.deleteProductCost(productCostId);

        //Then
        verify(productCostRepository, times(1)).deleteById(productCostId);
    }

    private ProductEntity productEntity1() {
        return ProductEntity.builder()
            .id(1L)
            .name("product 1")
            .build();
    }

    private ProductEntity productEntity2() {
        return ProductEntity.builder()
            .id(2L)
            .name("product 2")
            .build();
    }

    private ProductCostEntity productCostEntity1() {
        return ProductCostEntity.builder()
            .id(1L)
            .name("product cost 1")
            .product(productEntity1())
            .build();
    }
}