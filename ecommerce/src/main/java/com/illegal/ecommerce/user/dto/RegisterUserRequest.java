package com.illegal.ecommerce.user.dto;

public record RegisterUserRequest(
        String name,
        String email,
        String password
) { }
