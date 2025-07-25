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
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class QuestService {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private UserRepository userRepository;

    private static final int CUSTOM_QUEST_XP_VALUE = 5;

    public Quest createQuestForUser(Long userId, QuestDto questDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Quest quest = new Quest();
        quest.setTitle(questDto.title());
        quest.setDescription(questDto.description());
        quest.setXpValue(CUSTOM_QUEST_XP_VALUE);
        quest.setCategory(QuestCategory.valueOf(questDto.category().toUpperCase()));
        quest.setType(QuestType.valueOf(questDto.type().toUpperCase()));
        quest.setUser(user);

        return questRepository.save(quest);
    }

    public Quest completeQuest(Long questId) {
        try {
            Quest quest = questRepository.findById(questId)
                    .orElseThrow(() -> new EntityNotFoundException("Quest not found with id: " + questId));

            if (quest.isCompleted()) {
                throw new IllegalStateException("Quest with id: " + questId + " is already completed.");
            }

            quest.setCompleted(true);

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

            user.setTotalXp(user.getTotalXp() + xpToAdd);

            userRepository.save(user);
            return questRepository.save(quest);

        } catch (ObjectOptimisticLockingFailureException e) {
            throw new IllegalStateException("Quest with id: " + questId + " was completed by another process.", e);
        }
    }
}