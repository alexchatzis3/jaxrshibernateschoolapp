package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.AppServerException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.UserInsertDTO;
import gr.aueb.cf.schoolapp.dto.UserReadOnlyDTO;

/**
 * Service interface for managing {@code User} entities.
 *
 * <p>This layer handles business logic for users, including creation,
 * retrieval, validation, and existence checks. All operations
 * are performed using DTOs for input and output.</p>
 */
public interface IUserService {

    /**
     * Creates a new user in the system.
     *
     * @param dto the user data to insert
     * @return a {@link UserReadOnlyDTO} representing the created user
     * @throws AppServerException if an unexpected error occurs during insertion
     */
    UserReadOnlyDTO insertUser(UserInsertDTO dto) throws AppServerException;

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user
     * @return a {@link UserReadOnlyDTO} containing user information
     * @throws EntityNotFoundException if no user with the given username exists
     */
    UserReadOnlyDTO getUserByUsername(String username) throws EntityNotFoundException;

    /**
     * Validates if a user's credentials are correct.
     *
     * <p>This method checks whether the provided password matches the stored
     * hashed password for the given username.</p>
     *
     * @param username the username of the user
     * @param password the plain-text password provided for validation
     * @return {@code true} if the credentials are valid, {@code false} otherwise
     */
    boolean isUserValid(String username, String password);

    /**
     * Checks if a username (email) already exists in the system.
     *
     * <p>This is useful to enforce unique usernames during registration.</p>
     *
     * @param username the username to check
     * @return {@code true} if the username exists, {@code false} otherwise
     */
    boolean isEmailExists(String username);
}
