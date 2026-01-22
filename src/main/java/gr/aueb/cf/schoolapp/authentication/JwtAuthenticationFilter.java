package gr.aueb.cf.schoolapp.authentication;

import gr.aueb.cf.schoolapp.dao.IUserDAO;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.security.CustomSecurityContext;
import gr.aueb.cf.schoolapp.security.JwtService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * JAX-RS request filter that authenticates incoming HTTP requests using JWT.
 *
 * <p>This filter intercepts every request and validates the JWT provided in the
 * Authorization header. If the token is valid, it sets a {@link CustomSecurityContext}
 * with the authenticated {@link User} for the duration of the request.</p>
 *
 * <p>Public paths (e.g., login and registration) bypass authentication.</p>
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    /** Service for parsing and validating JWT tokens */
    private final JwtService jwtService;

    /** DAO for retrieving user information from the database */
    private final IUserDAO userDAO;

    /** JAX-RS injected SecurityContext (not used directly, we replace it) */
    @Context
    SecurityContext securityContext;

    /**
     * Filters each HTTP request to check for a valid JWT.
     *
     * <p>If the request path is public (e.g., login or register), it is ignored.
     * Otherwise, the filter checks for the Authorization header, validates the JWT,
     * and sets the {@link CustomSecurityContext} with the authenticated user.</p>
     *
     * @param requestContext the request context to filter
     * @throws IOException never thrown here, but required by interface
     * @throws NotAuthorizedException if the token is missing, invalid, or user not found
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        UriInfo uriInfo = requestContext.getUriInfo();
        String path = uriInfo.getPath();

        // Skip authentication for public endpoints
        if (isPublicPath(path)) {
            return;
        }

        // Get the Authorization header
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the header
        String token = authorizationHeader.substring("Bearer ".length()).trim();

        try {
            // Extract the username from the token
            String username = jwtService.extractSubject(token);
            if (username == null) {
                throw new NotAuthorizedException("Invalid token: username not found");
            }

            // Retrieve the user from the database
            User user = userDAO.getByUsername(username).orElse(null);

            // Validate the token against the user
            if (user == null || !jwtService.isTokenValid(token, user)) {
                throw new NotAuthorizedException("Invalid token or user not found");
            }

            // Set the SecurityContext for downstream access control
            requestContext.setSecurityContext(new CustomSecurityContext(user));

        } catch (Exception e) {
            // Any exception means authentication failed
            throw new NotAuthorizedException("Invalid token");
        }
    }

    /**
     * Checks if a given path is public and does not require authentication.
     *
     * @param path the request URI path
     * @return true if the path is public, false otherwise
     */
    private boolean isPublicPath(String path) {
        return path.equals("auth/register") || path.equals("auth/login");
    }
}
