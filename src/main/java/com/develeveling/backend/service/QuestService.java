package com.develeveling.backend.service;

import com.develeveling.backend.dto.QuestDto;
import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.model.QuestCategory;
import com.develeveling.backend.model.QuestType;
import com.develeveling.backend.repository.QuestRepository;
import com.develeveling.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestService {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private UserRepository userRepository;

    public Quest createQuestForUser(Long userId, QuestDto questDto) {
        //Find the user who this quest will belong to.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Create a new Quest entity from the DTO data.
        Quest quest = new Quest();
        quest.setTitle(questDto.title());
        quest.setDescription(questDto.description());
        quest.setXpValue(questDto.xpValue());

        // Convert the string from the DTO into our Enum types
        quest.setCategory(QuestCategory.valueOf(questDto.category().toUpperCase()));
        quest.setType(QuestType.valueOf(questDto.type().toUpperCase()));

        // Link the quest to the user
        quest.setUser(user);

        // Save the new quest to the database
        return questRepository.save(quest);
    }

    public Quest completeQuest(Long questId) {
        //Find the quest by its ID.
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new EntityNotFoundException("Quest not found with id: " + questId));

        //  Check if the quest is already completed to prevent duplicate XP.
        if (quest.isCompleted()) {
            throw new IllegalStateException("Quest with id: " + questId + " is already completed.");
        }

        //Mark the quest as completed.
        quest.setCompleted(true);

        //Get the user and award XP based on the quest's category.
        User user = quest.getUser();
        int xpToAdd = quest.getXpValue();
        QuestCategory category = quest.getCategory();

        switch (category) {
            case PROGRAMMING:
                user.setProgrammingXp(user.getProgrammingXp() + xpToAdd);
                break;
            case INTERVIEW_PREP:
                user.setLeetcodeXp(user.getLeetcodeXp() + xpToAdd);
                break;
            case APPLICATIONS:
                user.setInitiativeXp(user.getInitiativeXp() + xpToAdd);
                break;
            case NETWORKING:
                user.setNetworkingXp(user.getNetworkingXp() + xpToAdd);
                break;
        }

        userRepository.save(user);
        return questRepository.save(quest);
    }
}