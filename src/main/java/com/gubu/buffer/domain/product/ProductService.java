package com.gubu.buffer.domain.product;

import com.gubu.buffer.domain.model.ProductRecord;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void saveProduct(ProductRecord productRecord) {
        this.productRepository.save(ProductMapper.toEntity(productRecord));
    }

    public void deleteProduct(ProductRecord productRecord) {
        this.productRepository.delete(ProductMapper.toEntity(productRecord));
    }

    public List<ProductRecord> getAllProducts() {
        return this.productRepository.findAll().stream()
            .map(ProductMapper::toRecord)
            .collect(toList());
    }
}
