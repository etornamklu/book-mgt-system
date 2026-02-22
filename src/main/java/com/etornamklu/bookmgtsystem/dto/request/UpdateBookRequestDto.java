package com.etornamklu.bookmgtsystem.dto.request;

import lombok.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for updating an existing Book.
 * <p>
 * All fields are optional. Only non-null fields will be used to update the book.
 * This DTO supports partial updates via API requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookRequestDto {

    /**
     * Updated title of the book.
     * Optional; if null, the title will remain unchanged.
     */
    private String title;

    /**
     * Updated author of the book.
     * Optional; if null, the author will remain unchanged.
     */
    private String author;

    /**
     * Updated International Standard Book Number (ISBN) of the book.
     * Optional; if null, the ISBN will remain unchanged.
     */
    private String isbn;

    /**
     * Updated price of the book.
     * Optional; if null, the price will remain unchanged.
     */
    private BigDecimal price;

    /**
     * Updated stock quantity of the book.
     * Optional; if null, the stock quantity will remain unchanged.
     */
    private Integer stockQuantity;

    /**
     * Updated cover image of the book stored as a byte array.
     * Optional; if null, the cover image will remain unchanged.
     */
    private byte[] coverImage;
}