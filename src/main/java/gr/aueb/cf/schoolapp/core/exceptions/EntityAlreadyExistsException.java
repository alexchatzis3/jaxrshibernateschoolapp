package gr.aueb.cf.schoolapp.core.exceptions;

/**
 * Exception thrown when attempting to create or insert an entity
 * that already exists in the system.
 *
 * <p>This exception extends {@link EntityGenericException} and
 * automatically appends a default code ("AlreadyExists") to the provided code.</p>
 *
 * <p>It is typically used to indicate conflicts during create operations,
 * such as duplicate entries.</p>
 */
public class EntityAlreadyExistsException extends EntityGenericException {

    /** Default suffix for the error code to indicate an existing entity. */
    private static final String DEFAULT_CODE = "AlreadyExists";

    /**
     * Constructs a new EntityAlreadyExistsException with a specific code and message.
     *
     * @param code    a custom code to identify the specific error context
     * @param message a detailed message describing the exception
     */
    public EntityAlreadyExistsException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
