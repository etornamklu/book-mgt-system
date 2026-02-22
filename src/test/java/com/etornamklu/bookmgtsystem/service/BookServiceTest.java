package com.etornamklu.bookmgtsystem.service;

import com.etornamklu.bookmgtsystem.dto.request.CreateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.request.UpdateBookRequestDto;
import com.etornamklu.bookmgtsystem.dto.response.BookResponseDto;
import com.etornamklu.bookmgtsystem.exception.ResourceNotFoundException;
import com.etornamklu.bookmgtsystem.model.Book;
import com.etornamklu.bookmgtsystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

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
                .version(1L)
                .build();
    }


    @Test
    void findById_shouldReturnBook_whenBookExists() {
        when(bookRepository.findByIdAndDeletedAtIsNull(bookId)).thenReturn(Optional.of(book));

        BookResponseDto result = bookService.findById(bookId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookId);
        assertThat(result.getTitle()).isEqualTo("Clean Code");
        verify(bookRepository).findByIdAndDeletedAtIsNull(bookId);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenBookNotFound() {
        when(bookRepository.findByIdAndDeletedAtIsNull(bookId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(bookId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book")
                .hasMessageContaining("id");

        verify(bookRepository).findByIdAndDeletedAtIsNull(bookId);
    }


    @Test
    void findAllByPage_shouldReturnPageOfBooks() {
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAllByDeletedAtIsNull(any(PageRequest.class))).thenReturn(bookPage);

        Page<BookResponseDto> result = bookService.findAllByPage(0, 20);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getTitle()).isEqualTo("Clean Code");
        verify(bookRepository).findAllByDeletedAtIsNull(PageRequest.of(0, 20, Sort.by("createdAt").descending()));
    }

    @Test
    void findAllByPage_shouldReturnEmptyPage_whenNoBooksExist() {
        Page<Book> emptyPage = new PageImpl<>(List.of());
        when(bookRepository.findAllByDeletedAtIsNull(any(PageRequest.class))).thenReturn(emptyPage);

        Page<BookResponseDto> result = bookService.findAllByPage(0, 20);

        assertThat(result.getContent()).isEmpty();
    }


    @Test
    void create_shouldSaveAndReturnBook() {
        CreateBookRequestDto dto = CreateBookRequestDto.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .price(new BigDecimal("39.99"))
                .stockQuantity(10)
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDto result = bookService.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Clean Code");
        assertThat(result.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(result.getIsbn()).isEqualTo("978-0132350884");
        assertThat(result.getPrice()).isEqualByComparingTo("39.99");
        assertThat(result.getStockQuantity()).isEqualTo(10);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void create_shouldBuildBookWithAllFieldsFromDto() {
        CreateBookRequestDto dto = CreateBookRequestDto.builder()
                .title("The Pragmatic Programmer")
                .author("Dave Thomas")
                .isbn("978-0135957059")
                .price(new BigDecimal("49.99"))
                .stockQuantity(5)
                .build();

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponseDto result = bookService.create(dto);

        assertThat(result.getTitle()).isEqualTo("The Pragmatic Programmer");
        assertThat(result.getAuthor()).isEqualTo("Dave Thomas");
        assertThat(result.getIsbn()).isEqualTo("978-0135957059");
        assertThat(result.getPrice()).isEqualByComparingTo("49.99");
        assertThat(result.getStockQuantity()).isEqualTo(5);
    }


    @Test
    void update_shouldUpdateAllFields_whenAllFieldsProvided() {
        UpdateBookRequestDto dto = UpdateBookRequestDto.builder()
                .title("Updated Title")
                .author("Updated Author")
                .isbn("000-0000000000")
                .price(new BigDecimal("59.99"))
                .stockQuantity(20)
                .build();

        when(bookRepository.findByIdAndDeletedAtIsNull(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponseDto result = bookService.update(bookId, dto);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getAuthor()).isEqualTo("Updated Author");
        assertThat(result.getIsbn()).isEqualTo("000-0000000000");
        assertThat(result.getPrice()).isEqualByComparingTo("59.99");
        assertThat(result.getStockQuantity()).isEqualTo(20);
        verify(bookRepository).save(book);
    }

    @Test
    void update_shouldOnlyUpdateProvidedFields_whenPartialDtoProvided() {
        UpdateBookRequestDto dto = UpdateBookRequestDto.builder()
                .title("New Title Only")
                .build();

        when(bookRepository.findByIdAndDeletedAtIsNull(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponseDto result = bookService.update(bookId, dto);

        assertThat(result.getTitle()).isEqualTo("New Title Only");
        assertThat(result.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(result.getIsbn()).isEqualTo("978-0132350884");
        assertThat(result.getPrice()).isEqualByComparingTo("39.99");
        assertThat(result.getStockQuantity()).isEqualTo(10);
    }

    @Test
    void update_shouldThrowResourceNotFoundException_whenBookNotFound() {
        UpdateBookRequestDto dto = UpdateBookRequestDto.builder().title("Whatever").build();
        when(bookRepository.findByIdAndDeletedAtIsNull(bookId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update(bookId, dto))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(bookRepository, never()).save(any());
    }

    @Test

    void update_shouldThrowOptimisticLockingException_whenConcurrentUpdateDetected() {

        UpdateBookRequestDto dto = UpdateBookRequestDto.builder()
                .title("Updated Title")
                .build();
        when(bookRepository.findByIdAndDeletedAtIsNull(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(Book.class, bookId));
        assertThatThrownBy(() -> bookService.update(bookId, dto))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
        verify(bookRepository).save(book);

    }

    @Test

    void update_shouldSucceed_whenNoConcurrentUpdateDetected() {
        UpdateBookRequestDto dto = UpdateBookRequestDto.builder()
                .title("Updated Title")
                .build();
        Book updatedBook = Book.builder()
                .id(bookId)
                .title("Updated Title")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .price(new BigDecimal("39.99"))
                .stockQuantity(10)
                .version(2L)  // version incremented after successful save
                .build();

        when(bookRepository.findByIdAndDeletedAtIsNull(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        var result = bookService.update(bookId, dto);
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        verify(bookRepository).save(book);

    }




    @Test
    void delete_shouldSoftDeleteBook_whenBookExists() {
        when(bookRepository.findByIdAndDeletedAtIsNull(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Void result = bookService.delete(bookId);

        assertThat(result).isNull();
        assertThat(book.getDeletedAt()).isNotNull();  // tombstone is set
        verify(bookRepository).save(book);            // save called, not delete
        verify(bookRepository, never()).delete(any()); // hard delete never called
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenBookNotFound() {
        when(bookRepository.findByIdAndDeletedAtIsNull(bookId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.delete(bookId))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(bookRepository, never()).save(any());
        verify(bookRepository, never()).delete(any());
    }
}