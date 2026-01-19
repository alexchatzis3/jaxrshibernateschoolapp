package gr.aueb.cf.schoolapp.core.exceptions;

/**
 * Exception thrown when a user or process is not authorized
 * to perform a requested action on an entity.
 *
 * <p>This exception extends {@link EntityGenericException} and is typically used
 * to indicate that access control rules have been violated, such as
 * insufficient permissions or missing roles.</p>
 */
public class EntityNotAuthorizedException extends EntityGenericException {

    /**
     * Default code used to identify this type of exception.
     */
    private static final String DEFAULT_CODE = "NotAuthorized";

    /**
     * Constructs a new EntityNotAuthorizedException with a specific code and message.
     *
     * @param code    a custom code identifying the error context
     * @param message a detailed message describing the authorization failure
     */
    public EntityNotAuthorizedException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
