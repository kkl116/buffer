package com.gubu.buffer.domain.product;

import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.application.dto.response.ProductResponseDto;
import com.gubu.buffer.domain.model.ProductRecord;
import com.gubu.buffer.infrastructure.database.postgreql.product.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private ProductMapper() {}

    public static ProductRecord toRecord(ProductEntity productEntity) {
        return ProductRecord.builder()
            .id(productEntity.getId())
            .name(productEntity.getName())
            .build();
    }

    public static ProductEntity toEntity(ProductRequestDto productRequestDto) {
        return ProductEntity.builder()
           .name(productRequestDto.name())
           .build();
    }

    public static ProductResponseDto toResponse(ProductRecord productRecord) {
        return ProductResponseDto.builder()
           .id(productRecord.id())
           .name(productRecord.name())
           .build();
    }
}
