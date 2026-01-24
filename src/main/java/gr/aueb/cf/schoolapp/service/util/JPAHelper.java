package gr.aueb.cf.schoolapp.service.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Properties;

/**
 * Utility class for managing JPA {@link EntityManager} and transactions.
 *
 * <p>Provides thread-safe access to the {@link EntityManager} and
 * methods for beginning, committing, and rolling back transactions.
 * Also manages the lifecycle of the {@link EntityManagerFactory}.</p>
 *
 * <p>All methods are static and the class cannot be instantiated.</p>
 */
public final class JPAHelper {

    /** JPA EntityManagerFactory singleton instance. */
    private static EntityManagerFactory emf;

    /** Thread-local storage for EntityManager instances. */
    private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

    /** Private constructor to prevent instantiation. */
    private JPAHelper() {}

    /**
     * Returns the singleton {@link EntityManagerFactory}.
     * <p>If the factory does not exist or is closed, it is created using
     * the specified database connection properties. The database password
     * is read from the environment variable "PASS_DB6".</p>
     *
     * @return the {@link EntityManagerFactory} instance
     * @throws RuntimeException if the "PASS_DB6" environment variable is not set
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            Properties props = new Properties();

            // Database user
            props.setProperty("hibernate.hikari.dataSource.user", "schooluser");

            // Database password from environment variable
            String dbPassword = System.getenv("PASS_DB6");
            if (dbPassword == null) {
                throw new RuntimeException("PASS_DB6 environment variable is not set");
            }
            props.setProperty("hibernate.hikari.dataSource.password", dbPassword);

            // Database URL
            props.setProperty("hibernate.hikari.dataSource.url", "jdbc:mysql://localhost:3306/schooljaxrshiberdb?serverTimezone=UTC");

            // Create the EntityManagerFactory with the specified properties
            emf = Persistence.createEntityManagerFactory("schoolPU", props);
        }
        return emf;
    }

    /**
     * Returns the thread-local {@link EntityManager}.
     * Creates a new one if it does not exist or is closed.
     *
     * @return the {@link EntityManager} for the current thread
     */
    public static EntityManager getEntityManager() {
        EntityManager em = threadLocal.get();
        if (em == null || !em.isOpen()) {
            em = getEntityManagerFactory().createEntityManager();
            threadLocal.set(em);
        }
        return em;
    }

    /**
     * Closes the thread-local {@link EntityManager} if it exists.
     */
    public static void closeEntityManager() {
        EntityManager em = threadLocal.get();
        if (em != null && em.isOpen()) {
            em.close();
            threadLocal.remove();
        }
    }

    /**
     * Begins a JPA transaction on the thread-local {@link EntityManager}.
     */
    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }

    /**
     * Commits the current JPA transaction.
     */
    public static void commitTransaction() {
        getEntityManager().getTransaction().commit();
    }

    /**
     * Rolls back the current JPA transaction.
     */
    public static void rollbackTransaction() {
        getEntityManager().getTransaction().rollback();
    }

    /**
     * Closes the {@link EntityManagerFactory}.
     * Should be called when the application shuts down.
     */
    public static void closeEMF() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
