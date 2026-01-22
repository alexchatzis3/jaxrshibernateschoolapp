package gr.aueb.cf.schoolapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for returning user information in a read-only format.
 *
 * <p>This DTO is used when sending user data back to the client via REST APIs.
 * It exposes basic user information including id, username, role, and password hash.
 * The password field should contain the hashed password and should never be sent
 * in plain text.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReadOnlyDTO {

    /**
     * The unique identifier of the user in the database.
     */
    private Long id;

    /**
     * The username of the user, usually an email.
     */
    private String username;

    /**
     * The hashed password of the user.
     */
    private String password;

    /**
     * The role assigned to the user, e.g., "ADMIN" or "TEACHER".
     */
    private String role;
}
