package com.sportsbetting.settlement.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Common API response wrapper for success and error responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private boolean success;
    private int statusCode;
    private T body;
    private List<Error> errors;

    /**
     * Constructor with only success flag and status code (body and errors remain null).
     */

     public static <T> Response<T> success(int statusCode) {
        return Response.<T>builder()
                .success(true)
                .statusCode(statusCode)
                .errors(null)
                .build();
    }

    public static <T> Response<T> success(int statusCode, T body) {
        return Response.<T>builder()
                .success(true)
                .statusCode(statusCode)
                .body(body)
                .errors(null)
                .build();
    }

    public static <T> Response<T> failure(int statusCode, List<Error> errors) {
        return Response.<T>builder()
                .success(false)
                .statusCode(statusCode)
                .body(null)
                .errors(errors != null ? errors : Collections.emptyList())
                .build();
    }

    public static <T> Response<T> failure(int statusCode, String message, String code) {
        return failure(statusCode, List.of(Error.builder().message(message).code(code).build()));
    }

    public static <T> Response<T> failure(int statusCode, String message, String code, String field) {
        return failure(statusCode, List.of(Error.builder().message(message).code(code).field(field).build()));
    }
}
