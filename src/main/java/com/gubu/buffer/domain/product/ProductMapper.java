package com.gubu.buffer.domain.product;

import com.gubu.buffer.domain.model.ProductRecord;
import com.gubu.buffer.infrastructure.database.postgreql.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public static ProductRecord toRecord(ProductEntity productEntity) {
        return ProductRecord.builder()
            .id(productEntity.getId())
            .name(productEntity.getName())
            .build();
    }

    public static ProductEntity toEntity(ProductRecord productRecord) {
        return ProductEntity.builder()
            .id(productRecord.id())
            .name(productRecord.name())
            .build();
    }
}
