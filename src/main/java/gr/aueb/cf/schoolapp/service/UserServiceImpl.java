package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.AppServerException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dao.IUserDAO;
import gr.aueb.cf.schoolapp.dto.UserInsertDTO;
import gr.aueb.cf.schoolapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link IUserService} for managing User entities.
 *
 * <p>This service handles creation, retrieval, and validation of users,
 * as well as checking if a username/email exists. All methods wrap
 * database operations in transactions and use JPAHelper to manage the EntityManager.</p>
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserServiceImpl implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /** Data Access Object for user operations */
    private final IUserDAO userDAO;

    /**
     * Inserts a new user into the database.
     *
     * <p>The password is already hashed by the Mapper when converting from the DTO.
     * This method starts a transaction, delegates the insert to the DAO, and commits
     * if successful. It rolls back the transaction if any error occurs.</p>
     *
     * @param dto DTO containing the user data to insert
     * @return {@link UserReadOnlyDTO} of the created user
     * @throws AppServerException if the user could not be inserted
     */
    @Override
    public UserReadOnlyDTO insertUser(UserInsertDTO dto) throws AppServerException {
        try {
            JPAHelper.beginTransaction();

            // Convert DTO to entity (password hashed)
            User user = Mapper.mapToUser(dto);

            // Insert user in DB
            UserReadOnlyDTO readOnlyDTO = userDAO.insert(user)
                    .map(Mapper::mapToUserReadOnlyDTO)
                    .orElseThrow(() -> new AppServerException(
                            "User",
                            "User with username: " + dto.getUsername() + " not inserted"));

            JPAHelper.commitTransaction();
            LOGGER.info("User with username '{}' inserted successfully", dto.getUsername());
            return readOnlyDTO;

        } catch (AppServerException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error inserting user with username '{}'", dto.getUsername(), e);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * Retrieves a user by their username.
     *
     * <p>If no user is found, an {@link EntityNotFoundException} is thrown.
     * This method starts a transaction, delegates the retrieval to the DAO, and closes the EntityManager.</p>
     *
     * @param username the username to search for
     * @return {@link UserReadOnlyDTO} of the found user
     * @throws EntityNotFoundException if no user with the given username exists
     */
    @Override
    public UserReadOnlyDTO getUserByUsername(String username) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();

            UserReadOnlyDTO userReadOnlyDTO = userDAO.getByUsername(username)
                    .map(Mapper::mapToUserReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User",
                            "User with username: " + username + " not found"));

            JPAHelper.commitTransaction();
            return userReadOnlyDTO;

        } catch (EntityNotFoundException e) {
            LOGGER.warn("User with username '{}' not found", username);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * Validates if a user's credentials are correct.
     *
     * <p>This checks if the provided plain-text password matches the hashed password in the database.</p>
     *
     * @param username the username of the user
     * @param password the plain-text password to validate
     * @return true if credentials are valid, false otherwise
     */
    @Override
    public boolean isUserValid(String username, String password) {
        try {
            JPAHelper.beginTransaction();
            boolean isValid = userDAO.isUserValid(username, password);
            JPAHelper.commitTransaction();
            return isValid;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * Checks if a username (email) already exists in the system.
     *
     * @param username the username/email to check
     * @return true if the username exists, false otherwise
     */
    @Override
    public boolean isEmailExists(String username) {
        try {
            JPAHelper.beginTransaction();
            boolean exists = userDAO.isEmailExists(username);
            JPAHelper.commitTransaction();
            return exists;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }
}
