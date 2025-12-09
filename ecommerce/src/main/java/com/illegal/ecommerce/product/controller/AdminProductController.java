package com.illegal.ecommerce.product.controller;

import com.illegal.ecommerce.product.dto.ProductRequestDTO;
import com.illegal.ecommerce.product.dto.ProductResponseDTO;
import com.illegal.ecommerce.product.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final AdminProductService service;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(service.createProduct(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(service.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.ok("Produto deletado");
    }
}
