package com.illegal.ecommerce.product.dto;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock,
        String imageUrl
) { }
