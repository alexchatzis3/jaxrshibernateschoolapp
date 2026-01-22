package gr.aueb.cf.schoolapp.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) used as a response for authentication requests.
 *
 * <p>This DTO contains the JWT token that is returned to the client
 * after successful login. The client should store this token (e.g., in local storage
 * or a cookie) and include it in the `Authorization` header of subsequent requests.</p>
 *
 * <p>Example JSON response:</p>
 * <pre>
 * {
 *     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 * }
 * </pre>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationResponseDTO {

    /**
     * JWT token issued after successful authentication.
     *
     * <p>This token encodes the user's identity and role(s) and is
     * used for authenticating subsequent requests.</p>
     */
    private String token;
}
