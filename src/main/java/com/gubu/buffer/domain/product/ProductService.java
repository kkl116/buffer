package com.gubu.buffer.domain.product;

import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.domain.model.ProductRecord;
import com.gubu.buffer.infrastructure.database.postgreql.ProductEntity;
import com.gubu.buffer.infrastructure.database.postgreql.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gubu.buffer.domain.product.ProductMapper.toEntity;
import static com.gubu.buffer.domain.product.ProductMapper.toRecord;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductRecord addProduct(ProductRequestDto productRequestDto) {
        ProductEntity entity = toEntity(productRequestDto);
        return toRecord(this.productRepository.save(entity));
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

    public List<ProductRecord> getAllProducts() {
        return this.productRepository.findAll().stream()
            .map(ProductMapper::toRecord)
            .toList();
    }
}
