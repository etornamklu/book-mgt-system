package com.etornamklu.bookmgtsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for creating a new Book.
 * <p>
 * This DTO is used to capture user input when creating a book via API requests.
 * Includes validation annotations to enforce required fields and constraints.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequestDto {

    /**
     * Title of the book.
     * Must not be blank and cannot exceed 150 characters.
     */
    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;

    /**
     * Author of the book.
     * Must not be blank and cannot exceed 100 characters.
     */
    @NotBlank(message = "Author is required")
    @Size(max = 100, message = "Author must not exceed 100 characters")
    private String author;

    /**
     * International Standard Book Number (ISBN) of the book.
     * Optional field; cannot exceed 20 characters if provided.
     */
    @Size(max = 20, message = "ISBN must not exceed 20 characters")
    private String isbn;

    /**
     * Price of the book.
     * Must be a positive value greater than 0.
     */
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    /**
     * Quantity of stock available for this book.
     * Must be zero or a positive value.
     */
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    /**
     * Optional cover image of the book stored as a byte array.
     */
    private byte[] coverImage;
}