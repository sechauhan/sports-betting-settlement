package com.sportsbetting.settlement.service;

import com.sportsbetting.settlement.dto.Bet;
import com.sportsbetting.settlement.enums.BetStatus;
import com.sportsbetting.settlement.dto.BetSettlementMessage;
import com.sportsbetting.settlement.dto.EventOutcome;
import com.sportsbetting.settlement.repository.BetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class EventOutcomeKafkaConsumerTest {

    @Autowired
    private BetRepository betRepository;

    @SpyBean
    private BetSettlementProducer betSettlementProducer;

    @Autowired
    private EventOutcomeProcessor eventOutcomeProcessor;

    @BeforeEach
    void setUp() {
        betRepository.deleteAll();
    }

    @Test
    void whenEventOutcomeProcessed_matchingBetsTriggerSettlement() {
        Bet bet = Bet.builder()
                .betId("bet-1")
                .userId("user-1")
                .eventId("evt-1")
                .eventMarketId("market-1")
                .eventWinnerId("winner-1")
                .betAmount(new BigDecimal("100"))
                .status(BetStatus.PENDING)
                .build();
        betRepository.save(bet);

        EventOutcome outcome = EventOutcome.builder()
                .eventId("evt-1")
                .eventName("Team A vs Team B")
                .eventWinnerId("winner-1")
                .build();

        eventOutcomeProcessor.process(outcome);

        verify(betSettlementProducer).send(argThat((BetSettlementMessage m) ->
                m.getBetId().equals("bet-1") && m.isWon()));
    }

    @Test
    void whenEventOutcomeProcessed_noMatchingBets_noSettlementSent() {
        EventOutcome outcome = EventOutcome.builder()
                .eventId("evt-99")
                .eventName("Other Match")
                .eventWinnerId("winner-x")
                .build();

        eventOutcomeProcessor.process(outcome);

        verify(betSettlementProducer, never()).send(any());
    }

    @Test
    void whenEventOutcomeProcessed_losingBet_sendsSettlementWithWonFalse() {
        Bet bet = Bet.builder()
                .betId("bet-lose")
                .userId("user-2")
                .eventId("evt-2")
                .eventMarketId("market-2")
                .eventWinnerId("loser-id")
                .betAmount(new BigDecimal("50"))
                .status(BetStatus.PENDING)
                .build();
        betRepository.save(bet);

        EventOutcome outcome = EventOutcome.builder()
                .eventId("evt-2")
                .eventName("Match 2")
                .eventWinnerId("winner-id")
                .build();

        eventOutcomeProcessor.process(outcome);

        verify(betSettlementProducer).send(argThat((BetSettlementMessage m) ->
                m.getBetId().equals("bet-lose") && !m.isWon()));
    }
}
