package com.etornamklu.bookmgtsystem.controller;

import com.etornamklu.bookmgtsystem.dto.request.CreateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.request.UpdateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.response.ApiResponse;
import com.etornamklu.bookmgtsystem.dto.response.BookResponseDto;
import com.etornamklu.bookmgtsystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing books in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting books.
 */
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name="Books", description = "API for Book Management")
public class BookController {

    private final BookService bookService;

    /**
     * Retrieves a paginated list of all books.
     *
     * @param page the page number to retrieve (default is 0)
     * @param limit the number of books per page (default is 20)
     * @return an ApiResponse containing a page of BookResponseDto objects
     */
    @GetMapping
    @Operation(summary = "Get all books")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Page<BookResponseDto>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ApiResponse.success(bookService.findAllByPage(page,limit),"Books retrieved successfully");
    }

    /**
     * Creates a new book in the system.
     *
     * @param dto the CreateBookRequestDto containing book details
     * @return an ApiResponse containing the created BookResponseDto
     */
    @PostMapping
    @Operation(summary = "Create book")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BookResponseDto> getBook(@Valid @RequestBody CreateBookRequestDto dto) {
        return ApiResponse.success(bookService.create(dto),"Book created successfully");
    }

    /**
     * Retrieves a single book by its ID.
     *
     * @param id the UUID of the book to retrieve
     * @return an ApiResponse containing the BookResponseDto
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get book")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BookResponseDto> getBook(@PathVariable UUID id) {
        return ApiResponse.success(bookService.findById(id),"Books retrieved successfully");
    }

    /**
     * Updates an existing book by its ID.
     *
     * @param id the UUID of the book to update
     * @param dto the UpdateBookRequestDto containing updated book details
     * @return an ApiResponse containing the updated BookResponseDto
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Update Book")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BookResponseDto> updateBook(@PathVariable UUID id, @Valid @RequestBody UpdateBookRequestDto dto) {
        return ApiResponse.success(bookService.update(id,dto),"Book updated successfully");
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the UUID of the book to delete
     * @return an ApiResponse indicating success (no content)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Book")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteBook(@PathVariable UUID id) {
        return ApiResponse.success(bookService.delete(id),"Book deleted successfully");
    }

}