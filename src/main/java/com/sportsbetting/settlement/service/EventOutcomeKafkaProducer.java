package com.sportsbetting.settlement.service;

import com.sportsbetting.settlement.config.KafkaProperties;
import com.sportsbetting.settlement.domain.EventOutcome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Publishes event outcomes to the in-memory event-outcomes queue (mock only; no real Kafka).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeKafkaProducer {

    private final KafkaProperties kafkaProperties;
    private final InMemoryEventOutcomesQueue inMemoryEventOutcomesQueue;

    public void publish(EventOutcome eventOutcome) {
        try {
            String topic = kafkaProperties.getEventOutcomesTopic();
            inMemoryEventOutcomesQueue.offer(eventOutcome);
            log.info("Event outcome published to in-memory queue (topic '{}'): eventId={}", topic, eventOutcome.getEventId());
        } catch (Exception e) {
            log.error("Failed to publish event outcome to queue: eventId={}", eventOutcome != null ? eventOutcome.getEventId() : null, e);
            throw new IllegalStateException("Failed to publish event outcome", e);
        }
    }
}
