package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Student;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

/**
 * Implementation of {@link IStudentDAO} for accessing {@link Student} entities.
 * Extends {@link AbstractDAO} to inherit common CRUD and query operations.
 */
@ApplicationScoped
public class StudentDAOImpl extends AbstractDAO<Student> implements IStudentDAO {

    /**
     * Default constructor sets the persistence class for generic DAO operations.
     */
    public StudentDAOImpl() {
        setPersistenceClass(Student.class);
    }

    /**
     * Finds a student by their email address.
     *
     * @param email the email of the student to search for
     * @return an Optional containing the Student if found, otherwise empty
     */
    @Override
    public Optional<Student> getByEmail(String email) {
        // Obtain the EntityManager from JPA helper
        EntityManager em = getEntityManager();

        // JPQL query to find a student by email
        String sql = "SELECT s FROM Student s WHERE s.email = :email";

        try {
            // Execute query and return result wrapped in Optional
            Student student = em.createQuery(sql, Student.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(student);
        } catch (NoResultException e) {
            // Return empty Optional if no student is found
            return Optional.empty();
        }
    }
}
