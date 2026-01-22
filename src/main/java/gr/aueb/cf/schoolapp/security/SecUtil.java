package gr.aueb.cf.schoolapp.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password security using BCrypt hashing.
 *
 * <p>This class provides static methods for hashing passwords and
 * verifying plaintext passwords against stored hashed passwords.</p>
 *
 * <p>All methods are static and the constructor is private to prevent instantiation.</p>
 */
public final class SecUtil {

    // Private constructor to prevent instantiation
    private SecUtil() {}

    /**
     * Generates a hashed password from a plaintext input using BCrypt.
     *
     * <p>The method generates a random salt internally and applies the
     * BCrypt hashing algorithm with a configurable workload factor.</p>
     *
     * @param inputPasswd the plaintext password to hash
     * @return the hashed password as a string
     */
    public static String hashPassword(String inputPasswd) {
        int workload = 12; // defines the complexity of the hash
        String salt = BCrypt.gensalt(workload);
        return BCrypt.hashpw(inputPasswd, salt);
    }

    /**
     * Checks if a plaintext password matches a previously hashed password.
     *
     * <p>This is used for authentication to verify that the provided
     * password matches the stored hashed password in the database.</p>
     *
     * @param inputPasswd the plaintext password provided by the user
     * @param storedHashedPasswd the previously hashed password stored in the database
     * @return {@code true} if the passwords match, {@code false} otherwise
     */
    public static boolean checkPassword(String inputPasswd, String storedHashedPasswd) {
        return BCrypt.checkpw(inputPasswd, storedHashedPasswd);
    }
}
