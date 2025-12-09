package com.illegal.ecommerce.user.service;

import com.illegal.ecommerce.user.model.Role;
import com.illegal.ecommerce.user.model.User;
import com.illegal.ecommerce.user.repository.UserRepository;
import com.illegal.ecommerce.security.JwtService;
import com.illegal.ecommerce.user.dto.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponseDTO register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        String token = jwtService.generateToken(user);


        return new LoginResponse(token);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public User updateProfile(String email, User updated) {
        User user = getByEmail(email);

        user.setName(updated.getName());
        user.setEmail(updated.getEmail());

        return userRepository.save(user);
    }

    public void updatePassword(String email, String newPassword) {
        User user = getByEmail(email);

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    public void delete(String email) {
        User user = getByEmail(email);

        userRepository.delete(user);
    }

}
