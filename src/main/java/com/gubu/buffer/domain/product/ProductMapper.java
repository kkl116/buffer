package com.gubu.buffer.domain.product;

import com.gubu.buffer.application.dto.request.ProductRequestDto;
import com.gubu.buffer.application.dto.response.ProductCostResponseDto;
import com.gubu.buffer.application.dto.response.ProductResponseDto;
import com.gubu.buffer.infrastructure.database.postgreql.product.ProductEntity;
import com.gubu.buffer.infrastructure.database.postgreql.product.cost.ProductCostEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private ProductMapper() {}

    public static ProductEntity toEntity(ProductRequestDto productRequestDto) {
        return ProductEntity.builder()
           .name(productRequestDto.name())
           .build();
    }

    public static ProductResponseDto toResponse(ProductEntity product) {
        return ProductResponseDto.builder()
           .id(product.getId())
           .name(product.getName())
           .build();
    }

    public static ProductCostResponseDto toResponse(ProductCostEntity productCost) {
        return ProductCostResponseDto.builder()
            .id(productCost.getId())
            .build();
    }
}
