package com.develeveling.backend.service;

import com.develeveling.backend.entity.TargetCompany;
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
    public User addTargetCompany(Long userId, String companyName) {
        User user = getUserById(userId);

        boolean companyExists = user.getTargetCompanies().stream()
                .anyMatch(tc -> tc.getCompanyName().equalsIgnoreCase(companyName));

        if (!companyExists) {
            TargetCompany newTarget = new TargetCompany(user, companyName);
            user.getTargetCompanies().add(newTarget);
            return userRepository.save(user);
        }
        return user;
    }

    public User updateTargetRoles(Long userId, Set<String> roles) {
        User user = getUserById(userId);
        user.setTargetRoles(roles);
        return userRepository.save(user);
    }

    public User setGithubUsername(Long userId, String githubUsername) {
        User user = getUserById(userId);
        user.setGithubUsername(githubUsername);
        return userRepository.save(user);
    }
}