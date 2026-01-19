package gr.aueb.cf.schoolapp.core.exceptions;

/**
 * Exception thrown when a requested entity cannot be found in the database or persistence layer.
 *
 * <p>This exception extends {@link EntityGenericException} and is typically used
 * to indicate that an entity lookup by ID, email, or other criteria failed.</p>
 */
public class EntityNotFoundException extends EntityGenericException {

    /**
     * Default code used to identify this type of exception.
     */
    private static final String DEFAULT_CODE = "NotFound";

    /**
     * Constructs a new EntityNotFoundException with a specific code and message.
     *
     * @param code    a custom code identifying the error context
     * @param message a detailed message describing the entity not found situation
     */
    public EntityNotFoundException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
