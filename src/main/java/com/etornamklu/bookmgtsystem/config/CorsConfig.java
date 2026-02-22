package com.etornamklu.bookmgtsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS) settings.
 * <p>
 * Reads allowed origins, methods, headers, and credentials from application properties
 * and registers a {@link CorsConfigurationSource} bean for the application.
 */
@Configuration
public class CorsConfig {

    /**
     * Comma-separated list of allowed origins (e.g., "<a href="http://localhost:3000,http://example.com">...</a>").
     */
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    /**
     * Comma-separated list of allowed HTTP methods (e.g., "GET,POST,PUT,DELETE").
     */
    @Value("${app.cors.allowed-methods}")
    private String allowedMethods;

    /**
     * Comma-separated list of allowed HTTP headers or "*" for all headers.
     */
    @Value("${app.cors.allowed-headers}")
    private String allowedHeaders;

    /**
     * Flag indicating whether credentials (cookies, authorization headers) are allowed.
     */
    @Value("${app.cors.allow-credentials}")
    private Boolean allowCredentials;

    /**
     * Creates a {@link CorsConfigurationSource} bean based on the configured properties.
     * <p>
     * This method registers the CORS configuration for all endpoints ("/**") in the application.
     *
     * @return a {@link CorsConfigurationSource} with allowed origins, methods, headers, and credentials
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));

        if ("*".equals(allowedHeaders)) {
            configuration.addAllowedHeader("*");
        } else {
            configuration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        }

        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}