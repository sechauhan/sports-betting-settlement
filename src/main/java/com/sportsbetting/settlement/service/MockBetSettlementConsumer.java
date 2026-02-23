package com.sportsbetting.settlement.service;

import com.sportsbetting.settlement.config.RocketMQProperties;
import com.sportsbetting.settlement.domain.Bet;
import com.sportsbetting.settlement.enums.BetStatus;
import com.sportsbetting.settlement.domain.BetSettlementMessage;
import com.sportsbetting.settlement.repository.BetRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Consumes bet settlements from the in-memory queue and marks bets as SETTLED.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MockBetSettlementConsumer {

    private final InMemorySettlementQueue inMemorySettlementQueue;
    private final BetRepository betRepository;
    private final RocketMQProperties rocketMQProperties;

    private volatile boolean running = true;
    private ExecutorService executor;

    @PostConstruct
    public void startConsumer() {
        try {
            String topic = rocketMQProperties.getBetSettlementsTopic();
            log.info("Bet-settlements consumer listening to in-memory queue for topic '{}'", topic);
            executor = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "mock-bet-settlement-consumer");
                t.setDaemon(false);
                return t;
            });
            executor.submit(this::drainQueue);
        } catch (Exception e) {
            log.error("Failed to start bet-settlements consumer", e);
            throw new IllegalStateException("Failed to start bet-settlements consumer", e);
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
                log.warn("Bet-settlements consumer interrupted during shutdown");
            } catch (Exception e) {
                log.error("Error stopping bet-settlements consumer", e);
            }
        }
    }

    private void drainQueue() {
        while (running) {
            try {
                BetSettlementMessage message = inMemorySettlementQueue.getQueue().poll(1, TimeUnit.SECONDS);
                if (message != null) {
                    settleBet(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error processing settlement message", e);
            }
        }
    }

    private void settleBet(BetSettlementMessage message) {
        try {
            log.info("Consumed bet settlement (topic '{}'): betId={}, won={}",
                    rocketMQProperties.getBetSettlementsTopic(), message.getBetId(), message.isWon());

            Optional<Bet> optBet = betRepository.findByBetId(message.getBetId());
            if (optBet.isPresent()) {
                Bet bet = optBet.get();
                bet.setStatus(BetStatus.SETTLED);
                betRepository.save(bet);
                log.info("Settled bet: betId={}, status=SETTLED", bet.getBetId());
            } else {
                log.warn("Bet not found for settlement: betId={}", message.getBetId());
            }
        } catch (Exception e) {
            log.error("Failed to settle bet: betId={}", message != null ? message.getBetId() : null, e);
        }
    }
}
