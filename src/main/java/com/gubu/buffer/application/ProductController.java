package com.gubu.buffer.application;
import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.application.dto.response.ProductCostResponseDto;
import com.gubu.buffer.application.dto.response.ProductResponseDto;
import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.product.ProductService;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductCostEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    ResponseEntity<List<ProductResponseDto>> getProducts() {
        List<ProductResponseDto> products = productService.getAllProducts().stream()
            .map(this::toResponse)
            .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/product/{productId}")
    ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {
        return productService.getProductById(productId)
            .map(this::toResponse)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/product")
    ResponseEntity<ProductResponseDto> saveProduct(@RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = toResponse(productService.addProduct(productRequestDto));
        return ResponseEntity.ok(productResponseDto);

    }

    @PatchMapping("/product/{productId}")
    ResponseEntity<Void> updateProduct(@PathVariable Long productId, @RequestBody ProductRequestDto productRequestDto) {
        productService.updateProduct(productId, productRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/product/{productId}")
    ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/product/{productId}/cost")
    ResponseEntity<ProductCostResponseDto> addProductCost(
        @PathVariable Long productId,
        @RequestBody ProductCostRequestDto productCostRequestDto
    ) {
        ProductCost productCost = productService.addProductCost(productId, productCostRequestDto);
        return ResponseEntity.ok(toResponse(productCost));
    }

    @PatchMapping("/product/cost/{costId}")
    ResponseEntity<Void> updateProductCost(
        @PathVariable Long costId,
        @RequestBody ProductCostRequestDto productCostRequestDto
    ) {
        productService.updateProductCost(costId, productCostRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/product/cost/{costId}")
    ResponseEntity<Void> deleteProductCost(@PathVariable Long costId) {
        productService.deleteProductCost(costId);
        return ResponseEntity.ok().build();
    }

    private ProductResponseDto toResponse(Product product) {
        return ProductResponseDto.builder()
            .id(product.getId())
            .name(product.getName())
            .build();
    }

    private ProductCostResponseDto toResponse(ProductCost productCost) {
        return ProductCostResponseDto.builder()
            .id(productCost.getId())
            .build();
    }
}
