package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Course;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

/**
 * Implementation of {@link ICourseDAO} for accessing Course entities.
 * Provides CRUD operations and additional methods specific to Course.
 */
@ApplicationScoped
public class CourseDAOImpl extends AbstractDAO<Course> implements ICourseDAO {

    /**
     * Default constructor sets the persistence class for generic DAO operations.
     */
    public CourseDAOImpl() {
        setPersistenceClass(Course.class);
    }

    /**
     * Retrieves a Course entity from the database by its title.
     *
     * @param title the title of the course to search for
     * @return Optional containing the Course if found, otherwise empty
     */
    @Override
    public Optional<Course> getCourseByTitle(String title) {
        EntityManager em = getEntityManager();

        // JPQL query to fetch a course by its title
        String sql = "SELECT c FROM Course c WHERE c.title = :title";

        try {
            // Execute the query and return a single result
            Course course = em.createQuery(sql, Course.class)
                    .setParameter("title", title)
                    .getSingleResult();

            return Optional.of(course);
        } catch (NoResultException e) {
            // Return empty if no course is found with the given title
            return Optional.empty();
        }
    }
}
