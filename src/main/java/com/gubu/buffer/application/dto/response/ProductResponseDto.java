package com.gubu.buffer.application.dto.response;

import lombok.Builder;

@Builder
public record ProductResponseDto(String name, Long id) {}
