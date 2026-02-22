package com.etornamklu.bookmgtsystem.repository;

import com.etornamklu.bookmgtsystem.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for performing CRUD operations on {@link Book} entities.
 * Extends {@link JpaRepository} to provide standard JPA methods.
 * Includes custom queries for soft-deleted records (ignoring books with non-null deletedAt).
 */
@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    /**
     * Finds a book by its ID, only if it has not been soft-deleted.
     *
     * @param id the UUID of the book to find
     * @return an Optional containing the book if found and not deleted, or empty otherwise
     */
    Optional<Book> findByIdAndDeletedAtIsNull(UUID id);

    /**
     * Retrieves a paginated list of all books that have not been soft-deleted.
     *
     * @param pageable the pagination information
     * @return a Page containing books that have deletedAt == null
     */
    Page<Book> findAllByDeletedAtIsNull(Pageable pageable);
}