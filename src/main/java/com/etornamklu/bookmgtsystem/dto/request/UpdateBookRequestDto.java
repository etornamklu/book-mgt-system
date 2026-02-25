package com.etornamklu.bookmgtsystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
     * Updated price of the book.
     * Optional; if null, the price will remain unchanged.
     */
    private BigDecimal price;

    /**
     * Updated cover image of the book stored as a byte array.
     * Optional; if null, the cover image will remain unchanged.
     */
    @Schema(
            description = "Base64 encoded cover image",
            example = "/9j/4AAQSkZJRgABAQAAAQABAAD..."
    )
    private String coverImage;
}