package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.security.SecUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Implementation of {@link IUserDAO} for {@link User} entity.
 *
 * <p>This class provides concrete database operations for users,
 * including finding by username, validating credentials, and checking
 * username uniqueness.</p>
 *
 * <p>Extends {@link AbstractDAO} to inherit generic CRUD operations.</p>
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
public class UserDAOImpl extends AbstractDAO<User> implements IUserDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> getByUsername(String username) {
        String sql = "SELECT u FROM User u WHERE u.username = :username";

        try {
            User user = getEntityManager()
                    .createQuery(sql, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            // Return empty if no user is found
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Uses {@link SecUtil} to verify that the provided plaintext password
     * matches the stored hashed password.</p>
     */
    @Override
    public boolean isUserValid(String username, String password) {
        String sql = "SELECT u FROM User u WHERE u.username = :username";

        try {
            User user = getEntityManager()
                    .createQuery(sql, User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            // Check if the password matches the hashed password in the database
            return SecUtil.checkPassword(password, user.getPassword());

        } catch (NoResultException e) {
            // Return false if username does not exist
            return false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Checks if a username already exists in the database.</p>
     *
     * @param username the username to check
     * @return {@code true} if the username exists, {@code false} otherwise
     */
    @Override
    public boolean isEmailExists(String username) {
        String sql = "SELECT COUNT(u) FROM User u WHERE u.username = :username";

        try {
            Long count = getEntityManager()
                    .createQuery(sql, Long.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return count > 0;

        } catch (NoResultException e) {
            // If no results, the username does not exist
            return false;
        }
    }
}
