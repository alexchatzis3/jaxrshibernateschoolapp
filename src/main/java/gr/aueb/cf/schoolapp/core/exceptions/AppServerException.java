package gr.aueb.cf.schoolapp.core.exceptions;

/**
 * Exception thrown when an unexpected server-side error occurs
 * that prevents the application from completing the requested operation.
 *
 * <p>This exception extends {@link EntityGenericException} and is typically used
 * to indicate internal server issues, such as database connectivity problems,
 * resource unavailability, or other unexpected failures.</p>
 */
public class AppServerException extends EntityGenericException {

    /**
     * Constructs a new AppServerException with a specific code and message.
     *
     * @param code    a custom code identifying the error context
     * @param message a detailed message describing the exception
     */
    public AppServerException(String code, String message) {
        super(code, message);
    }
}
