package com.develeveling.backend.service;

import com.develeveling.backend.dto.RegisterRequest;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.exception.DuplicateUserException;
import com.develeveling.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest req) {
        try {
            if (userRepository.findByUsername(req.getUsername()).isPresent()) {
                throw new DuplicateUserException("Username");
            }
            if (userRepository.findByEmail(req.getEmail()).isPresent()) {
                throw new DuplicateUserException("Email");
            }

            User user = new User();
            user.setUsername(req.getUsername().trim().toLowerCase());
            user.setEmail(req.getEmail().trim().toLowerCase());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            userRepository.save(user);

        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateUserException("Username or Email");
        }
    }
}
