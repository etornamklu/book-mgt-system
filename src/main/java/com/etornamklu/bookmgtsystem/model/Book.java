package com.etornamklu.bookmgtsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a Book in the system.
 * <p>
 * Supports soft-delete via {@link #deletedAt}, optimistic locking via {@link #version},
 * and auditing with {@link #createdAt} and {@link #updatedAt}.
 */
@Entity
@Table(name = "books")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    /**
     * Primary key for the book.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Version field for optimistic locking.
     */
    @Version
    private Long version;

    /**
     * Title of the book.
     */
    @NotBlank
    @Column(nullable = false, length = 150)
    private String title;

    /**
     * Price of the book in the local currency.
     * Must be a positive value.
     */
    @Positive
    @Column(precision = 10, scale = 2)
    private BigDecimal price;


    /**
     * Cover image of the book stored as a binary large object (BLOB).
     */
    @Lob
    private byte[] coverImage;

    /**
     * Timestamp indicating when the book was soft-deleted.
     * If null, the book is considered active.
     */
    private LocalDateTime deletedAt;

    /**
     * Timestamp indicating when the book was created.
     * Automatically set by JPA auditing, not updatable.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp indicating when the book was last updated.
     * Automatically set by JPA auditing.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}