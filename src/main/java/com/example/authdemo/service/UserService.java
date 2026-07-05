package com.example.authdemo.service;

import com.example.authdemo.model.User;
import com.example.authdemo.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public boolean usernameExists(String username) {
        return repo.findByUsername(username).isPresent();
    }

    public User registerNewUser(String username, String rawPassword) {
        if (usernameExists(username)) throw new IllegalArgumentException("username taken");
        var user = new User(username, encoder.encode(rawPassword));
        return repo.save(user);
    }
}