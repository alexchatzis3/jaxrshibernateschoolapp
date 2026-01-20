package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.StudentReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.StudentUpdateDTO;

import java.util.List;
import java.util.Map;

/**
 * Service interface that defines business operations related to {@code Student} entities.
 *
 * <p>This layer is responsible for enforcing application rules, performing validation,
 * and transforming data between DTOs and domain entities.</p>
 */
public interface IStudentService {

    /**
     * Creates a new student based on the provided {@link StudentInsertDTO}.
     *
     * @param insertDTO DTO containing the required student data for creation
     * @return a {@link StudentReadOnlyDTO} representing the persisted student
     * @throws EntityAlreadyExistsException if a student with conflicting unique fields already exists
     * @throws EntityInvalidArgumentException if the provided data fails validation rules
     * @throws EntityNotFoundException if a referenced related entity does not exist (e.g. class)
     */
    StudentReadOnlyDTO insertStudent(StudentInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException;

    /**
     * Updates an existing student using the provided {@link StudentUpdateDTO}.
     *
     * @param updateDTO DTO containing updated student data and identifier
     * @return a {@link StudentReadOnlyDTO} representing the updated student
     * @throws EntityNotFoundException if no student matching the provided ID exists
     * @throws EntityInvalidArgumentException if provided data is invalid
     */
    StudentReadOnlyDTO updateStudent(StudentUpdateDTO updateDTO)
            throws EntityNotFoundException, EntityInvalidArgumentException;

    /**
     * Deletes a student by its identifier.
     *
     * @param id student identifier
     * @throws EntityNotFoundException if no student with the given ID exists
     */
    void deleteStudent(Object id) throws EntityNotFoundException;

    /**
     * Retrieves a student by its identifier.
     *
     * @param id student identifier
     * @return a {@link StudentReadOnlyDTO} representing the found student
     * @throws EntityNotFoundException if no student with the given ID exists
     */
    StudentReadOnlyDTO getStudentById(Object id) throws EntityNotFoundException;

    /**
     * Retrieves all stored students.
     *
     * @return a list of {@link StudentReadOnlyDTO} instances, possibly empty
     */
    List<StudentReadOnlyDTO> getAllStudents();

    /**
     * Retrieves students matching the provided filtering criteria.
     *
     * @param criteria a map where keys represent field names and values represent filter values
     * @return a filtered list of {@link StudentReadOnlyDTO} instances, possibly empty
     */
    List<StudentReadOnlyDTO> getStudentsByCriteria(Map<String, Object> criteria);
}
