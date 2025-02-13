package com.gubu.buffer.domain.product;

import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.application.dto.request.ProductDimensionRequestDto;
import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.model.ProductDimensions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private static ProductRepositoryAdapter productRepositoryAdapter;
    private static ProductCostRepositoryAdapter productCostRepositoryAdapter;
    private static ProductDimensionRepositoryAdapter productDimensionRepositoryAdapter;
    private ProductService productService;

    @BeforeEach
    void setup() {
        productRepositoryAdapter = Mockito.mock(ProductRepositoryAdapter.class);
        productCostRepositoryAdapter = Mockito.mock(ProductCostRepositoryAdapter.class);
        productDimensionRepositoryAdapter = Mockito.mock(ProductDimensionRepositoryAdapter.class);
        productService = new ProductService(
            productRepositoryAdapter,
            productCostRepositoryAdapter,
            productDimensionRepositoryAdapter
        );
    }

    @Test
    void shouldAddProduct() {
        //When
        productService.addProduct(new ProductRequestDto("dummy", null, null));
        //Then
        verify(productRepositoryAdapter, times(1)).save(any(Product.class));
    }

    @Test
    void shouldDeleteProduct() {
        //When
        productService.deleteProduct(1L);

        //Then
        verify(productRepositoryAdapter, times(1)).deleteById(1L);
    }

    @Test
    void shouldGetAllProducts() {
        //When
        productService.getAllProducts(Set.of());

        //Then
        verify(productRepositoryAdapter, times(1)).findAll(Set.of());
    }

    @Test
    void shouldGetProductById() {
        //When
        productService.getProductById(1L, Set.of());

        //Then
        verify(productRepositoryAdapter, times(1)).findById(1L, Set.of());
    }

    @Test
    void shouldUpdateProduct() {
        //When
        var productRequestDto = new ProductRequestDto(null, "Description 2", null);
        productService.updateProduct(1L, productRequestDto);

        //Then
        verify(productRepositoryAdapter, times(1)).updateProduct(any(Long.class), any(Product.class));
    }

    @Test
    void shouldAddProductCost() {
        //When
        var productCostRequestDto = new ProductCostRequestDto("dummy cost", 100.0);
        productService.addProductCost(1L, productCostRequestDto);

        //Then
        verify(productCostRepositoryAdapter, times(1)).save(any(Long.class), any(ProductCost.class));
    }

    @Test
    void shouldUpdateProductCost() {
        //Given
        var productCostRequestDto = new ProductCostRequestDto("dummy cost", 100.0);

        //When
        productService.updateProductCost(1L, productCostRequestDto);

        //Then
        verify(productCostRepositoryAdapter, times(1)).update(anyLong(), any(ProductCost.class));
    }

    @Test
    void shouldDeleteProductCost() {
        //When
        productService.deleteProductCost(1L);

        //Then
        verify(productCostRepositoryAdapter, times(1)).deleteById(any(Long.class));
    }

    @Test
    void shouldUpdateProductDimesnion() {
        //Given
        var productDimensionRequestDto = new ProductDimensionRequestDto(0.00, null, null);

        //When
        productService.updateProductDimension(1L, productDimensionRequestDto);

        //Then
        verify(productDimensionRepositoryAdapter, times(1))
            .update(anyLong(), any(ProductDimensions.class));
    }
}