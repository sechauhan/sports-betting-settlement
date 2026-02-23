package com.sportsbetting.settlement.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result of publishing an event outcome to the event-outcomes queue.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOutcomePublishResult {

    private String message;
    private String eventId;
    private String topic;
}
