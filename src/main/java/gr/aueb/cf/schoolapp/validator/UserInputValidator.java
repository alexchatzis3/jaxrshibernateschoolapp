package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dao.IUserDAO;
import gr.aueb.cf.schoolapp.dao.UserDAOImpl;
import gr.aueb.cf.schoolapp.dto.UserInsertDTO;
import gr.aueb.cf.schoolapp.service.IUserService;
import gr.aueb.cf.schoolapp.service.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for validating {@link UserInsertDTO} input before user registration.
 *
 * <p>This class performs additional validation that cannot be handled
 * by annotations alone, such as checking if the password matches the
 * confirmation and whether the username/email already exists in the database.</p>
 *
 * <p>All methods are static and the class cannot be instantiated.</p>
 */
public class UserInputValidator {

    /** DAO for user database operations */
    private static final IUserDAO userDAO = new UserDAOImpl();

    /** Service for user-related business logic */
    private static final IUserService userService = new UserServiceImpl(userDAO);

    /** Private constructor to prevent instantiation */
    private UserInputValidator() {}

    /**
     * Validates a {@link UserInsertDTO}.
     *
     * <p>Performs the following checks:</p>
     * <ul>
     *     <li>Checks that {@code password} matches {@code confirmPassword}</li>
     *     <li>Checks if the {@code username/email} already exists in the database</li>
     * </ul>
     *
     * @param <T> type parameter extending {@link UserInsertDTO}
     * @param dto the user registration DTO to validate
     * @return a map of field names to error messages. Empty if no errors.
     */
    public static <T extends UserInsertDTO> Map<String, String> validate(T dto) {
        Map<String, String> errors = new HashMap<>();

        // Check if password and confirmation match
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            errors.put("confirmPassword", "Password and ConfirmedPassword do not match");
        }

        // Check if username/email already exists
        if (userService.isEmailExists(dto.getUsername())) {
            errors.put("username", "Email/Username already exists");
        }

        return errors;
    }
}
