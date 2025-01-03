package com.gubu.buffer.domain.product;

import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.domain.model.ProductRecord;
import com.gubu.buffer.infrastructure.database.postgreql.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void saveProduct(ProductRequestDto productDtoRecord) {
        this.productRepository.save(ProductMapper.toEntity(productDtoRecord));
    }

    public void deleteProduct(Long id) {
        this.productRepository.deleteById(id);
    }

    public List<ProductRecord> getAllProducts() {
        return this.productRepository.findAll().stream()
            .map(ProductMapper::toRecord)
            .toList();
    }
}
