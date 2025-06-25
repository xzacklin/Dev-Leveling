package com.develeveling.backend.service;

import com.develeveling.backend.entity.User;
import com.develeveling.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    public User updateTargetCompanies(Long userId, Set<String> companies) {
        User user = getUserById(userId);
        user.setTargetCompanies(companies);
        return userRepository.save(user);
    }
    public User updateTargetRoles(Long userId, Set<String> roles) {
        User user = getUserById(userId);
        user.setTargetRoles(roles);
        return userRepository.save(user);
    }
}