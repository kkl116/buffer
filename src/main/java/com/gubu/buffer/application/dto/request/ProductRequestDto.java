package com.gubu.buffer.application.dto.request;

public record ProductRequestDto(
    String name,
    String description,
    Double price
) {}
