package com.develeveling.backend.controller;

import com.develeveling.backend.dto.NetworkingEventDto;
import com.develeveling.backend.service.NetworkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/networking-events")
@RequiredArgsConstructor
public class NetworkingController {

    private final NetworkingService networkingService;

    @PostMapping
    public ResponseEntity<NetworkingEventDto> logNetworkingEvent(@PathVariable Long userId, @RequestBody NetworkingEventDto eventDto) {
        NetworkingEventDto createdEvent = networkingService.logEvent(userId, eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @GetMapping
    public ResponseEntity<List<NetworkingEventDto>> getNetworkingEvents(@PathVariable Long userId) {
        List<NetworkingEventDto> events = networkingService.getEventsForUser(userId);
        return ResponseEntity.ok(events);
    }
}