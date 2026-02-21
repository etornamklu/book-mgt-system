package com.etornamklu.bookmgtsystem.controller;

import com.etornamklu.bookmgtsystem.dto.request.CreateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.request.UpdateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.response.ApiResponse;
import com.etornamklu.bookmgtsystem.model.Book;
import com.etornamklu.bookmgtsystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name="Books", description = "API for Book Management")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ApiResponse.success(bookService.findAllByPage(page,limit),"Books retrieved successfully");
    }

    @PostMapping
    @Operation(summary = "Create book")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Book> getBook(@Valid @RequestBody CreateBookRequestDto dto) {
        return ApiResponse.success(bookService.create(dto),"Book created successfully");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Book> getBook(@PathVariable UUID id) {
        return ApiResponse.success(bookService.findById(id),"Books retrieved successfully");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update Book")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Book> updateBook(@PathVariable UUID id, @Valid @RequestBody UpdateBookRequestDto dto) {
        return ApiResponse.success(bookService.update(id,dto),"Book updated successfully");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Book")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteBook(@PathVariable UUID id) {
        return ApiResponse.success(bookService.delete(id),"Book updated successfully");
    }

}
