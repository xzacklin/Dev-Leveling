package com.develeveling.backend.service.generator;

import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.model.QuestCategory;
import com.develeveling.backend.model.QuestType;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GitHubCommitQuestGenerator implements QuestGenerator {
    @Override
    public Optional<Quest> generate(User user) {
        Quest quest = new Quest();
        quest.setTitle("Maintain Your Streak");
        quest.setDescription("Make at least 3 new commits to any of your GitHub projects today.");
        quest.setCategory(QuestCategory.PROGRAMMING);
        quest.setType(QuestType.DAILY);
        quest.setXpValue(30);
        quest.setUser(user);
        return Optional.of(quest);
    }
}