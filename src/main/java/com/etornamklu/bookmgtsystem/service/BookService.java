package com.etornamklu.bookmgtsystem.service;

import com.etornamklu.bookmgtsystem.dto.request.CreateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.request.UpdateBookRequestDto;
import com.etornamklu.bookmgtsystem.exception.ResourceNotFoundException;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Cacheable(value = "books", key = "#id")
    public Book findById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    @Cacheable(value = "books-page", key = "#page + '-' + #size")
    public Page<Book> findAllByPage(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @CacheEvict(value = {"books", "books-page"}, allEntries = true)
    @Transactional
    public Book create(CreateBookRequestDto dto) {
        log.info("Creating new book: {}", dto.getTitle());
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .build();

        return bookRepository.save(book);
    }

    @CacheEvict(value = {"books", "books-page"}, allEntries = true)
    @Transactional
    public Book update(UUID id, UpdateBookRequestDto dto) {
        Book book = findById(id);

        if (dto.getTitle() != null) {
            book.setTitle(dto.getTitle());
        }

        if (dto.getAuthor() != null) {
            book.setAuthor(dto.getAuthor());
        }

        if (dto.getIsbn() != null) {
            book.setIsbn(dto.getIsbn());
        }

        if (dto.getPrice() != null) {
            book.setPrice(dto.getPrice());
        }

        if (dto.getStockQuantity() != null) {
            book.setStockQuantity(dto.getStockQuantity());
        }
        return bookRepository.save(book);
    }


    @CacheEvict(value = {"books", "books-page"}, allEntries = true)
    @Transactional
    public Void delete(UUID id) {
        Book product = findById(id);
        bookRepository.delete(product);
        log.info("Deleted book: {}", id);
        return null;
    }

}
