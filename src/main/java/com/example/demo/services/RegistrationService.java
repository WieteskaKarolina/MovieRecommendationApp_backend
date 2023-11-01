package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository for database operations

    public boolean isUsernameTaken(String username) {
        // Check if the username already exists in the database
        return userRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        // Check if the email is already in use in the database
        return userRepository.existsByEmail(email);
    }

    public void registerUser(User user) {
        // Implement registration logic, such as password hashing
        // You should also set other properties of the user entity as needed
        String encodedPassword = encodePassword(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    // You can use Spring Security's BCryptPasswordEncoder to hash passwords
    private String encodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}

