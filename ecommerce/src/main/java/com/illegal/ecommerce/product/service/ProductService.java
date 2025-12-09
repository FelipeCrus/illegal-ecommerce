package com.illegal.ecommerce.product.service;

import com.illegal.ecommerce.product.dto.ProductRequestDTO;
import com.illegal.ecommerce.product.dto.ProductResponseDTO;
import com.illegal.ecommerce.product.model.Product;
import com.illegal.ecommerce.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductResponseDTO> listProducts() {
        return repository.findAll().stream()
                .map(p -> new ProductResponseDTO(p.getId(), p.getName(), p.getDescription(),
                        p.getPrice(), p.getStock(), p.getImageUrl()))
                .collect(Collectors.toList());
    }

    public ProductResponseDTO getProduct(Long id) {
        Product p = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return new ProductResponseDTO(p.getId(), p.getName(), p.getDescription(),
                p.getPrice(), p.getStock(), p.getImageUrl());
    }

}