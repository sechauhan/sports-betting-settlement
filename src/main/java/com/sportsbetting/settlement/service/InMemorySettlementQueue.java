package com.sportsbetting.settlement.service;

import com.sportsbetting.settlement.dto.BetSettlementMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * In-memory queue for bet settlements (replaces RocketMQ bet-settlements topic).
 */
@Component
public class InMemorySettlementQueue {

    private final BlockingQueue<BetSettlementMessage> queue = new LinkedBlockingQueue<>();

    public void offer(BetSettlementMessage message) {
        queue.offer(message);
    }

    public BlockingQueue<BetSettlementMessage> getQueue() {
        return queue;
    }
}
