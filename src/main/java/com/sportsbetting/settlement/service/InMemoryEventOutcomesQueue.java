package com.sportsbetting.settlement.service;

import com.sportsbetting.settlement.dto.EventOutcome;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * In-memory queue for event outcomes (replaces Kafka event-outcomes topic).
 */
@Component
public class InMemoryEventOutcomesQueue {

    private final BlockingQueue<EventOutcome> queue = new LinkedBlockingQueue<>();

    public void offer(EventOutcome eventOutcome) {
        queue.offer(eventOutcome);
    }

    public BlockingQueue<EventOutcome> getQueue() {
        return queue;
    }
}
