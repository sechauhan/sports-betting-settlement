package com.sportsbetting.settlement.exception;

/**
 * Thrown when a bet is not found by ID.
 */
public class BetNotFoundException extends RuntimeException {

    public BetNotFoundException(String betId) {
        super("Bet not found: " + betId);
    }
}
