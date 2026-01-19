package gr.aueb.cf.schoolapp.core.exceptions;

/**
 * Exception thrown when a method receives an invalid argument
 * that prevents the operation from being performed correctly.
 *
 * <p>This exception extends {@link EntityGenericException} and is typically used
 * to indicate validation errors or incorrect input data provided by the user
 * or calling code.</p>
 */
public class EntityInvalidArgumentException extends EntityGenericException {

    /**
     * Default code used to identify this type of exception.
     */
    private static final String DEFAULT_CODE = "InvalidArgument";

    /**
     * Constructs a new EntityInvalidArgumentException with a specific code and message.
     *
     * @param code    a custom code identifying the error context
     * @param message a detailed message describing the invalid argument
     */
    public EntityInvalidArgumentException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
