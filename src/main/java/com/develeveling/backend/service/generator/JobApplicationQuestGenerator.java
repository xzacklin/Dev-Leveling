package com.develeveling.backend.service.generator;

import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.model.QuestCategory;
import com.develeveling.backend.model.QuestType;
import org.springframework.stereotype.Component;
import java.util.ArrayList; // <-- Add import
import java.util.List;     // <-- Add import
import java.util.Optional; // <-- Add import
import java.util.Set;      // <-- Add import
import java.util.concurrent.ThreadLocalRandom; // <-- Add import

@Component
public class JobApplicationQuestGenerator implements QuestGenerator {

    @Override
    public Optional<Quest> generate(User user) {
        Set<String> targetRoles = user.getTargetRoles();

        // If the user hasn't set any target roles, prompt them to do so.
        if (targetRoles == null || targetRoles.isEmpty()) {
            Quest setupQuest = new Quest();
            setupQuest.setTitle("Define Your Job Search");
            setupQuest.setDescription("Add at least 2 target roles to your profile (e.g., 'Backend Engineer', 'Java Developer') to receive personalized application quests.");
            setupQuest.setCategory(QuestCategory.APPLICATIONS);
            setupQuest.setType(QuestType.DAILY);
            setupQuest.setXpValue(10);
            setupQuest.setUser(user);
            return Optional.of(setupQuest);
        }

        // Otherwise, pick a random role from their list and create a targeted quest.
        List<String> rolesList = new ArrayList<>(targetRoles);
        String randomRole = rolesList.get(ThreadLocalRandom.current().nextInt(rolesList.size()));

        Quest specificQuest = new Quest();
        specificQuest.setTitle("Apply for a " + randomRole + " position");
        specificQuest.setDescription("Find a job posting for a " + randomRole + " that excites you and submit your application.");
        specificQuest.setCategory(QuestCategory.APPLICATIONS);
        specificQuest.setType(QuestType.DAILY);
        specificQuest.setXpValue(70);
        specificQuest.setUser(user);
        specificQuest.getTags().add("APPLICATION_SUBMITTED");

        return Optional.of(specificQuest);
    }
}