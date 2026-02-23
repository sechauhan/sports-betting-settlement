package com.sportsbetting.settlement.repository;

import com.sportsbetting.settlement.domain.Bet;
import com.sportsbetting.settlement.domain.BetStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository for bets. Implemented with an in-memory HashMap.
 */
public interface BetRepository {

    Bet save(Bet bet);

    Optional<Bet> findByBetId(String betId);

    List<Bet> findByEventIdAndStatus(String eventId, BetStatus status);

    List<Bet> findAll();

    void deleteAll();
}
