package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.User;

import java.util.Optional;

/**
 * Data Access Object (DAO) interface for {@link User} entity.
 *
 * <p>Provides methods to perform CRUD operations and specific queries
 * related to user authentication and uniqueness.</p>
 *
 * <p>Extends {@link IGenericDAO} to inherit generic CRUD operations.</p>
 */
public interface IUserDAO extends IGenericDAO<User> {

    /**
     * Finds a user by their unique username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    Optional<User> getByUsername(String username);

    /**
     * Checks if a username/password combination is valid.
     *
     * <p>This is typically used for authentication purposes.</p>
     *
     * @param username the username of the user
     * @param password the plaintext password to check
     * @return {@code true} if the credentials match a user, {@code false} otherwise
     */
    boolean isUserValid(String username, String password);

    /**
     * Checks whether a username (email) already exists in the database.
     *
     * <p>This helps enforce uniqueness before inserting a new user.</p>
     *
     * @param username the username (email) to check
     * @return {@code true} if the username exists, {@code false} otherwise
     */
    boolean isEmailExists(String username);
}
