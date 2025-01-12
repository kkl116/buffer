package com.gubu.buffer.domain.product;

import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.infrastructure.database.postgreql.product.ProductEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.ProductRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.cost.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.cost.ProductCostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gubu.buffer.domain.product.ProductMapper.toEntity;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCostRepository productCostRepository;

    public ProductService(
        ProductRepository productRepository,
        ProductCostRepository productCostRepository
    ) {
        this.productRepository = productRepository;
        this.productCostRepository = productCostRepository;
    }

    public ProductEntity addProduct(ProductRequestDto productRequestDto) {
        ProductEntity entity = toEntity(productRequestDto);
        return this.productRepository.save(entity);
    }

    public void deleteProduct(Long id) {
        this.productRepository.deleteById(id);
    }

    public void updateProduct(Long id, ProductRequestDto productRequestDto) {
        ProductEntity entity = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(String.format("Product id %s not found", id)));

        entity.setName(productRequestDto.name());
        this.productRepository.save(entity);
    }

    public List<ProductEntity> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Optional<ProductEntity> getProductById(Long id) {
        return this.productRepository.findById(id);
    }

    @Transactional
    public ProductCostEntity addProductCost(Long productId, ProductCostRequestDto productCostRequestDto) {
        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException(String.format("Product id %s not found", productId)));

        ProductCostEntity productCostEntity = ProductCostEntity.builder()
            .name(productCostRequestDto.name())
            .price(productCostRequestDto.price())
            .build();

        //ensure both sides of relation is updated
        //TODO: test - after adding updates show up in both sides
        product.getProductCosts().add(productCostEntity);
        productCostEntity.setProduct(product);
        this.productRepository.save(product);

        //TODO: test - returned entity has the generated Id
        return productCostEntity;
    }

    public void updateProductCost(Long costId, ProductCostRequestDto productCostRequestDto) {
        ProductCostEntity productCostEntity = productCostRepository.findById(costId)
           .orElseThrow(() -> new RuntimeException(String.format("Product cost id %s not found", costId)));

        if (productCostRequestDto.name() != null) {
            productCostEntity.setName(productCostRequestDto.name());
        }

        if (productCostRequestDto.price() != null) {
            productCostEntity.setPrice(productCostRequestDto.price());
        }

        this.productCostRepository.save(productCostEntity);
    }
}
