package com.develeveling.backend.service;

import com.develeveling.backend.dto.LeaderboardEntryDto;
import com.develeveling.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService {

    @Autowired
    private UserRepository userRepository;

    public List<LeaderboardEntryDto> getTop10Leaderboard() {

        Pageable top10 = PageRequest.of(0, 10);
        return userRepository.findTopUsersByTotalXp(top10);
    }
}