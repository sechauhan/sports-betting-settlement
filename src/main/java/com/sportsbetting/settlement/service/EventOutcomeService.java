package com.sportsbetting.settlement.service;

import com.sportsbetting.settlement.dto.EventOutcomeRequest;
import com.sportsbetting.settlement.domain.EventOutcome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Business logic for event outcome operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventOutcomeService {

    private final EventOutcomeKafkaProducer eventOutcomeKafkaProducer;

    public void publishEventOutcome(EventOutcomeRequest request) {
        try {
            EventOutcome outcome = EventOutcome.builder()
                    .eventId(request.getEventId())
                    .eventName(request.getEventName())
                    .eventWinnerId(request.getEventWinnerId())
                    .build();
            eventOutcomeKafkaProducer.publish(outcome);
            log.info("Event outcome published to event-outcomes");
        } catch (Exception e) {
            log.error("Failed to publish event outcome: eventId={}", request != null ? request.getEventId() : null, e);
            throw new IllegalStateException("Failed to publish event outcome", e);
        }
    }
}
