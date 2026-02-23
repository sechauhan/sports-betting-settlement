package com.sportsbetting.settlement.repository.impl;

import com.sportsbetting.settlement.domain.Bet;
import com.sportsbetting.settlement.enums.BetStatus;
import com.sportsbetting.settlement.repository.BetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory bet store using a HashMap (ConcurrentHashMap for thread safety).
 */
@Slf4j
@Repository
public class InMemoryBetRepository implements BetRepository {

    private final ConcurrentHashMap<String, Bet> store = new ConcurrentHashMap<>();

    @Override
    public Bet save(Bet bet) {
        try {
            if (bet.getStatus() == null) {
                bet.setStatus(BetStatus.PENDING);
            }
            store.put(bet.getBetId(), bet);
            return bet;
        } catch (Exception e) {
            log.error("Failed to save bet: betId={}", bet != null ? bet.getBetId() : null, e);
            throw new IllegalStateException("Failed to save bet", e);
        }
    }

    @Override
    public Optional<Bet> findByBetId(String betId) {
        try {
            return Optional.ofNullable(store.get(betId));
        } catch (Exception e) {
            log.error("Failed to find bet by id: betId={}", betId, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Bet> findByEventIdAndStatus(String eventId, BetStatus status) {
        try {
            return store.values().stream()
                    .filter(b -> eventId.equals(b.getEventId()) && status == b.getStatus())
                    .toList();
        } catch (Exception e) {
            log.error("Failed to find bets by eventId and status: eventId={}, status={}", eventId, status, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Bet> findAll() {
        try {
            return new ArrayList<>(store.values());
        } catch (Exception e) {
            log.error("Failed to find all bets", e);
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteAll() {
        try {
            store.clear();
        } catch (Exception e) {
            log.error("Failed to delete all bets", e);
            throw new IllegalStateException("Failed to delete all bets", e);
        }
    }
}
