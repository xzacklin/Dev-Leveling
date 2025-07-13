package com.develeveling.backend.service;

import com.develeveling.backend.dto.NetworkingEventDto;
import com.develeveling.backend.entity.NetworkingEvent;
import com.develeveling.backend.entity.User;
import com.develeveling.backend.model.NetworkingEventType;
import com.develeveling.backend.model.NetworkingStatus;
import com.develeveling.backend.repository.NetworkingEventRepository;
import com.develeveling.backend.repository.TargetCompanyRepository;
import com.develeveling.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NetworkingService {

    private final NetworkingEventRepository eventRepository;
    private final UserRepository userRepository;
    private final TargetCompanyRepository targetCompanyRepository;

    @Transactional
    public NetworkingEventDto logEvent(Long userId, NetworkingEventDto eventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("I can't find a user with ID: " + userId));

        NetworkingEvent event = new NetworkingEvent();
        event.setUser(user);
        event.setEventType(eventDto.eventType());
        event.setContactName(eventDto.contactName());
        event.setCompany(eventDto.company());
        event.setEventDate(eventDto.eventDate());
        event.setNotes(eventDto.notes());

        targetCompanyRepository.findByUserAndCompanyNameIgnoreCase(user, event.getCompany())
                .ifPresent(target -> {
                    if (event.getEventType() == NetworkingEventType.REFERRAL_SECURED) {
                        target.setStatus(NetworkingStatus.REFERRAL_SECURED);
                    } else if (event.getEventType() == NetworkingEventType.COFFEE_CHAT) {
                        if (target.getStatus() != NetworkingStatus.REFERRAL_SECURED) {
                            target.setStatus(NetworkingStatus.COFFEE_CHAT_COMPLETED);
                        }
                    }
                });

        NetworkingEvent savedEvent = eventRepository.save(event);
        return toDto(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<NetworkingEventDto> getEventsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("I can't find a user with ID: " + userId));

        return eventRepository.findByUserOrderByEventDateDesc(user)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private NetworkingEventDto toDto(NetworkingEvent event) {
        return new NetworkingEventDto(
                event.getId(),
                event.getEventType(),
                event.getContactName(),
                event.getCompany(),
                event.getEventDate(),
                event.getNotes()
        );
    }
}