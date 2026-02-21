package com.etornamklu.bookmgtsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 100, message = "Author must not exceed 100 characters")
    private String author;

    @Size(max = 20, message = "ISBN must not exceed 20 characters")
    private String isbn;

    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
}