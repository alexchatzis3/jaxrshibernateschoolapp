package gr.aueb.cf.schoolapp.core.exceptions;

import lombok.Getter;

/**
 * Base exception class for generic entity-related errors in the application.
 *
 * <p>This exception can be used when a specific entity operation fails and
 * a standard message and error code should be returned.</p>
 *
 * <p>The {@code code} field can be used to represent application-specific
 * error codes for easier client handling or logging.</p>
 */
@Getter
public class EntityGenericException extends Exception {

    /** Application-specific error code describing the exception. */
    private final String code;

    /**
     * Constructs a new EntityGenericException with a specific code and message.
     *
     * @param code    a string representing the error code
     * @param message a detailed message describing the exception
     */
    public EntityGenericException(String code, String message) {
        super(message);
        this.code = code;
    }
}
