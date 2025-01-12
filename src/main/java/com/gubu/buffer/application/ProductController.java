package com.gubu.buffer.application;
import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.application.dto.response.ProductResponseDto;
import com.gubu.buffer.domain.product.ProductMapper;
import com.gubu.buffer.domain.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.gubu.buffer.domain.product.ProductMapper.toResponse;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    ResponseEntity<List<ProductResponseDto>> getProducts() {
        List<ProductResponseDto> products = productService.getAllProducts().stream()
            .map(ProductMapper::toResponse)
            .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/product/{id}")
    ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        return productService.getProductById(id)
            .map(ProductMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/product")
    ResponseEntity<ProductResponseDto> saveProduct(@RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = toResponse(productService.addProduct(productRequestDto));
        return ResponseEntity.ok(productResponseDto);

    }

    @PatchMapping("/product/{id}")
    ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto) {
        productService.updateProduct(id, productRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/product/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
