package com.illegal.ecommerce.admin.controller;

import com.illegal.ecommerce.user.model.Role;
import com.illegal.ecommerce.user.model.User;
import com.illegal.ecommerce.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<User>> listAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/promote/{id}")
    public ResponseEntity<String> promoteToAdmin(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);

        return ResponseEntity.ok("Usuário promovido para ADMIN");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Usuário não existe");
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok("Usuário deletado");
    }
}
