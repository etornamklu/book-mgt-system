package com.etornamklu.bookmgtsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper used for all REST endpoints.
 * <p>
 * Encapsulates success status, response data, messages, and optional error details.
 *
 * @param <T> the type of the response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Indicates whether the request was successful.
     */
    private Boolean success;

    /**
     * The data returned by the API if the request was successful.
     */
    private T data;

    /**
     * A human-readable message describing the outcome of the request.
     */
    private String message;

    /**
     * Detailed information about an error if the request failed.
     */
    private Object error;

    /**
     * Creates a successful {@link ApiResponse} with the given data and message.
     *
     * @param data    the response data
     * @param message a message describing the success
     * @param <T>     the type of the response data
     * @return an ApiResponse indicating success
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    /**
     * Creates an error {@link ApiResponse} with the given message and details.
     *
     * @param message a message describing the error
     * @param details additional error details (optional)
     * @param <T>     the type of the response data (null in error case)
     * @return an ApiResponse indicating failure
     */
    public static <T> ApiResponse<T> error(String message, Object details) {
        return new ApiResponse<>(false, null, message, details);
    }
}