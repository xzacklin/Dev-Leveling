package com.develeveling.backend.service.generator;

import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.model.QuestCategory;
import com.develeveling.backend.model.QuestType;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
@Component
public class NetworkingQuestGenerator implements QuestGenerator {

    @Override
    public Optional<Quest> generate(User user) {
        Set<String> targetCompanies = user.getTargetCompanies();

        // If the user hasn't set any target companies, give them a quest to do so.
        if (targetCompanies == null || targetCompanies.isEmpty()) {
            Quest setupQuest = new Quest();
            setupQuest.setTitle("Set Your Career Goals");
            setupQuest.setDescription("Add at least 3 target companies to your profile to receive personalized networking quests.");
            setupQuest.setCategory(QuestCategory.NETWORKING);
            setupQuest.setType(QuestType.DAILY);
            setupQuest.setXpValue(10);
            setupQuest.setUser(user);
            return Optional.of(setupQuest);
        }

        // Otherwise, pick a random company from their list and create a targeted quest.
        List<String> companiesList = new ArrayList<>(targetCompanies);
        String randomCompany = companiesList.get(ThreadLocalRandom.current().nextInt(companiesList.size()));

        Quest specificQuest = new Quest();
        specificQuest.setTitle("Network at " + randomCompany);
        specificQuest.setDescription("Find an engineer or recruiter from " + randomCompany + " on LinkedIn and schedule a coffee chat.");
        specificQuest.setCategory(QuestCategory.NETWORKING);
        specificQuest.setType(QuestType.DAILY);
        specificQuest.setXpValue(10);
        specificQuest.setUser(user);
        specificQuest.getTags().add("COFFEE_CHAT");

        return Optional.of(specificQuest);
    }
}