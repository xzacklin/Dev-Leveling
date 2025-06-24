package com.develeveling.backend.service;

import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.repository.QuestRepository;
import com.develeveling.backend.repository.UserRepository;
import com.develeveling.backend.service.generator.QuestGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestGenerationService {

    private static final Logger log = LoggerFactory.getLogger(QuestGenerationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private List<QuestGenerator> questGenerators;

    @Scheduled(cron = "0 0 0 * * ?") // Midnight daily
    @Transactional
    public void generateAllDailyQuests() {
        log.info("Starting daily quest generation using Strategy Pattern...");
        List<User> users = userRepository.findAll();
        List<Quest> questsToCreate = new ArrayList<>();

        for (User user : users) {
            for (QuestGenerator generator : questGenerators) {
                generator.generate(user).ifPresent(questsToCreate::add);
            }
        }

        if (!questsToCreate.isEmpty()) {
            questRepository.saveAll(questsToCreate);
        }
        log.info("Finished daily quest generation. Created {} total quests.", questsToCreate.size());
    }
}