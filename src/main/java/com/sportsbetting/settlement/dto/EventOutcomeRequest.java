package com.sportsbetting.settlement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOutcomeRequest {

    @NotBlank(message = "Event ID is required")
    private String eventId;

    @NotBlank(message = "Event name is required")
    private String eventName;

    @NotBlank(message = "Event winner ID is required")
    private String eventWinnerId;
}
