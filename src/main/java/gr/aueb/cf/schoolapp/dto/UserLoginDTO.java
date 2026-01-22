package gr.aueb.cf.schoolapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for user login requests.
 *
 * <p>This DTO is used when a user attempts to log in via the REST API.
 * It contains the minimal information required to authenticate a user:
 * the username (email) and password.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginDTO {

    /**
     * The username used to log in, usually an email.
     */
    private String username;

    /**
     * The password used to authenticate the user.
     */
    private String password;
}
