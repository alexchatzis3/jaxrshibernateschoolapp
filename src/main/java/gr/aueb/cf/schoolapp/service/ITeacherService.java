package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;

import java.util.List;
import java.util.Map;

/**
 * Service interface that defines business operations related to {@code Teacher} entities.
 *
 * <p>This layer is responsible for applying application rules, validation, and
 * transforming data between DTOs and domain entities.</p>
 */
public interface ITeacherService {

    /**
     * Creates a new teacher based on the provided {@link TeacherInsertDTO}.
     *
     * @param insertDTO DTO containing required teacher data for creation
     * @return a {@link TeacherReadOnlyDTO} representing the persisted teacher
     * @throws EntityAlreadyExistsException if a teacher with the same unique field (e.g. VAT) already exists
     * @throws EntityInvalidArgumentException if the provided data fails validation rules
     */
    TeacherReadOnlyDTO insertTeacher(TeacherInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    /**
     * Updates an existing teacher using the provided {@link TeacherUpdateDTO}.
     *
     * @param updateDTO DTO containing updated teacher data and identifier
     * @return a {@link TeacherReadOnlyDTO} representing the updated teacher
     * @throws EntityNotFoundException if no teacher matching the provided ID exists
     * @throws EntityInvalidArgumentException if provided data is invalid
     */
    TeacherReadOnlyDTO updateTeacher(TeacherUpdateDTO updateDTO)
            throws EntityNotFoundException, EntityInvalidArgumentException;

    /**
     * Deletes a teacher by its identifier.
     *
     * @param id teacher identifier
     * @throws EntityNotFoundException if no teacher with the given ID exists
     */
    void deleteTeacher(Object id) throws EntityNotFoundException;

    /**
     * Retrieves a teacher by its identifier.
     *
     * @param id teacher identifier
     * @return a {@link TeacherReadOnlyDTO} representing the found teacher
     * @throws EntityNotFoundException if no teacher with the given ID exists
     */
    TeacherReadOnlyDTO getTeacherById(Object id) throws EntityNotFoundException;

    /**
     * Retrieves all stored teachers.
     *
     * @return a list of {@link TeacherReadOnlyDTO} instances, possibly empty
     */
    List<TeacherReadOnlyDTO> getAllTeachers();

    /**
     * Retrieves teachers matching the provided filtering criteria.
     *
     * @param criteria a map where keys represent field names and values represent filter values
     * @return a filtered list of {@link TeacherReadOnlyDTO} instances, possibly empty
     */
    List<TeacherReadOnlyDTO> getTeachersByCriteria(Map<String, Object> criteria);
}
