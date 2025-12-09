package com.illegal.ecommerce.user.controller;

import com.illegal.ecommerce.user.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.illegal.ecommerce.user.model.User;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService service;

    @GetMapping
    public ResponseEntity<List<User>> listAllUsers() {
        return ResponseEntity.ok(service.listAllUsers());
    }

    @PostMapping("/promote/{id}")
    public ResponseEntity<String> promoteToAdmin(@PathVariable Long id) {
        service.promoteToAdmin(id);
        return ResponseEntity.ok("Usuário promovido para ADMIN");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok("Usuário deletado");
    }
}
