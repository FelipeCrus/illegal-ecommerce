package com.illegal.ecommerce.user.controller;

import com.illegal.ecommerce.user.dto.LoginRequest;
import com.illegal.ecommerce.user.dto.LoginResponse;
import com.illegal.ecommerce.user.dto.RegisterUserRequest;
import com.illegal.ecommerce.user.dto.UserResponseDTO;
import com.illegal.ecommerce.user.model.User;
import com.illegal.ecommerce.user.repository.UserRepository;
import com.illegal.ecommerce.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class MeController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MeController(UserService userService,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public User getProfile(Authentication auth) {
        return userService.getByEmail(auth.getName());
    }

     @PutMapping
    public User updateProfile(Authentication auth, @RequestBody User updated) {
        return userService.updateProfile(auth.getName(), updated);
    }

     @PutMapping("/password")
    public String updatePassword(Authentication auth, @RequestBody String newPassword) {
        userService.updatePassword(auth.getName(), newPassword);
        return "Senha atualizada com sucesso!";
    }

    @DeleteMapping
    public String deleteAccount(Authentication auth) {
        userService.delete(auth.getName());
        return "Conta deletada com sucesso!";
    }
}
