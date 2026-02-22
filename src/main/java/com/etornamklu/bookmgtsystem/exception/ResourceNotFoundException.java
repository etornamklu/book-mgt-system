package com.etornamklu.bookmgtsystem.exception;

/**
 * Exception thrown when a requested resource is not found in the system.
 * <p>
 * Typically used in service layers when an entity with a given identifier
 * does not exist or has been soft-deleted.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@link ResourceNotFoundException} with a detailed message.
     *
     * @param resource the name of the resource (e.g., "Book")
     * @param field    the field used to search for the resource (e.g., "id")
     * @param value    the value of the field that was not found
     */
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: '%s'", resource, field, value));
    }
}