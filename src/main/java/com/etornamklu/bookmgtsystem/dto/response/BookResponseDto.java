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
     * Author of the book.
     */
    private String author;

    /**
     * International Standard Book Number (ISBN) of the book.
     */
    private String isbn;

    /**
     * Price of the book in local currency.
     */
    private BigDecimal price;

    /**
     * Quantity of books available in stock.
     */
    private Integer stockQuantity;

    /**
     * Cover image of the book stored as a byte array.
     */
    private byte[] coverImage;

    /**
     * Timestamp when the book was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the book was last updated.
     */
    private LocalDateTime updatedAt;
}