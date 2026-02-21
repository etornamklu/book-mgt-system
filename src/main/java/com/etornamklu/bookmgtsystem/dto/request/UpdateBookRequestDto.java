package com.etornamklu.bookmgtsystem.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookRequestDto {

    private String title;

    private String author;

    private String isbn;

    private BigDecimal price;

    private Integer stockQuantity;
}