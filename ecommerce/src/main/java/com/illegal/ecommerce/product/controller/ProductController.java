package com.illegal.ecommerce.product.controller;

import com.illegal.ecommerce.product.dto.ProductRequestDTO;
import com.illegal.ecommerce.product.dto.ProductResponseDTO;
import com.illegal.ecommerce.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ProductResponseDTO createProduct(@RequestBody ProductRequestDTO dto) {
        return service.createProduct(dto);
    }

    @GetMapping
    public List<ProductResponseDTO> listProducts() {
        return service.listProducts();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id) {
        return service.getProduct(id);
    }
}
