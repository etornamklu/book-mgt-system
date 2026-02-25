package com.etornamklu.bookmgtsystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
     * Price of the book.
     * Must be a positive value greater than 0.
     */
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;


    /**
     * Optional cover image of the book stored as a byte array.
     */
    @Schema(
            description = "Base64 encoded cover image",
            example = "/9j/4AAQSkZJRgABAQAAAQABAAD..."
    )
    private String coverImage;
}