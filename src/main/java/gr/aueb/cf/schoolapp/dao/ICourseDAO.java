package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Course;

import java.util.Optional;

/**
 * Data Access Object interface for {@link Course} entity.
 * Extends the generic DAO for common CRUD operations.
 */
public interface ICourseDAO extends IGenericDAO<Course> {

    /**
     * Finds a course by its title.
     *
     * @param title the title of the course to search for
     * @return Optional containing the Course if found, otherwise empty
     */
    Optional<Course> getCourseByTitle(String title);

}
