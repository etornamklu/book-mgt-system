package com.etornamklu.bookmgtsystem.controller;

import com.etornamklu.bookmgtsystem.config.TestWebConfig;
import com.etornamklu.bookmgtsystem.dto.request.CreateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.request.UpdateBookRequestDto;
import com.etornamklu.bookmgtsystem.exception.GlobalExceptionHandler;
import com.etornamklu.bookmgtsystem.exception.ResourceNotFoundException;
import com.etornamklu.bookmgtsystem.model.Book;
import com.etornamklu.bookmgtsystem.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class, excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})
@Import({GlobalExceptionHandler.class, TestWebConfig.class})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private UUID bookId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        book = Book.builder()
                .id(bookId)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .price(new BigDecimal("39.99"))
                .stockQuantity(10)
                .build();
    }

    // -------------------------
    // GET /api/v1/books
    // -------------------------

    @Test
    void getAllBooks_shouldReturn200WithPageOfBooks() throws Exception {
        Page<Book> page = new PageImpl<>(List.of(book));
        when(bookService.findAllByPage(0, 20)).thenReturn(page);

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Books retrieved successfully"))
                .andExpect(jsonPath("$.data.content[0].title").value("Clean Code"))
                .andExpect(jsonPath("$.data.content[0].author").value("Robert C. Martin"));

        verify(bookService).findAllByPage(0, 20);
    }

    @Test
    void getAllBooks_shouldRespectCustomPageAndLimitParams() throws Exception {
        Page<Book> page = new PageImpl<>(List.of());
        when(bookService.findAllByPage(2, 5)).thenReturn(page);

        mockMvc.perform(get("/api/v1/books").param("page", "2").param("limit", "5"))
                .andExpect(status().isOk());

        verify(bookService).findAllByPage(2, 5);
    }

    // -------------------------
    // POST /api/v1/books
    // -------------------------

    @Test
    void createBook_shouldReturn200WithCreatedBook() throws Exception {
        CreateBookRequestDto dto = CreateBookRequestDto.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .price(new BigDecimal("39.99"))
                .stockQuantity(10)
                .build();

        when(bookService.create(any(CreateBookRequestDto.class))).thenReturn(book);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book created successfully"))
                .andExpect(jsonPath("$.data.title").value("Clean Code"))
                .andExpect(jsonPath("$.data.author").value("Robert C. Martin"));

        verify(bookService).create(any(CreateBookRequestDto.class));
    }

    @Test
    void createBook_shouldReturn400_whenTitleIsBlank() throws Exception {
        CreateBookRequestDto dto = CreateBookRequestDto.builder()
                .title("")
                .author("Some Author")
                .price(new BigDecimal("9.99"))
                .stockQuantity(1)
                .build();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verify(bookService, never()).create(any());
    }

    @Test
    void createBook_shouldReturn400_whenAuthorIsBlank() throws Exception {
        CreateBookRequestDto dto = CreateBookRequestDto.builder()
                .title("Some Title")
                .author("")
                .price(new BigDecimal("9.99"))
                .stockQuantity(1)
                .build();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verify(bookService, never()).create(any());
    }

    @Test
    void createBook_shouldReturn400_whenPriceIsNegative() throws Exception {
        CreateBookRequestDto dto = CreateBookRequestDto.builder()
                .title("Some Title")
                .author("Some Author")
                .price(new BigDecimal("-5.00"))
                .stockQuantity(1)
                .build();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verify(bookService, never()).create(any());
    }

    @Test
    void createBook_shouldReturn400_whenStockQuantityIsNegative() throws Exception {
        CreateBookRequestDto dto = CreateBookRequestDto.builder()
                .title("Some Title")
                .author("Some Author")
                .price(new BigDecimal("9.99"))
                .stockQuantity(-1)
                .build();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verify(bookService, never()).create(any());
    }

    // -------------------------
    // GET /api/v1/books/{id}
    // -------------------------

    @Test
    void getBook_shouldReturn200WithBook_whenBookExists() throws Exception {
        when(bookService.findById(bookId)).thenReturn(book);

        mockMvc.perform(get("/api/v1/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Books retrieved successfully"))
                .andExpect(jsonPath("$.data.title").value("Clean Code"))
                .andExpect(jsonPath("$.data.isbn").value("978-0132350884"));

        verify(bookService).findById(bookId);
    }

    @Test
    void getBook_shouldReturn404_whenBookNotFound() throws Exception {
        when(bookService.findById(bookId))
                .thenThrow(new ResourceNotFoundException("Book", "id", bookId));

        mockMvc.perform(get("/api/v1/books/{id}", bookId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));

        verify(bookService).findById(bookId);
    }

    // -------------------------
    // PATCH /api/v1/books/{id}
    // -------------------------

    @Test
    void updateBook_shouldReturn200WithUpdatedBook() throws Exception {
        UpdateBookRequestDto dto = UpdateBookRequestDto.builder()
                .title("Updated Title")
                .price(new BigDecimal("49.99"))
                .build();

        Book updatedBook = Book.builder()
                .id(bookId)
                .title("Updated Title")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .price(new BigDecimal("49.99"))
                .stockQuantity(10)
                .build();

        when(bookService.update(eq(bookId), any(UpdateBookRequestDto.class))).thenReturn(updatedBook);

        mockMvc.perform(patch("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book updated successfully"))
                .andExpect(jsonPath("$.data.title").value("Updated Title"))
                .andExpect(jsonPath("$.data.price").value(49.99));

        verify(bookService).update(eq(bookId), any(UpdateBookRequestDto.class));
    }

    @Test
    void updateBook_shouldReturn404_whenBookNotFound() throws Exception {
        UpdateBookRequestDto dto = UpdateBookRequestDto.builder().title("New Title").build();

        when(bookService.update(eq(bookId), any(UpdateBookRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Book", "id", bookId));

        mockMvc.perform(patch("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    // -------------------------
    // DELETE /api/v1/books/{id}
    // -------------------------

    @Test
    void deleteBook_shouldReturn204_whenBookExists() throws Exception {
        doNothing().when(bookService).delete(bookId);

        mockMvc.perform(delete("/api/v1/books/{id}", bookId))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book updated successfully"));

        verify(bookService).delete(bookId);
    }

    @Test
    void deleteBook_shouldReturn404_whenBookNotFound() throws Exception {
        when(bookService.delete(bookId))
                .thenThrow(new ResourceNotFoundException("Book", "id", bookId));

        mockMvc.perform(delete("/api/v1/books/{id}", bookId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));

        verify(bookService).delete(bookId);
    }
}