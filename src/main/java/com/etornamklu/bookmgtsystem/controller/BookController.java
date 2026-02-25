package com.etornamklu.bookmgtsystem.controller;

import com.etornamklu.bookmgtsystem.dto.request.CreateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.request.UpdateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.response.ApiResponse;
import com.etornamklu.bookmgtsystem.dto.response.BookResponseDto;
import com.etornamklu.bookmgtsystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Books", description = "API for Book Management")
public class BookController {

    private final BookService bookService;

    /**
     * Retrieves a paginated list of all books.
     *
     * @param page  the page number to retrieve (default is 0)
     * @param limit the number of books per page (default is 20)
     * @return an ApiResponse containing a page of BookResponseDto objects
     */
    @GetMapping
    @Operation(
            summary = "Get all books",
            description = "Retrieve all books with pagination. The response contains paginated BookResponseDto objects.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Books retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)
                            )
                    )
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Page<BookResponseDto>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ApiResponse.success(bookService.findAllByPage(page, limit), "Books retrieved successfully");
    }

    /**
     * Creates a new book in the system.
     *
     * @param dto the CreateBookRequestDto containing book details
     * @return an ApiResponse containing the created BookResponseDto
     */
    @PostMapping
    @Operation(
            summary = "Create book",
            description = "Create a new book. Mandatory fields: title, price. Optional: coverImage (Base64 encoded image).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Book creation payload",
                    content = @Content(schema = @Schema(implementation = CreateBookRequestDto.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Book created successfully",
                            content = @Content(schema = @Schema(implementation = BookResponseDto.class))
                    )
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BookResponseDto> createBook(@Valid @RequestBody CreateBookRequestDto dto) {
        return ApiResponse.success(bookService.create(dto), "Book created successfully");
    }

    /**
     * Retrieves a single book by its ID.
     *
     * @param id the UUID of the book to retrieve
     * @return an ApiResponse containing the BookResponseDto
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get book by ID",
            description = "Retrieve a single book by its UUID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Book retrieved successfully",
                            content = @Content(schema = @Schema(implementation = BookResponseDto.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BookResponseDto> getBook(@PathVariable UUID id) {
        return ApiResponse.success(bookService.findById(id), "Book retrieved successfully");
    }

    /**
     * Updates an existing book by its ID.
     *
     * @param id  the UUID of the book to update
     * @param dto the UpdateBookRequestDto containing updated book details
     * @return an ApiResponse containing the updated BookResponseDto
     */
    @PatchMapping("/{id}")
    @Operation(
            summary = "Update book",
            description = "Update an existing book. All fields are optional. Only provided fields will be updated.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Book update payload",
                    content = @Content(schema = @Schema(implementation = UpdateBookRequestDto.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Book updated successfully",
                            content = @Content(schema = @Schema(implementation = BookResponseDto.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BookResponseDto> updateBook(@PathVariable UUID id, @Valid @RequestBody UpdateBookRequestDto dto) {
        return ApiResponse.success(bookService.update(id, dto), "Book updated successfully");
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the UUID of the book to delete
     * @return an ApiResponse indicating success (no content)
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete book",
            description = "Soft delete a book by setting its deletedAt field. Returns no content.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Book deleted successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteBook(@PathVariable UUID id) {
        return ApiResponse.success(bookService.delete(id), "Book deleted successfully");
    }

}