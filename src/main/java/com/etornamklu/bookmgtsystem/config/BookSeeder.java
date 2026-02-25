package com.etornamklu.bookmgtsystem.config;

import com.etornamklu.bookmgtsystem.model.Book;
import com.etornamklu.bookmgtsystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Component that seeds the database with initial {@link Book} records.
 * <p>
 * This runner executes at application startup, but will not run in the "test" profile.
 * If the database already contains books, seeding is skipped.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")  // won't run during tests
public class BookSeeder implements CommandLineRunner {

    /**
     * Repository for performing CRUD operations on {@link Book} entities.
     */
    private final BookRepository bookRepository;

    /**
     * Seeds the database with a predefined list of books if none exist.
     * <p>
     * Each book is created with title, author, ISBN, price, and stock quantity.
     *
     * @param args runtime arguments (not used)
     */
    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) {
            log.info("Database already seeded, skipping...");
            return;
        }

        log.info("Seeding database with books...");

        List<Book> books = List.of(
                Book.builder()
                        .title("Clean Code")
                        .price(new BigDecimal("39.99"))
                        .build(),
                Book.builder()
                        .title("The Pragmatic Programmer")
                        .price(new BigDecimal("49.99"))
                        .build(),
                Book.builder()
                        .title("Design Patterns")
                        .price(new BigDecimal("54.99"))
                        .build(),
                Book.builder()
                        .title("Refactoring")
                        .price(new BigDecimal("44.99"))
                        .build(),
                Book.builder()
                        .title("Domain-Driven Design")
                        .price(new BigDecimal("59.99"))
                        .build(),
                Book.builder()
                        .title("Spring Boot in Action")
                        .price(new BigDecimal("49.99"))
                        .build(),
                Book.builder()
                        .title("Effective Java")
                        .price(new BigDecimal("44.99"))
                        .build(),
                Book.builder()
                        .title("You Don't Know JS")
                        .price(new BigDecimal("34.99"))
                        .build()
        );

        bookRepository.saveAll(books);
        log.info("Successfully seeded {} books", books.size());
    }
}