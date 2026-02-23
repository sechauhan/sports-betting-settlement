package com.sportsbetting.settlement.service;

import com.sportsbetting.settlement.config.KafkaProperties;
import com.sportsbetting.settlement.domain.EventOutcome;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Consumes event outcomes from the in-memory queue and processes each (match bets, send settlements).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MockEventOutcomeConsumer {

    private final InMemoryEventOutcomesQueue inMemoryEventOutcomesQueue;
    private final EventOutcomeProcessor eventOutcomeProcessor;
    private final KafkaProperties kafkaProperties;

    private volatile boolean running = true;
    private ExecutorService executor;

    @PostConstruct
    public void startConsumer() {
        try {
            String topic = kafkaProperties.getEventOutcomesTopic();
            log.info("Event-outcomes consumer listening to in-memory queue for topic '{}'", topic);
            executor = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "mock-event-outcome-consumer");
                t.setDaemon(false);
                return t;
            });
            executor.submit(this::drainQueue);
        } catch (Exception e) {
            log.error("Failed to start event-outcomes consumer", e);
            throw new IllegalStateException("Failed to start event-outcomes consumer", e);
        }
    }

    @PreDestroy
    public void stopConsumer() {
        running = false;
        if (executor != null) {
            try {
                executor.shutdown();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
                log.warn("Event-outcomes consumer interrupted during shutdown");
            } catch (Exception e) {
                log.error("Error stopping event-outcomes consumer", e);
            }
        }
    }

    private void drainQueue() {
        while (running) {
            try {
                EventOutcome eventOutcome = inMemoryEventOutcomesQueue.getQueue().poll(1, TimeUnit.SECONDS);
                if (eventOutcome != null) {
                    log.info("Consumed event outcome from in-memory queue: eventId={}", eventOutcome.getEventId());
                    eventOutcomeProcessor.process(eventOutcome);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error processing event outcome", e);
            }
        }
    }
}
