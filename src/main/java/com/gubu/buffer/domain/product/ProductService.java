package com.gubu.buffer.domain.product;

import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.application.dto.request.ProductDimensionRequestDto;
import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.model.ProductDimension;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    private final ProductRepositoryAdapter productRepositoryAdapter;
    private final ProductCostRepositoryAdapter productCostRepositoryAdapter;
    private final ProductDimensionRepositoryAdapter productDimensionRepositoryAdapter;

    public ProductService(
        ProductRepositoryAdapter productRepositoryAdapter,
        ProductCostRepositoryAdapter productCostRepositoryAdapter,
        ProductDimensionRepositoryAdapter productDimensionRepositoryAdapter
    ) {
        this.productRepositoryAdapter = productRepositoryAdapter;
        this.productCostRepositoryAdapter = productCostRepositoryAdapter;
        this.productDimensionRepositoryAdapter = productDimensionRepositoryAdapter;
    }

    //Product methods
    public Product addProduct(ProductRequestDto productRequestDto) {
        return this.productRepositoryAdapter.save(toModel(productRequestDto));
    }

    public void deleteProduct(Long productId) {
        this.productRepositoryAdapter.deleteById(productId);
    }

    public void updateProduct(Long productId, ProductRequestDto productRequestDto) {
        this.productRepositoryAdapter.updateProduct(productId, toModel(productRequestDto));
    }

    public List<Product> getAllProducts() {
        return this.productRepositoryAdapter.findAll();
    }

    public Optional<Product> getProductById(Long productId) {
        return this.productRepositoryAdapter.findById(productId);
    }

    //Product cost methods
    public ProductCost addProductCost(Long productId, ProductCostRequestDto productCostRequestDto) {
        return this.productCostRepositoryAdapter.save(productId, toModel(productCostRequestDto));
    }

    public void updateProductCost(Long costId, ProductCostRequestDto productCostRequestDto) {
        productCostRepositoryAdapter.update(costId, toModel(productCostRequestDto));
    }

    public void deleteProductCost(Long costId) {
        this.productCostRepositoryAdapter.deleteById(costId);
    }

    //Product dimension methods
    public void updateProductDimension(
        Long productId,
        ProductDimensionRequestDto productDimensionRequestDto
    ) {
       this.productDimensionRepositoryAdapter.update(productId, toModel(productDimensionRequestDto));
    }

    private static Product toModel(ProductRequestDto productRequestDto) {
        return Product.builder()
            .name(productRequestDto.name())
            .build();
    }

    private static ProductCost toModel(ProductCostRequestDto productCostRequestDto) {
        return ProductCost.builder()
            .name(productCostRequestDto.name())
            .price(productCostRequestDto.price())
            .build();
    }

    private static ProductDimension toModel(ProductDimensionRequestDto productDimensionRequestDto) {
        return ProductDimension.builder()
            .height(productDimensionRequestDto.height())
            .width(productDimensionRequestDto.width())
            .depth(productDimensionRequestDto.depth())
            .build();
    }
}
