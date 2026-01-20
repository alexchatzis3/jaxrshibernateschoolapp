package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.CourseInsertDTO;
import gr.aueb.cf.schoolapp.dto.CourseReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.CourseUpdateDTO;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing {@code Course} entities.
 *
 * <p>This layer applies business rules, validation, transaction control,
 * and transforms data between DTOs and domain entities.</p>
 */
public interface ICourseService {

    /**
     * Creates a new course based on the provided {@link CourseInsertDTO}.
     *
     * @param insertDTO DTO containing required course data
     * @return a {@link CourseReadOnlyDTO} representing the persisted course
     * @throws EntityAlreadyExistsException if a course with the same unique fields (e.g., title + teacher) already exists
     * @throws EntityInvalidArgumentException if provided data is invalid
     * @throws EntityNotFoundException if referenced teacher does not exist
     */
    CourseReadOnlyDTO insertCourse(CourseInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException;

    /**
     * Updates an existing course using the provided {@link CourseUpdateDTO}.
     *
     * @param updateDTO DTO containing updated course data and identifier
     * @return a {@link CourseReadOnlyDTO} representing the updated course
     * @throws EntityInvalidArgumentException if provided data is invalid
     * @throws EntityNotFoundException if no course matching the provided ID exists
     */
    CourseReadOnlyDTO updateCourse(CourseUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException;

    /**
     * Deletes a course by its identifier.
     *
     * @param id course identifier
     * @throws EntityNotFoundException if no course with the given ID exists
     */
    void deleteCourse(Object id) throws EntityNotFoundException;

    /**
     * Retrieves a course by its identifier.
     *
     * @param id course identifier
     * @return a {@link CourseReadOnlyDTO} representing the found course
     * @throws EntityNotFoundException if no course with the given ID exists
     */
    CourseReadOnlyDTO getCourseById(Object id) throws EntityNotFoundException;

    /**
     * Retrieves all stored courses.
     *
     * @return a list of {@link CourseReadOnlyDTO} instances, possibly empty
     */
    List<CourseReadOnlyDTO> getAllCourses();

    /**
     * Retrieves courses matching the provided filtering criteria.
     *
     * @param criteria a map where keys represent field names and values represent filter values
     * @return a filtered list of {@link CourseReadOnlyDTO} instances, possibly empty
     */
    List<CourseReadOnlyDTO> getCoursesByCriteria(Map<String, Object> criteria);
}
