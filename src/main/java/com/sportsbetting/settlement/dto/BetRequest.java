package com.sportsbetting.settlement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetRequest {

    @NotBlank(message = "Bet ID is required")
    private String betId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Event ID is required")
    private String eventId;

    @NotBlank(message = "Event market ID is required")
    private String eventMarketId;

    @NotBlank(message = "Event winner ID is required")
    private String eventWinnerId;

    @NotNull(message = "Bet amount is required")
    @DecimalMin(value = "0.01", message = "Bet amount must be positive")
    private BigDecimal betAmount;
}
