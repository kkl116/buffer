package com.gubu.buffer.application;
import com.gubu.buffer.domain.model.ProductRecord;
import com.gubu.buffer.domain.product.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    List<ProductRecord> getProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/product")
    void saveProduct(@RequestBody ProductRecord productRecord) {
        productService.saveProduct(productRecord);
    }

    @DeleteMapping("/product/{id}")
    void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}