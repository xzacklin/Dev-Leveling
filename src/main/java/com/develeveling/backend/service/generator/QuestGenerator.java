package com.develeveling.backend.service.generator;

import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import java.util.Optional;

public interface QuestGenerator {
    Optional<Quest> generate(User user);
}