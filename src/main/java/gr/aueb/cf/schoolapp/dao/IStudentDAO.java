package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Student;

import java.util.Optional;

/**
 * DAO interface for accessing {@link Student} entities.
 * Provides generic CRUD operations and additional methods specific to Student.
 */
public interface IStudentDAO extends IGenericDAO<Student> {

    /**
     * Finds a student by their email address.
     *
     * @param email the email of the student to search for
     * @return an Optional containing the Student if found, otherwise empty
     */
    Optional<Student> getByEmail(String email);
}
