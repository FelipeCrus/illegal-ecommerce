package com.illegal.ecommerce.config;

import com.illegal.ecommerce.user.model.Role;
import com.illegal.ecommerce.user.model.User;
import com.illegal.ecommerce.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminConfig {

    @Bean
    public CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!userRepository.existsByEmail("admin@illegal.com")) {
                User admin = new User();
                admin.setName("Administrador");
                admin.setEmail("admin@illegal.com");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setRole(Role.ROLE_ADMIN);

                userRepository.save(admin);
                System.out.println("ADMIN criado com sucesso.");
            }
        };
    }
}
