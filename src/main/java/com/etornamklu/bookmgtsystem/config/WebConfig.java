package com.etornamklu.bookmgtsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Web configuration class for the Book Management System.
 * <p>
 * Enables Spring Data web support to automatically handle pageable and sortable
 * requests in controllers. Configured to serialize {@link org.springframework.data.domain.Page}
 * responses via DTOs rather than exposing entities directly.
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig {
}