package com.develeveling.backend.controller;

import com.develeveling.backend.service.QuestGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Autowired
    private QuestGenerationService questGenerationService;

    @PostMapping("/generate-quests")
    public ResponseEntity<String> triggerDailyQuestGeneration() {
        questGenerationService.generateAllDailyQuests();
        return ResponseEntity.ok("Daily quest generation job triggered successfully!");
    }
}