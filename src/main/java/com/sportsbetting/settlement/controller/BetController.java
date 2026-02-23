package com.sportsbetting.settlement.controller;

import com.sportsbetting.settlement.dto.BetRequest;
import com.sportsbetting.settlement.dto.Response;
import com.sportsbetting.settlement.constants.ApiEndpoint;
import com.sportsbetting.settlement.domain.Bet;
import com.sportsbetting.settlement.enums.BetStatus;
import com.sportsbetting.settlement.service.BetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for bets. Delegates to BetService and maps results to Response.
 */
@RestController
@RequestMapping(ApiEndpoint.BETS)
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;

    @PostMapping
    public ResponseEntity<Object> placeBet(@Valid @RequestBody BetRequest request) {
        Bet bet = betService.placeBet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(HttpStatus.CREATED.value(), bet));
    }

    @GetMapping
    public ResponseEntity<Object> listBets(
            @RequestParam(required = false) String eventId,
            @RequestParam(required = false) BetStatus status) {
        List<Bet> bets = betService.listBets(eventId, status);
        return ResponseEntity.ok(Response.success(HttpStatus.OK.value(), bets));
    }

    @GetMapping(ApiEndpoint.BETS_BY_ID)
    public ResponseEntity<Object> getBet(@PathVariable(ApiEndpoint.PATH_BET_ID) String betId) {
        Bet bet = betService.getBet(betId);
        return ResponseEntity.ok(Response.success(HttpStatus.OK.value(), bet));
    }
}
