package com.develeveling.backend.controller;

import com.develeveling.backend.dto.ProgrammingDashboardDto;
import com.develeveling.backend.dto.UserDashboardStatsDto;
import com.develeveling.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/{userId}/dashboard")
    public ResponseEntity<UserDashboardStatsDto> getUserDashboard(@PathVariable Long userId) {
        UserDashboardStatsDto dashboardStats = dashboardService.getDashboardStatsForUser(userId);
        return ResponseEntity.ok(dashboardStats);
    }

    @GetMapping("/{userId}/dashboard/programming")
    public ResponseEntity<ProgrammingDashboardDto> getProgrammingDashboard(@PathVariable Long userId) {
        ProgrammingDashboardDto programmingStats = dashboardService.getProgrammingDashboardStats(userId);
        return ResponseEntity.ok(programmingStats);
    }
}