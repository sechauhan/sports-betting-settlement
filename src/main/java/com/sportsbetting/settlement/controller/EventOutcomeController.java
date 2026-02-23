package com.sportsbetting.settlement.controller;

import com.sportsbetting.settlement.dto.EventOutcomeRequest;
import com.sportsbetting.settlement.dto.Response;
import com.sportsbetting.settlement.constants.ApiEndpoint;
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
    public ResponseEntity<Object> publishEventOutcome(@Valid @RequestBody EventOutcomeRequest request) {
        eventOutcomeService.publishEventOutcome(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Response.success(HttpStatus.ACCEPTED.value()));
    }
}
