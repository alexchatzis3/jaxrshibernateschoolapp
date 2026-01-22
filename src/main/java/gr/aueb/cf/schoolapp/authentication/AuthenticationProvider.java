package gr.aueb.cf.schoolapp.authentication;

import gr.aueb.cf.schoolapp.dto.UserLoginDTO;
import gr.aueb.cf.schoolapp.service.IUserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Provides authentication services for users.
 *
 * <p>This class acts as a bridge between the login DTO provided by a client
 * and the {@link IUserService} which handles user validation.
 * It checks if the provided username and password are valid.</p>
 *
 * <p>The class is request-scoped and injectable via CDI.</p>
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
public class AuthenticationProvider {

    /** Service used to validate user credentials */
    private final IUserService userService;

    /**
     * Authenticates a user based on their login information.
     *
     * <p>This method delegates validation to {@link IUserService#isUserValid(String, String)}.
     * It compares the username and password provided by the client against the database.
     * Password comparison is done securely (hashed passwords).</p>
     *
     * @param loginDTO the user's login data (username and password)
     * @return {@code true} if the username and password are correct, {@code false} otherwise
     */
    public boolean authenticate(UserLoginDTO loginDTO) {
        // Delegates the check to the UserService
        return userService.isUserValid(loginDTO.getUsername(), loginDTO.getPassword());
    }
}
