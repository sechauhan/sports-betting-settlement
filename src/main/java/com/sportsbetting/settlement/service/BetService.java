package com.sportsbetting.settlement.service;

import com.sportsbetting.settlement.api.dto.BetRequest;
import com.sportsbetting.settlement.domain.Bet;
import com.sportsbetting.settlement.domain.BetStatus;
import com.sportsbetting.settlement.exception.BetNotFoundException;
import com.sportsbetting.settlement.exception.DuplicateBetException;
import com.sportsbetting.settlement.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for bet operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository betRepository;

    public Bet placeBet(BetRequest request) {
        try {
            if (betRepository.findByBetId(request.getBetId()).isPresent()) {
                throw new DuplicateBetException(request.getBetId());
            }
            Bet bet = createBet(request);
            return betRepository.save(bet);
        } catch (DuplicateBetException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to place bet: betId={}", request != null ? request.getBetId() : null, e);
            throw new IllegalStateException("Failed to place bet", e);
        }
    }

    public List<Bet> listBets(String eventId, BetStatus status) {
        try {
            if (eventId != null && status != null) {
                return betRepository.findByEventIdAndStatus(eventId, status);
            }
            if (eventId != null) {
                return betRepository.findAll().stream()
                        .filter(b -> b.getEventId().equals(eventId))
                        .collect(Collectors.toList());
            }
            return betRepository.findAll();
        } catch (Exception e) {
            log.error("Failed to list bets: eventId={}, status={}", eventId, status, e);
            return Collections.emptyList();
        }
    }

    public Bet getBet(String betId) {
        try {
            return betRepository.findByBetId(betId)
                    .orElseThrow(() -> new BetNotFoundException(betId));
        } catch (BetNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get bet: betId={}", betId, e);
            throw new IllegalStateException("Failed to get bet", e);
        }
    }

    private Bet createBet(BetRequest request){
        return Bet.builder()
                .betId(request.getBetId())
                .userId(request.getUserId())
                .eventId(request.getEventId())
                .eventMarketId(request.getEventMarketId())
                .eventWinnerId(request.getEventWinnerId())
                .betAmount(request.getBetAmount())
                .status(BetStatus.PENDING)
                .build();
    }
}
