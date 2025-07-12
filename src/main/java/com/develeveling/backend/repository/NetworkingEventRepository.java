package com.develeveling.backend.repository;

import com.develeveling.backend.entity.NetworkingEvent;
import com.develeveling.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetworkingEventRepository extends JpaRepository<NetworkingEvent, Long> {
    List<NetworkingEvent> findByUserOrderByEventDateDesc(User user);
}