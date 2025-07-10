package com.develeveling.backend.repository;

import com.develeveling.backend.entity.Quest;
import com.develeveling.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {

    List<Quest> findByUserAndCompletedIsTrue(User user);

    List<Quest> findByUserAndCompletedIsFalse(User user);

    @Query("SELECT count(q) FROM Quest q WHERE q.user = :user AND q.completed = true AND q.completedAt > :timestamp")
    long countCompletedQuestsAfter(@Param("user") User user, @Param("timestamp") ZonedDateTime timestamp);

}