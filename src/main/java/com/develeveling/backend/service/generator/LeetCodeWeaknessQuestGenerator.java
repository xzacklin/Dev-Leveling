package com.develeveling.backend.service.generator;

import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.model.QuestCategory;
import com.develeveling.backend.model.QuestType;
import com.develeveling.backend.repository.QuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LeetCodeWeaknessQuestGenerator implements QuestGenerator {

    @Autowired
    private QuestRepository questRepository;

    // Define the set possible LeetCode tags
    private static final List<String> LEETCODE_TAGS = List.of("ARRAY", "GRAPH", "DYNAMIC_PROGRAMMING", "TREES", "TWO_POINTERS");

    @Override
    public Optional<Quest> generate(User user) {
        // Get all completed LeetCode quests for the user.
        List<Quest> completedQuests = questRepository.findByUserAndCompletedIsTrue(user);

        // Count the frequency of each tag from completed quests.
        Map<String, Long> tagCounts = completedQuests.stream()
                .filter(q -> q.getCategory() == QuestCategory.INTERVIEW_PREP)
                .flatMap(q -> q.getTags().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Find the tag the user has practiced the least.
        String weakestTag = LEETCODE_TAGS.stream()
                .min(Comparator.comparing(tag -> tagCounts.getOrDefault(tag, 0L)))
                .orElse("GENERAL"); // Fallback tag

        // Create a personalized quest.
        Quest quest = new Quest();
        quest.setTitle("Targeted Practice: " + weakestTag);
        quest.setDescription("Focus on your weak points! Solve a problem tagged with " + weakestTag + ".");
        quest.setCategory(QuestCategory.INTERVIEW_PREP);
        quest.setType(QuestType.DAILY);
        quest.setXpValue(75);
        quest.setUser(user);
        quest.setTags(Set.of(weakestTag));

        return Optional.of(quest);
    }
}