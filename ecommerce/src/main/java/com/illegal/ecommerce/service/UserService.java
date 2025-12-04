package com.illegal.ecommerce.service;


import com.illegal.ecommerce.domain.model.User;
import com.illegal.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    public class UserService {

        private final UserRepository repository;

        public UserService(UserRepository repository) {
            this.repository = repository;
        }

        public User createUser(User user) {
            return repository.save(user);
        }

        public List<User> getAllUsers() {
            return repository.findAll();
        }
}

