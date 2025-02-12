package com.gubu.buffer.domain.product;

import com.gubu.buffer.application.dto.request.ProductCostRequestDto;
import com.gubu.buffer.application.dto.request.ProductDimensionRequestDto;
import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.model.ProductCost;
import com.gubu.buffer.domain.model.ProductDimensions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepositoryAdapter productRepositoryAdapter;
    private final ProductCostRepositoryAdapter productCostRepositoryAdapter;
    private final ProductDimensionRepositoryAdapter productDimensionRepositoryAdapter;

    public ProductService(
        ProductRepositoryAdapter productRepositoryAdapter,
        ProductCostRepositoryAdapter productCostRepositoryAdapter,
        ProductDimensionRepositoryAdapter productDimensionRepositoryAdapter
    ) {
        this.productRepositoryAdapter = productRepositoryAdapter;
        this.productCostRepositoryAdapter = productCostRepositoryAdapter;
        this.productDimensionRepositoryAdapter = productDimensionRepositoryAdapter;
    }

    //Product methods
    public Product addProduct(ProductRequestDto productRequestDto) {
        return this.productRepositoryAdapter.save(toModel(productRequestDto));
    }

    public void deleteProduct(Long productId) {
        this.productRepositoryAdapter.deleteById(productId);
    }

    public void updateProduct(Long productId, ProductRequestDto productRequestDto) {
        this.productRepositoryAdapter.updateProduct(productId, toModel(productRequestDto));
    }

    public List<Product> getAllProducts(Set<ProductField> fields) {
        return this.productRepositoryAdapter.findAll(toEntityFields(fields));
    }

    public Optional<Product> getProductById(Long productId, Set<ProductField> fields) {
        return this.productRepositoryAdapter.findById(productId, toEntityFields(fields));
    }

    //Product cost methods
    public ProductCost addProductCost(Long productId, ProductCostRequestDto productCostRequestDto) {
        return this.productCostRepositoryAdapter.save(productId, toModel(productCostRequestDto));
    }

    public void updateProductCost(Long costId, ProductCostRequestDto productCostRequestDto) {
        productCostRepositoryAdapter.update(costId, toModel(productCostRequestDto));
    }

    public void deleteProductCost(Long costId) {
        this.productCostRepositoryAdapter.deleteById(costId);
    }

    //Product dimension methods
    public void updateProductDimension(
        Long productId,
        ProductDimensionRequestDto productDimensionRequestDto
    ) {
       this.productDimensionRepositoryAdapter.update(productId, toModel(productDimensionRequestDto));
    }

    private static Product toModel(ProductRequestDto productRequestDto) {
        return Product.builder()
            .name(productRequestDto.name())
            .description(productRequestDto.description())
            .price(productRequestDto.price())
            .build();
    }

    private static ProductCost toModel(ProductCostRequestDto productCostRequestDto) {
        return ProductCost.builder()
            .name(productCostRequestDto.name())
            .price(productCostRequestDto.price())
            .build();
    }

    private static ProductDimensions toModel(ProductDimensionRequestDto productDimensionRequestDto) {
        return ProductDimensions.builder()
            .height(productDimensionRequestDto.height())
            .width(productDimensionRequestDto.width())
            .depth(productDimensionRequestDto.depth())
            .build();
    }

    private static Set<ProductField> toEntityFields(Set<ProductField> fields) {
        Map<Boolean, List<ProductField>> partitionedFields = fields.stream()
            .collect(Collectors.partitioningBy(ProductField::isCalculatedField));

        Set<ProductField> entityFields = partitionedFields.get(true).stream()
            .flatMap(field -> field.getRequisiteFields().stream())
            .collect(Collectors.toSet());

        entityFields.addAll(partitionedFields.get(false));
        return entityFields;
    }
}
