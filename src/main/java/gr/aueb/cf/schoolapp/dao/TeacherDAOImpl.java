package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Teacher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

/**
 * Implementation of {@link ITeacherDAO} using JPA.
 * Provides CRUD operations for Teacher entities and additional query methods.
 */
@ApplicationScoped
public class TeacherDAOImpl extends AbstractDAO<Teacher> implements ITeacherDAO {

    /**
     * Default constructor.
     * Sets the persistent class to {@link Teacher} for generic DAO operations.
     */
    public TeacherDAOImpl() {
        setPersistenceClass(Teacher.class);
    }

    /**
     * Finds a teacher by their VAT number.
     * Uses a JPQL query to search the database.
     *
     * @param vat the VAT number to search for
     * @return Optional containing the Teacher if found, otherwise empty
     */
    @Override
    public Optional<Teacher> getByVat(String vat) {
        EntityManager em = getEntityManager();
        String sql = "SELECT t FROM Teacher t WHERE t.vat = :vat";

        try {
            // Execute the JPQL query with the VAT parameter
            Teacher teacher = em.createQuery(sql, Teacher.class)
                    .setParameter("vat", vat)
                    .getSingleResult();

            return Optional.of(teacher);
        } catch (NoResultException e) {
            // Return empty if no teacher is found
            return Optional.empty();
        }
    }
}
