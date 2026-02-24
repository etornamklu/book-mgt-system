package com.etornamklu.bookmgtsystem.service;

import com.etornamklu.bookmgtsystem.dto.request.CreateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.request.UpdateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.response.BookResponseDto;
import com.etornamklu.bookmgtsystem.exception.ResourceNotFoundException;
import com.etornamklu.bookmgtsystem.mapper.BookMapper;
import com.etornamklu.bookmgtsystem.model.Book;
import com.etornamklu.bookmgtsystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service class for managing {@link Book} entities.
 * <p>
 * Provides methods to create, retrieve, update, and delete books.
 * Supports caching for individual books and paginated lists.
 * Uses soft-delete by setting {@code deletedAt} timestamp instead of removing records.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Finds a book by its ID if it has not been soft-deleted.
     * <p>
     * Results are cached using {@code books} cache with the book ID as the key.
     *
     * @param id the UUID of the book to retrieve
     * @return the {@link BookResponseDto} representing the found book
     * @throws ResourceNotFoundException if no book with the given ID exists or it is deleted
     */
    @Cacheable(value = "books", key = "#id")
    public BookResponseDto findById(UUID id) {
        Book book = bookRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        return BookMapper.toDto(book);
    }

    /**
     * Retrieves a paginated list of all books that have not been soft-deleted.
     * <p>
     * Results are cached using {@code books-page} cache with {@code page-size} as the key.
     *
     * @param page the page number to retrieve
     * @param size the number of books per page
     * @return a {@link Page} of {@link BookResponseDto} objects
     */
    @Cacheable(value = "books-page", key = "#page + '-' + #size")
    public Page<BookResponseDto> findAllByPage(int page, int size) {
        Page<Book> books = bookRepository.findAllByDeletedAtIsNull(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return books.map(BookMapper::toDto);
    }

    /**
     * Creates a new book in the system.
     * <p>
     * Evicts all entries in {@code books} and {@code books-page} caches after creation.
     *
     * @param dto the {@link CreateBookRequestDto} containing book details
     * @return the {@link BookResponseDto} representing the created book
     */
    @CacheEvict(value = {"books", "books-page"}, allEntries = true)
    @Transactional
    public BookResponseDto create(CreateBookRequestDto dto) {
        log.info("Creating new book: {}", dto.getTitle());
        Book book = Book.builder()
                .title(dto.getTitle())
                .price(dto.getPrice())
                .coverImage(dto.getCoverImage())
                .build();

        bookRepository.save(book);
        return BookMapper.toDto(book);
    }

    /**
     * Updates an existing book by its ID.
     * <p>
     * Only non-null fields in the {@code dto} are updated.
     * Evicts all entries in {@code books} and {@code books-page} caches after update.
     *
     * @param id  the UUID of the book to update
     * @param dto the {@link UpdateBookRequestDto} containing updated book details
     * @return the {@link BookResponseDto} representing the updated book
     * @throws ResourceNotFoundException if no book with the given ID exists or it is deleted
     */
    @CacheEvict(value = {"books", "books-page"}, allEntries = true)
    @Transactional
    public BookResponseDto update(UUID id, UpdateBookRequestDto dto) {
        Book book = bookRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        if (dto.getTitle() != null) book.setTitle(dto.getTitle());
        if (dto.getPrice() != null) book.setPrice(dto.getPrice());
        if (dto.getCoverImage() != null) book.setCoverImage(dto.getCoverImage());

        bookRepository.save(book);
        return BookMapper.toDto(book);
    }

    /**
     * Soft-deletes a book by setting its {@code deletedAt} timestamp.
     * <p>
     * Evicts all entries in {@code books} and {@code books-page} caches after deletion.
     *
     * @param id the UUID of the book to delete
     * @return null (Void) indicating successful deletion
     * @throws ResourceNotFoundException if no book with the given ID exists or it is already deleted
     */
    @CacheEvict(value = {"books", "books-page"}, allEntries = true)
    @Transactional
    public Void delete(UUID id) {
        Book book = bookRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setDeletedAt(LocalDateTime.now());
        bookRepository.save(book);
        log.info("Deleted book: {}", id);
        return null;
    }

}