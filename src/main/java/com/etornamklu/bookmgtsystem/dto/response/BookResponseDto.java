package com.etornamklu.bookmgtsystem.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a Book for API responses.
 * <p>
 * Contains all relevant book information returned to clients,
 * including metadata like creation and update timestamps.
 */
@Data
@Builder
public class BookResponseDto {

    /**
     * Unique identifier of the book.
     */
    private UUID id;

    /**
     * Title of the book.
     */
    private String title;

    /**
     * Price of the book in local currency.
     */
    private BigDecimal price;

    /**
     * Timestamp when the book was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the book was last updated.
     */
    private LocalDateTime updatedAt;
}