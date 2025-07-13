package com.develeveling.backend.service.generator;

import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.TargetCompany;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.model.NetworkingStatus;
import com.develeveling.backend.model.QuestCategory;
import com.develeveling.backend.model.QuestType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class NetworkingQuestGenerator implements QuestGenerator {

    @Override
    public Optional<Quest> generate(User user) {
        // I'll find all target companies where a referral has NOT been secured yet.
        List<TargetCompany> availableTargets = user.getTargetCompanies().stream()
                .filter(tc -> tc.getStatus() != NetworkingStatus.REFERRAL_SECURED)
                .collect(Collectors.toList());

        if (availableTargets.isEmpty()) {
            return Optional.empty();
        }

        TargetCompany target = availableTargets.get(ThreadLocalRandom.current().nextInt(availableTargets.size()));

        Quest quest = new Quest();
        quest.setUser(user);
        quest.setTitle("Network at " + target.getCompanyName());
        quest.setDescription("I'll schedule a coffee chat with an engineer or recruiter from " + target.getCompanyName() + " this week.");
        quest.setCategory(QuestCategory.NETWORKING);
        quest.setType(QuestType.WEEKLY);
        quest.setXpValue(50);
        quest.getTags().add("COFFEE_CHAT");

        return Optional.of(quest);
    }
}