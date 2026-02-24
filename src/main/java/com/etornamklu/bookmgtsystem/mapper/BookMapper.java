package com.etornamklu.bookmgtsystem.mapper;

import com.etornamklu.bookmgtsystem.dto.response.BookResponseDto;
import com.etornamklu.bookmgtsystem.model.Book;

/**
 * Mapper class for converting {@link Book} entities to {@link BookResponseDto} objects.
 * <p>
 * This class centralizes the mapping logic to ensure consistent DTO representations
 * across the API.
 */
public class BookMapper {

    /**
     * Converts a {@link Book} entity to a {@link BookResponseDto}.
     *
     * @param book the {@link Book} entity to convert
     * @return a {@link BookResponseDto} containing the same data as the entity
     */
    public static BookResponseDto toDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .price(book.getPrice())
                .coverImage(book.getCoverImage())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}