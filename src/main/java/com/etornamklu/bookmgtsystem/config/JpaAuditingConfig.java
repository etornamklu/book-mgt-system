package com.etornamklu.bookmgtsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.etornamklu.bookmgtsystem.model.Book;

/**
 * Configuration class to enable JPA auditing in the Book Management System.
 * <p>
 * With this configuration, Spring Data JPA automatically populates auditing fields
 * such as {@code @CreatedDate} and {@code @LastModifiedDate} in entity classes.
 * <p>
 * Typically used in entities like {@link Book}
 * to track creation and modification timestamps.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}