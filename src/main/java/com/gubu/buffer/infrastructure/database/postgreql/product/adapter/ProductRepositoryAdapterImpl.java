package com.gubu.buffer.infrastructure.database.postgreql.product.adapter;

import com.gubu.buffer.domain.model.Product;
import com.gubu.buffer.domain.product.ProductField;
import com.gubu.buffer.domain.product.ProductRepositoryAdapter;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductCostEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductDimensionsEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.entity.ProductEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductDimensionRepository;
import com.gubu.buffer.infrastructure.database.postgreql.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<Product> findAll(Set<ProductField> fields) {
        //TODO: Return objects instead of exceptions!
        if (fields.isEmpty()) {
            return this.productRepository.findAll().stream().map(EntityModelMapper::toModel).toList();
        }

        List<Tuple> results = executeFetchProductQuery(fields, null);

        if (results.isEmpty()) {
            return List.of();
        }

        //deal with result list here. - probably partition results my productId, then do reduce
        return results.stream()
            .collect(Collectors.groupingBy(row -> row.get("id", Long.class)))
            .values().stream()
            .map(rows -> convertRowsToProduct(fields, rows))
            .toList();
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
    public Optional<Product> findById(Long productId, Set<ProductField> fields) {
        if (fields.isEmpty()) {
            //just return the whole entity if none specified
            return this.productRepository.findById(productId).map(EntityModelMapper::toModel);
        }

        List<Tuple> results = executeFetchProductQuery(fields, productId);

        if (results.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(convertRowsToProduct(fields, results));
    }

    @Override
    public void deleteAll() {
        this.productRepository.deleteAll();
    }

    @Override
    public void updateProduct(Long productId, Product product) {
        var productEntity = this.productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException(String.format("Product id %s not found", productId)));

        if (product.getName() != null) {
            productEntity.setName(product.getName());
        }

        if (product.getDescription() != null) {
            productEntity.setDescription(product.getDescription());
        }

        if (product.getPrice() != null) {
            productEntity.setPrice(product.getPrice());
        }

        this.productRepository.save(productEntity);
    }

    private Product.ProductBuilder updateProduct(
        ProductField field,
        Product.ProductBuilder productBuilder,
        List<Tuple> results
    ) {
        return switch (field) {
            case ID -> productBuilder.id(results.getFirst().get(field.getValue(), Long.class));
            case NAME -> productBuilder.name(results.getFirst().get(field.getValue(), String.class));
            case DESCRIPTION -> productBuilder.description(results.getFirst().get(field.getValue(), String.class));
            case PRICE -> productBuilder.price(results.getFirst().get(field.getValue(), Double.class));
            case DIMENSIONS -> productBuilder.dimensions(
                toModel(results.getFirst().get(field.getValue(), ProductDimensionsEntity.class))
            );
            case COSTS -> productBuilder.costs(results.stream()
                .map(tuple -> tuple.get(field.getValue(), ProductCostEntity.class))
                .map(EntityModelMapper::toModel)
                .toList()
            );
            case PROFIT,PROFIT_MARGIN,TOTAL_COST -> productBuilder;
        };
    }

    private List<Tuple> executeFetchProductQuery(
        Set<ProductField> requestedFields,
        Long productId
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);
        Root<ProductEntity> root = query.from(ProductEntity.class);

        //create intermediate set of fields for query - should include Id - for grouping later
        Set<ProductField> queryFields = new HashSet<>(requestedFields);
        queryFields.add(ProductField.ID);

        List<Selection<?>> selections = new ArrayList<>();
        queryFields.forEach(field -> selections.add(root.get(field.getValue()).alias(field.getValue())));

        query.multiselect(selections);

        //if productId provided add a where clause
        Optional.ofNullable(productId).ifPresent(id -> query.where(criteriaBuilder.equal(root.get("id"), id)));
        return entityManager.createQuery(query).getResultList();
    }

    private Product convertRowsToProduct(Set<ProductField> requestedFields, List<Tuple> rows) {
        //a combiner is needed in this case because hypothetically if this changed into a parallel stream,
        //you need something to join the values of the intermediate streams that is of the new type.
        return requestedFields.stream()
            .reduce(Product.builder(),
                (builder, field) -> updateProduct(field, builder, rows),
                (builder1, builder2) -> builder2 //dummy combiner
            )
            .build();
    }
}
