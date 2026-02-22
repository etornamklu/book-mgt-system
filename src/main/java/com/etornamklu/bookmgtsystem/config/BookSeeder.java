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
                        .author("Robert C. Martin")
                        .isbn("978-0132350884")
                        .price(new BigDecimal("39.99"))
                        .stockQuantity(50)
                        .build(),
                Book.builder()
                        .title("The Pragmatic Programmer")
                        .author("David Thomas")
                        .isbn("978-0135957059")
                        .price(new BigDecimal("49.99"))
                        .stockQuantity(30)
                        .build(),
                Book.builder()
                        .title("Design Patterns")
                        .author("Gang of Four")
                        .isbn("978-0201633610")
                        .price(new BigDecimal("54.99"))
                        .stockQuantity(25)
                        .build(),
                Book.builder()
                        .title("Refactoring")
                        .author("Martin Fowler")
                        .isbn("978-0134757599")
                        .price(new BigDecimal("44.99"))
                        .stockQuantity(40)
                        .build(),
                Book.builder()
                        .title("Domain-Driven Design")
                        .author("Eric Evans")
                        .isbn("978-0321125217")
                        .price(new BigDecimal("59.99"))
                        .stockQuantity(20)
                        .build(),
                Book.builder()
                        .title("Spring Boot in Action")
                        .author("Craig Walls")
                        .isbn("978-1617292545")
                        .price(new BigDecimal("49.99"))
                        .stockQuantity(35)
                        .build(),
                Book.builder()
                        .title("Effective Java")
                        .author("Joshua Bloch")
                        .isbn("978-0134685991")
                        .price(new BigDecimal("44.99"))
                        .stockQuantity(45)
                        .build(),
                Book.builder()
                        .title("You Don't Know JS")
                        .author("Kyle Simpson")
                        .isbn("978-1491924464")
                        .price(new BigDecimal("34.99"))
                        .stockQuantity(60)
                        .build()
        );

        bookRepository.saveAll(books);
        log.info("Successfully seeded {} books", books.size());
    }
}