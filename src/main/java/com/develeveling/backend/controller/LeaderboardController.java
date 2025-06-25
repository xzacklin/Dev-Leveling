package com.develeveling.backend.controller;

import com.develeveling.backend.dto.LeaderboardEntryDto;
import com.develeveling.backend.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboard() {
        List<LeaderboardEntryDto> leaderboard = leaderboardService.getTop10Leaderboard();
        return ResponseEntity.ok(leaderboard);
    }
}