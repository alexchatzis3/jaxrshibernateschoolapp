package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for inserting a new {@code User}.
 *
 * <p>This DTO is used when creating a new user through the REST API or service layer.
 * It includes validation annotations to ensure the input is correct:
 * <ul>
 *     <li>{@link #username} must be a valid email address.</li>
 *     <li>{@link #password} and {@link #confirmPassword} must satisfy a strong password pattern:
 *         at least 8 characters, including one uppercase, one lowercase, one digit, and one special character.</li>
 *     <li>{@link #role} cannot be empty and must match a valid role (e.g., ADMIN, TEACHER, STUDENT).</li>
 * </ul>
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInsertDTO {

    /**
     * The username of the user, which must be a valid email address.
     */
    @Email(message = "Invalid username")
    private String username;

    /**
     * The user's password. Must contain at least:
     * <ul>
     *     <li>One lowercase letter</li>
     *     <li>One uppercase letter</li>
     *     <li>One number</li>
     *     <li>One special character (e.g., @$!%&*)</li>
     *     <li>Minimum length of 8 characters</li>
     * </ul>
     */
    @Pattern(
            regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)(?=.*?[@$!%&*]).{8,}$",
            message = "Invalid password."
    )
    private String password;

    /**
     * Confirmation of the password. Must match the same strong password pattern as {@link #password}.
     */
    @Pattern(
            regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)(?=.*?[@$!%&*]).{8,}$",
            message = "Invalid password."
    )
    private String confirmPassword;

    /**
     * The role assigned to the user. Cannot be empty.
     * Examples: "ADMIN", "TEACHER", "STUDENT".
     */
    @NotEmpty(message = "Role can not be empty")
    private String role;
}
