package com.illegal.ecommerce.controller;

import com.illegal.ecommerce.dto.LoginResponse;
import com.illegal.ecommerce.dto.LonginRequest;
import com.illegal.ecommerce.repository.UserRepository;
import com.illegal.ecommerce.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.DocFlavor;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repository;
    private final JwtService jwtService;

    public AuthController(UserRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

        @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LonginRequest request){
        var user = repository.findByEmail(request.email());

        if (user.isEmpty() || !user.get().getPassword().equals(request.password())){
            return ResponseEntity.status(401).build();
        }

        String token = jwtService.generateToken(user.get().getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }

}
