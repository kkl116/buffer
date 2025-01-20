package com.gubu.buffer.application;
import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.application.dto.request.ProductDimensionRequestDto;
import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.application.dto.response.ProductCostResponseDto;
import com.gubu.buffer.application.dto.response.ProductResponseDto;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.gubu.buffer.application.ResponseMapper.toResponse;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Product level endpoints
    @GetMapping("/products")
    ResponseEntity<List<ProductResponseDto>> getProducts(
        @RequestParam(value = "fields", required = false) String fieldsParam
    ) {
        List<ProductResponseDto> products = productService.getAllProducts(parseFieldsParam(fieldsParam)).stream()
            .map(ResponseMapper::toResponse)
            .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/product/{productId}")
    ResponseEntity<ProductResponseDto> getProduct(
        @PathVariable Long productId,
        @RequestParam(value = "fields", required = false) String fieldsParam
    ) {
        return productService.getProductById(productId, parseFieldsParam(fieldsParam))
            .map(ResponseMapper::toResponse)
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

    //Product cost endpoints
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

    @PatchMapping("/product/{productId}/dimension")
    ResponseEntity<Void> updateProductDimension(
        @PathVariable Long productId,
        @RequestBody ProductDimensionRequestDto productDimensionRequestDto
    ) {
        productService.updateProductDimension(productId, productDimensionRequestDto);
        return ResponseEntity.ok().build();
    }

    private List<String> parseFieldsParam(String fieldsParam) {
        return Optional.ofNullable(fieldsParam)
            .map(param -> List.of(param.split(",")))
            .orElse(List.of());
    }
}
