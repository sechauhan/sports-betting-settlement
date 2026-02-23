package com.sportsbetting.settlement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents a sports event outcome published to Kafka.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOutcome implements Serializable {

    private static final long serialVersionUID = 1L;

    private String eventId;
    private String eventName;
    private String eventWinnerId;
}
