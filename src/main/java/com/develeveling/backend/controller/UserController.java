package com.develeveling.backend.controller;

import com.develeveling.backend.dto.UpdateGithubUsernameRequest;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.develeveling.backend.dto.AddTargetCompanyRequest;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/target-companies")
    public ResponseEntity<User> addTargetCompany(@PathVariable Long userId, @RequestBody AddTargetCompanyRequest request) {
        User updatedUser = userService.addTargetCompany(userId, request.getCompanyName());
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{userId}/target-roles")
    public ResponseEntity<User> updateTargetRoles(@PathVariable Long userId, @RequestBody Set<String> roles) {
        User updatedUser = userService.updateTargetRoles(userId, roles);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{userId}/github-username")
    public ResponseEntity<User> setGithubUsername(@PathVariable Long userId, @RequestBody UpdateGithubUsernameRequest request) {
        User updatedUser = userService.setGithubUsername(userId, request.getGithubUsername());
        return ResponseEntity.ok(updatedUser);
    }

}