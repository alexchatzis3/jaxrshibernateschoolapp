package gr.aueb.cf.schoolapp.core.services;

import gr.aueb.cf.schoolapp.core.exceptions.*;
import gr.aueb.cf.schoolapp.dto.ResponseMessageDTO;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Maps custom {@link EntityGenericException} exceptions to appropriate HTTP responses.
 *
 * <p>This class implements {@link ExceptionMapper} and is automatically registered
 * as a JAX-RS provider using the {@link Provider} annotation.</p>
 *
 * <p>The mapper inspects the exception type and sets the corresponding HTTP status code,
 * returning a JSON response with a structured message.</p>
 */
@Provider
public class AppExceptionMapper implements ExceptionMapper<EntityGenericException> {

    /**
     * Converts a thrown {@link EntityGenericException} to a proper {@link Response}.
     *
     * @param exception the exception instance to map
     * @return a {@link Response} object with HTTP status and JSON payload
     */
    @Override
    public Response toResponse(EntityGenericException exception) {

        // Default status for unhandled exceptions
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        // Map specific exception types to corresponding HTTP status codes
        if (exception instanceof EntityNotFoundException) {
            status = Response.Status.NOT_FOUND; // 404
        } else if (exception instanceof EntityInvalidArgumentException) {
            status = Response.Status.BAD_REQUEST; // 400
        } else if (exception instanceof EntityNotAuthorizedException) {
            status = Response.Status.UNAUTHORIZED; // 401
        } else if (exception instanceof EntityAlreadyExistsException) {
            status = Response.Status.CONFLICT; // 409
        } else if (exception instanceof AppServerException) {
            status = Response.Status.SERVICE_UNAVAILABLE; // 503
        }

        // Build the response with structured JSON message
        return Response
                .status(status)
                .entity(new ResponseMessageDTO(exception.getCode(), exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
