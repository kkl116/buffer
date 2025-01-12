package com.gubu.buffer.application;
import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.application.dto.response.ProductResponseDto;
import com.gubu.buffer.domain.product.ProductMapper;
import com.gubu.buffer.domain.product.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gubu.buffer.domain.product.ProductMapper.toResponse;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    List<ProductResponseDto> getProducts() {
        //TODO: Return responseEntity to include status codes etc.
        return productService.getAllProducts().stream()
            .map(ProductMapper::toResponse)
            .toList();
    }

    @GetMapping("/product/{id}")
    ProductResponseDto getProduct(@PathVariable Long id) {
        return productService.getProductById(id)
            .map(ProductMapper::toResponse)
            .orElseThrow(() -> new RuntimeException(String.format("Product id %s not found", id)));
    }

    @PostMapping("/product")
    ProductResponseDto saveProduct(@RequestBody ProductRequestDto productRequestDto) {
        return toResponse(productService.addProduct(productRequestDto));
    }

    @PatchMapping("/product/{id}")
    void updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto) {
        productService.updateProduct(id, productRequestDto);
    }

    @DeleteMapping("/product/{id}")
    void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
