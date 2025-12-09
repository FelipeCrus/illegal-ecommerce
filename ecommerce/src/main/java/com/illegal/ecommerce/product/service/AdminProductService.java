package com.illegal.ecommerce.product.service;

import com.illegal.ecommerce.product.dto.ProductRequestDTO;
import com.illegal.ecommerce.product.dto.ProductResponseDTO;
import com.illegal.ecommerce.product.model.Product;
import com.illegal.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository repository;

    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        product.setImageUrl(dto.imageUrl());

        repository.save(product);

        return new ProductResponseDTO(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStock(), product.getImageUrl());
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        product.setImageUrl(dto.imageUrl());

        repository.save(product);

        return new ProductResponseDTO(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStock(), product.getImageUrl());
    }

    public void deleteProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        repository.delete(product);
    }
}

