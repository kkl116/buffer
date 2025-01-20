package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.product.ProductRepositoryAdapter;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionsEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductDimensionRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gubu.buffer.infrastructure.database.postgreql.product.adapter.EntityModelMapper.toEntity;
import static com.gubu.buffer.infrastructure.database.postgreql.product.adapter.EntityModelMapper.toModel;

@Component
public class ProductRepositoryAdapterImpl implements ProductRepositoryAdapter {

    @PersistenceContext
    private EntityManager entityManager;
    private final ProductRepository productRepository;
    private final ProductDimensionRepository productDimensionRepository;

    public ProductRepositoryAdapterImpl(
        ProductRepository productRepository,
        ProductDimensionRepository productDimensionRepository
    ) {
        this.productRepository = productRepository;
        this.productDimensionRepository = productDimensionRepository;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll().stream().map(EntityModelMapper::toModel).toList();
    }

    @Override
    public Product save(Product product) {
        var productEntity = this.productRepository.save(toEntity(product));
        //Also initialise product dimension for the product here
        var productDimensionEntity = ProductDimensionsEntity.builder()
            .height(0.0)
            .width(0.0)
            .depth(0.0)
            .build();

        productEntity.setDimensions(productDimensionEntity);
        productDimensionEntity.setProduct(productEntity);
        this.productDimensionRepository.save(productDimensionEntity);

        return toModel(productEntity);
    }

    @Override
    public void deleteById(Long productId) {
        this.productRepository.deleteById(productId);
    }

    @Override
    public Optional<Product> findById(Long productId, List<String> fields) {
        if (fields.isEmpty()) {
            //just return the whole entity if none specified
            return this.productRepository.findById(productId).map(EntityModelMapper::toModel);
        }

        //perform a select query with specified fields
        //TODO: bruh forgot to filter by Id here.... - but refactor this so getProducts can also do the same
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);
        Root<ProductEntity> root = query.from(ProductEntity.class);
        List<Selection<?>> selections = new ArrayList<>();

        List<ProductField> productFields = fields.stream()
            .map(ProductField::fromString)
            .toList();

        productFields.forEach(field -> {
            //check validity of field
            selections.add(root.get(field.getValue()).alias(field.getValue()));
        });

        query.multiselect(selections);
        query.where(criteriaBuilder.equal(root.get("id"), productId));

        List<Tuple> results = entityManager.createQuery(query).getResultList();

        if (results.isEmpty()) {
            return Optional.empty();
        }

        //a combiner is needed in this case because hypothetically if this changed into a parallel stream,
        //you need something to join the values of the intermediate streams that is of the new type.
        return Optional.of(productFields.stream()
            .reduce(Product.builder(),
                (builder, field) -> updateProduct(field, builder, results),
                (builder1, builder2) -> builder2 //dummy combiner
            )
            .build()
        );
    }

    @Override
    public void deleteAll() {
        this.productRepository.deleteAll();
    }

    @Override
    public void updateProduct(Long productId, Product product) {
        var productEntity = this.productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException(String.format("Product id %s not found", productId)));

        productEntity.setName(product.getName());
        this.productRepository.save(productEntity);
    }

    @Getter
    private enum ProductField {
        ID("id"),
        NAME("name"),
        DIMENSIONS("dimensions"),
        COSTS("costs");

        private final String value;

        ProductField(String value) {
            this.value = value;
        }

        public static ProductField fromString(String fieldName) {
            for (ProductField field : values()) {
                if (field.name().equalsIgnoreCase(fieldName)) {
                    return field;
                }
            }
            throw new IllegalArgumentException("Invalid field: " + fieldName);
        }
    }

    private Product.ProductBuilder updateProduct(
        ProductField field,
        Product.ProductBuilder productBuilder,
        List<Tuple> results
    ) {
        return switch (field) {
            case ID -> productBuilder.id(results.getFirst().get(field.getValue(), Long.class));
            case NAME -> productBuilder.name(results.getFirst().get(field.getValue(), String.class));
            case DIMENSIONS -> productBuilder.dimensions(
                toModel(results.getFirst().get(field.getValue(), ProductDimensionsEntity.class))
            );
            case COSTS -> productBuilder.costs(results.stream()
                .map(tuple -> tuple.get("costs", ProductCostEntity.class))
                .map(EntityModelMapper::toModel)
                .toList()
            );
        };
    }
}
