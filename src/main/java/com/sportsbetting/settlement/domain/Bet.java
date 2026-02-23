package com.sportsbetting.settlement.domain;

import com.sportsbetting.settlement.enums.BetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * A bet stored in memory (HashMap), to be matched and settled based on event outcomes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bet {

    private String betId;
    private String userId;
    private String eventId;
    private String eventMarketId;
    private String eventWinnerId;
    private BigDecimal betAmount;

    @Builder.Default
    private BetStatus status = BetStatus.PENDING;
}
