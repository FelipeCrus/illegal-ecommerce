package com.illegal.ecommerce.product.dto;


public record ProductRequestDTO(
        String name,
        String description,
        Double price,
        Integer stock,
        String imageUrl
) { }
