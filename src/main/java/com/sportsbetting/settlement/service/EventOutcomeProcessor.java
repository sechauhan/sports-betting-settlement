package com.sportsbetting.settlement.service;

import com.sportsbetting.settlement.dto.Bet;
import com.sportsbetting.settlement.enums.BetStatus;
import com.sportsbetting.settlement.dto.BetSettlementMessage;
import com.sportsbetting.settlement.dto.EventOutcome;
import com.sportsbetting.settlement.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Processes an event outcome: matches to pending bets and sends settlement messages.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeProcessor {

    private final BetRepository betRepository;
    private final BetSettlementProducer betSettlementProducer;

    public void process(EventOutcome eventOutcome) {
        try {
            log.info("Processing event outcome: eventId={}, eventName={}, winnerId={}",
                    eventOutcome.getEventId(), eventOutcome.getEventName(), eventOutcome.getEventWinnerId());

            List<Bet> pendingBets = betRepository.findByEventIdAndStatus(eventOutcome.getEventId(), BetStatus.PENDING);
            log.info("Found {} pending bet(s) for event {}", pendingBets.size(), eventOutcome.getEventId());

            String winningWinnerId = eventOutcome.getEventWinnerId();
            for (Bet bet : pendingBets) {
                try {
                    boolean won = winningWinnerId.equals(bet.getEventWinnerId());
                    BetSettlementMessage message = createBetMessageFromRequest(bet, won);
                    betSettlementProducer.send(message);
                } catch (Exception e) {
                    log.error("Failed to send settlement for bet: betId={}", bet != null ? bet.getBetId() : null, e);
                }
            }
        } catch (Exception e) {
            log.error("Failed to process event outcome: eventId={}", eventOutcome != null ? eventOutcome.getEventId() : null, e);
            throw new IllegalStateException("Failed to process event outcome", e);
        }
    }

    private BetSettlementMessage createBetMessageFromRequest(Bet bet, boolean won){
        return BetSettlementMessage.builder()
                .betId(bet.getBetId())
                .userId(bet.getUserId())
                .eventId(bet.getEventId())
                .eventMarketId(bet.getEventMarketId())
                .eventWinnerId(bet.getEventWinnerId())
                .betAmount(bet.getBetAmount())
                .won(won)
                .build();
    }
}
