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