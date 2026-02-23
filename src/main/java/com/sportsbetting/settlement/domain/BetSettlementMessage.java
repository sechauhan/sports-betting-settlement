package com.sportsbetting.settlement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Message sent to the bet-settlements in-memory queue for settling a bet.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetSettlementMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String betId;
    private String userId;
    private String eventId;
    private String eventMarketId;
    private String eventWinnerId;
    private BigDecimal betAmount;
    private boolean won;
}
