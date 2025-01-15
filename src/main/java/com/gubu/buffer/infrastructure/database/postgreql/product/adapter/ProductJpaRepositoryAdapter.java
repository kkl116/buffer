package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.product.ProductRepositoryAdapter;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductDimensionRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.gubu.buffer.infrastructure.database.postgreql.product.adapter.EntityModelMapper.toEntity;
import static com.gubu.buffer.infrastructure.database.postgreql.product.adapter.EntityModelMapper.toModel;

@Component
public class ProductJpaRepositoryAdapter implements ProductRepositoryAdapter {

    private final ProductRepository productRepository;
    private final ProductDimensionRepository productDimensionRepository;

    public ProductJpaRepositoryAdapter(
        ProductRepository productRepository,
        ProductDimensionRepository productDimensionRepository
    ) {
        this.productRepository = productRepository;
        this.productDimensionRepository = productDimensionRepository;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll().stream().map(EntityModelMapper::toModel).toList();
    }

    @Override
    public Product save(Product product) {
        var productEntity = this.productRepository.save(toEntity(product));
        //Also initialise product dimension for the product here
        var productDimensionEntity = ProductDimensionEntity.builder()
            .height(0.0)
            .width(0.0)
            .depth(0.0)
            .build();

        productEntity.setProductDimension(productDimensionEntity);
        productDimensionEntity.setProduct(productEntity);
        this.productDimensionRepository.save(productDimensionEntity);

        return toModel(productEntity);
    }

    @Override
    public void deleteById(Long productId) {
        this.productRepository.deleteById(productId);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return this.productRepository.findById(productId).map(EntityModelMapper::toModel);
    }

    @Override
    public void deleteAll() {
        this.productRepository.deleteAll();
    }

    @Override
    public void updateProduct(Long productId, Product product) {
        var productEntity = this.productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException(String.format("Product id %s not found", productId)));

        productEntity.setName(product.getName());
        this.productRepository.save(productEntity);
    }
}
