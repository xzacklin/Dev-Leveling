package com.develeveling.backend.repository;

import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {

    List<Quest> findByUserAndCompletedIsTrue(User user);

    List<Quest> findByUserAndCompletedIsFalse(User user);

}