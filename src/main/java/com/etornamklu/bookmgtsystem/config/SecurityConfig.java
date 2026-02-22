package com.etornamklu.bookmgtsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the Book Management System.
 * <p>
 * Configures HTTP security, including CSRF protection, CORS, endpoint authorization,
 * and stateless session management.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the {@link SecurityFilterChain} for HTTP requests.
     * <p>
     * - Disables CSRF protection (recommended for stateless APIs).<br>
     * - Enables CORS support.<br>
     * - Permits all requests to the book API endpoints and Swagger/OpenAPI documentation.<br>
     * - Configures the session management to be stateless.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs while configuring security
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/books/**",
                                "/docs", "/swagger-ui/**", "/v3/api-docs/**"
                        ).permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}