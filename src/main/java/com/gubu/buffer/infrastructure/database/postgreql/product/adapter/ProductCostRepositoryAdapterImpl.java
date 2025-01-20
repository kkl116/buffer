package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.product.ProductCostRepositoryAdapter;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductCostRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.gubu.buffer.infrastructure.database.postgreql.product.adapter.EntityModelMapper.toEntity;
import static com.gubu.buffer.infrastructure.database.postgreql.product.adapter.EntityModelMapper.toModel;

@Component
public class ProductCostRepositoryAdapterImpl implements ProductCostRepositoryAdapter {

    private final ProductRepository productRepository;
    private final ProductCostRepository productCostRepository;

    public ProductCostRepositoryAdapterImpl(
        ProductRepository productRepository,
        ProductCostRepository productCostRepository
    ) {
        this.productRepository = productRepository;
        this.productCostRepository = productCostRepository;
    }

    @Override
    public Optional<ProductCost> findById(Long id) {
        return productCostRepository.findById(id).map(EntityModelMapper::toModel);
    }

    @Override
    @Transactional
    public ProductCost save(Long productId, ProductCost productCost) {
        var productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException(String.format("Product id %s not found", productId)));

        var productCostEntity = toEntity(productCost);

        //ensure both sides of relation is updated
        productEntity.getCosts().add(productCostEntity);
        productCostEntity.setProduct(productEntity);

        //Should persist the owning side of the relationship - the one that has the FK
        this.productCostRepository.save(productCostEntity);
        return toModel(productCostEntity);
    }

    @Override
    public void deleteAll() {
        this.productCostRepository.deleteAll();
    }

    @Override
    public void update(Long costId, ProductCost productCost) {
        var productCostEntity = this.productCostRepository.findById(costId)
            .orElseThrow(() -> new RuntimeException(String.format("Product cost id %s not found", costId)));

        if (productCost.getName() != null) {
            productCostEntity.setName(productCost.getName());
        }

        if (productCost.getPrice() != null) {
            productCostEntity.setPrice(productCost.getPrice());
        }

        this.productCostRepository.save(productCostEntity);
    }

    @Override
    public void deleteById(Long costId) {
        var productCost = this.productCostRepository.findById(costId)
            .orElseThrow(() -> new RuntimeException(String.format("Product cost id %s not found", costId)));

        var productEntity = productCost.getProduct();

        productEntity.getCosts().remove(productCost);
        this.productCostRepository.deleteById(costId);
    }
}
