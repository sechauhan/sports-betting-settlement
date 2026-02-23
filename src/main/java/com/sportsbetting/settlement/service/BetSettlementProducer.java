package com.sportsbetting.settlement.service;

import com.sportsbetting.settlement.config.RocketMQProperties;
import com.sportsbetting.settlement.dto.BetSettlementMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Sends bet settlement messages to the bet-settlements in-memory queue (mock only; no real RocketMQ).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BetSettlementProducer {

    private final RocketMQProperties rocketMQProperties;
    private final InMemorySettlementQueue inMemorySettlementQueue;

    public void send(BetSettlementMessage message) {
        try {
            String topic = rocketMQProperties.getBetSettlementsTopic();
            inMemorySettlementQueue.offer(message);
            log.info("Bet settlement sent to in-memory queue (topic '{}'): betId={}", topic, message.getBetId());
        } catch (Exception e) {
            log.error("Failed to send bet settlement to queue: betId={}", message != null ? message.getBetId() : null, e);
            throw new IllegalStateException("Failed to send bet settlement", e);
        }
    }
}
