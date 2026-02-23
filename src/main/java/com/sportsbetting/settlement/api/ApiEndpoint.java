package com.sportsbetting.settlement.api;

/**
 * API endpoint path constants.
 */
public final class ApiEndpoint {

    private ApiEndpoint() {
    }

    public static final String API_V1 = "/api/v1";

    // Bets
    public static final String BETS = API_V1 + "/bets";
    public static final String BETS_BY_ID = "/{betId}";

    // Event outcomes
    public static final String EVENT_OUTCOMES = API_V1 + "/event-outcomes";

    // Path variable names
    public static final String PATH_BET_ID = "betId";
}
