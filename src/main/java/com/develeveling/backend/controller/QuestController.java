package com.develeveling.backend.controller;

import com.develeveling.backend.dto.QuestDto;
import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quests") // Base path for all quest actions
public class QuestController {

    @Autowired
    private QuestService questService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<Quest> createQuestForUser(@PathVariable Long userId, @RequestBody QuestDto questDto) {
        Quest createdQuest = questService.createQuestForUser(userId, questDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuest);
    }

    @PatchMapping("/{questId}/complete")
    public ResponseEntity<Quest> completeQuest(@PathVariable Long questId) {
        Quest completedQuest = questService.completeQuest(questId);
        return ResponseEntity.ok(completedQuest);
    }
}