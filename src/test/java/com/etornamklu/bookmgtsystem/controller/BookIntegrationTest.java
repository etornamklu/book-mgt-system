package com.etornamklu.bookmgtsystem.controller;

import com.etornamklu.bookmgtsystem.dto.request.CreateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.request.UpdateBookRequestDto;
import com.etornamklu.bookmgtsystem.model.Book;
import com.etornamklu.bookmgtsystem.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateBookRequestDto createDto;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();

        createDto = CreateBookRequestDto.builder()
                .title("Clean Code")
                .price(new BigDecimal("39.99"))
                .build();
    }

    // -------------------------
    // POST /api/v1/books
    // -------------------------

    @Test
    void createBook_shouldPersistAndReturn201() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Clean Code"))
                .andExpect(jsonPath("$.data.id").isNotEmpty());

        assertThat(bookRepository.count()).isEqualTo(1);
    }

    @Test
    void createBook_shouldReturn400_whenTitleMissing() throws Exception {
        createDto.setTitle("");

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"));

        assertThat(bookRepository.count()).isEqualTo(0);
    }

    // -------------------------
    // GET /api/v1/books
    // -------------------------

    @Test
    void getAllBooks_shouldReturnPagedResults() throws Exception {
        bookRepository.save(Book.builder().title("Book A").price(new BigDecimal("10.00")).build());
        bookRepository.save(Book.builder().title("Book B").price(new BigDecimal("20.00")).build());

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.page.totalElements").value(2))
                .andExpect(jsonPath("$.data.page.number").value(0));
    }

    @Test
    void getAllBooks_shouldNotReturnSoftDeletedBooks() throws Exception {
        // create a book then delete it
        String response = mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(response, "$.data.id");

        mockMvc.perform(delete("/api/v1/books/{id}", id));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.page.totalElements").value(0));
    }

    @Test
    void getAllBooks_shouldRespectPaginationParams() throws Exception {
        for (int i = 1; i <= 5; i++) {
            bookRepository.save(Book.builder()
                    .title("Book " + i)
                    .price(new BigDecimal("10.00"))
                    .build());
        }

        mockMvc.perform(get("/api/v1/books").param("page", "0").param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.page.totalElements").value(5))
                .andExpect(jsonPath("$.data.page.totalPages").value(3));
    }

    // -------------------------
    // GET /api/v1/books/{id}
    // -------------------------

    @Test
    void getBook_shouldReturnBook_whenExists() throws Exception {
        String response = mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(response, "$.data.id");

        mockMvc.perform(get("/api/v1/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Clean Code"))
                .andExpect(jsonPath("$.data.id").value(id));
    }

    @Test
    void getBook_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/books/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getBook_shouldReturn404_whenBookIsSoftDeleted() throws Exception {
        String response = mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(response, "$.data.id");

        mockMvc.perform(delete("/api/v1/books/{id}", id));

        mockMvc.perform(get("/api/v1/books/{id}", id))
                .andExpect(status().isNotFound());
    }

    // -------------------------
    // PATCH /api/v1/books/{id}
    // -------------------------

    @Test
    void updateBook_shouldPersistChanges() throws Exception {
        String response = mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(response, "$.data.id");

        UpdateBookRequestDto updateDto = UpdateBookRequestDto.builder()
                .title("Updated Title")
                .price(new BigDecimal("59.99"))
                .build();

        mockMvc.perform(patch("/api/v1/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated Title"))
                .andExpect(jsonPath("$.data.price").value(59.99));
    }

    @Test
    void updateBook_shouldReturn404_whenNotFound() throws Exception {
        UpdateBookRequestDto updateDto = UpdateBookRequestDto.builder().title("New Title").build();

        mockMvc.perform(patch("/api/v1/books/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }


    // -------------------------
    // DELETE /api/v1/books/{id}
    // -------------------------

    @Test
    void deleteBook_shouldSoftDelete_notHardDelete() throws Exception {
        String response = mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(response, "$.data.id");

        mockMvc.perform(delete("/api/v1/books/{id}", id))
                .andExpect(status().isNoContent());

        // record still exists in DB (tombstoned)
        Book book = bookRepository.findById(UUID.fromString(id)).orElseThrow();
        assertThat(book.getDeletedAt()).isNotNull();

        // but is no longer accessible via API
        mockMvc.perform(get("/api/v1/books/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBook_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/books/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}