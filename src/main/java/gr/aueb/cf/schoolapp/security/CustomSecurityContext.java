package gr.aueb.cf.schoolapp.security;

import gr.aueb.cf.schoolapp.model.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.security.Principal;

/**
 * Custom implementation of {@link SecurityContext} for JAX-RS.
 *
 * <p>This class provides user authentication and role-based authorization
 * information for the current request. It wraps a {@link User} entity
 * and exposes its principal and role information.</p>
 *
 * <p>The class is {@code @RequestScoped} to ensure a separate instance
 * per HTTP request.</p>
 */
@RequestScoped
@NoArgsConstructor
@AllArgsConstructor
public class CustomSecurityContext implements SecurityContext {

    /** The authenticated user associated with the current request. */
    private User user;

    /**
     * Returns the {@link Principal} associated with the current request.
     *
     * @return the authenticated {@link User} as a {@link Principal}, or {@code null} if unauthenticated
     */
    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    /**
     * Checks whether the authenticated user has a specific role.
     *
     * <p>The role is matched against the {@link gr.aueb.cf.schoolapp.core.enums.RoleType}
     * name of the user.</p>
     *
     * @param role the role name to check (e.g., "ADMIN", "TEACHER", "STUDENT")
     * @return {@code true} if the user has the role, {@code false} otherwise
     */
    @Override
    public boolean isUserInRole(String role) {
        return user.getRoleType().name().equals(role);
    }

    /**
     * Indicates whether the request was made over a secure channel (HTTPS).
     *
     * @return {@code false} as HTTPS detection is not implemented
     */
    @Override
    public boolean isSecure() {
        return false;
    }

    /**
     * Returns the authentication scheme used for this request.
     *
     * @return the authentication scheme as a string (e.g., "Bearer")
     */
    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}
