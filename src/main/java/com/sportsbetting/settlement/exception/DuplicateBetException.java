package com.sportsbetting.settlement.exception;

/**
 * Thrown when placing a bet with a betId that already exists.
 */
public class DuplicateBetException extends RuntimeException {

    public DuplicateBetException(String betId) {
        super("Bet already exists with betId: " + betId);
    }
}
