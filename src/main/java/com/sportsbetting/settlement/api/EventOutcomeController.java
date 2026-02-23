package com.sportsbetting.settlement.api;

import com.sportsbetting.settlement.api.dto.EventOutcomePublishResult;
import com.sportsbetting.settlement.api.dto.EventOutcomeRequest;
import com.sportsbetting.settlement.api.dto.Response;
import com.sportsbetting.settlement.service.EventOutcomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for event outcomes. Delegates to EventOutcomeService and maps result to Response.
 */
@RestController
@RequestMapping(ApiEndpoint.EVENT_OUTCOMES)
@RequiredArgsConstructor
public class EventOutcomeController {

    private final EventOutcomeService eventOutcomeService;

    @PostMapping
    public ResponseEntity<Response<EventOutcomePublishResult>> publishEventOutcome(@Valid @RequestBody EventOutcomeRequest request) {
        eventOutcomeService.publishEventOutcome(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Response.success(HttpStatus.ACCEPTED.value()));
    }
}
