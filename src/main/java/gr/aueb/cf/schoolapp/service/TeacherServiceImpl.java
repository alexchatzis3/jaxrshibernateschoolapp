package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static gr.aueb.cf.schoolapp.mapper.Mapper.mapToTeacher;

/**
 * Service layer implementation for managing {@code Teacher} entities.
 *
 * <p>This class handles business logic, validation, transactions,
 * data transformations (DTO ↔ Entity), and exception mapping.</p>
 *
 * <p>DAO operations are delegated to {@link ITeacherDAO}.</p>
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TeacherServiceImpl implements ITeacherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);

    /** DAO layer dependency used for database operations. */
    private final ITeacherDAO teacherDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherReadOnlyDTO insertTeacher(TeacherInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            JPAHelper.beginTransaction();

            Teacher teacher = Mapper.mapToTeacher(insertDTO);

            // Check uniqueness constraint (VAT must be unique)
            teacherDAO.getByVat(insertDTO.getVat()).orElseThrow(() ->
                    new EntityAlreadyExistsException("Teacher",
                            "Teacher with vat: " + insertDTO.getVat() + " already exists."));

            TeacherReadOnlyDTO readOnlyDTO = teacherDAO.insert(teacher)
                    .map(Mapper::mapToTeacherReadOnlyDTO)
                    .orElseThrow(() ->
                            new EntityInvalidArgumentException("Teacher",
                                    "Teacher with vat: " + insertDTO.getVat() + " not inserted."));

            JPAHelper.commitTransaction();

            LOGGER.info("Teacher inserted: id={}, vat={}, firstname={}, lastname={}",
                    teacher.getId(), teacher.getVat(), teacher.getFirstname(), teacher.getLastname());

            return readOnlyDTO;

        } catch (Exception e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Insert failed for teacher: vat={}, firstname={}, lastname={}",
                    insertDTO.getVat(), insertDTO.getFirstname(), insertDTO.getLastname(), e);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherReadOnlyDTO updateTeacher(TeacherUpdateDTO updateDTO)
            throws EntityNotFoundException, EntityInvalidArgumentException {

        try {
            JPAHelper.beginTransaction();

            Teacher teacher = mapToTeacher(updateDTO);

            // Ensure VAT exists before updating
            teacherDAO.getByVat(updateDTO.getVat()).orElseThrow(() ->
                    new EntityNotFoundException("Teacher",
                            "Teacher with vat: " + updateDTO.getVat() + " not found."));

            // Ensure ID exists before updating
            teacherDAO.getById(updateDTO.getId()).orElseThrow(() ->
                    new EntityNotFoundException("Teacher",
                            "Teacher with id: " + updateDTO.getId() + " not found."));

            TeacherReadOnlyDTO readOnlyDTO = teacherDAO.update(teacher)
                    .map(Mapper::mapToTeacherReadOnlyDTO)
                    .orElseThrow(() ->
                            new EntityInvalidArgumentException("Teacher", "Error during update."));

            JPAHelper.commitTransaction();

            LOGGER.info("Teacher updated: id={}, vat={}, firstname={}, lastname={}",
                    teacher.getId(), teacher.getVat(), teacher.getFirstname(), teacher.getLastname());

            return readOnlyDTO;

        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Update failed for teacher: id={}, vat={}, firstname={}, lastname={}",
                    updateDTO.getId(), updateDTO.getVat(), updateDTO.getFirstname(), updateDTO.getLastname(), e);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTeacher(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();

            // Check existence before delete
            teacherDAO.getById(id).orElseThrow(() ->
                    new EntityNotFoundException("Teacher",
                            "Teacher with id: " + id + " not found."));

            teacherDAO.delete(id);

            JPAHelper.commitTransaction();
            LOGGER.info("Teacher deleted: id={}", id);

        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Delete failed. Teacher with id {} was not found.", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherReadOnlyDTO getTeacherById(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();

            TeacherReadOnlyDTO readOnlyDTO = teacherDAO.getById(id)
                    .map(Mapper::mapToTeacherReadOnlyDTO)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Teacher",
                                    "Teacher with id " + id + " not found"));

            JPAHelper.commitTransaction();
            LOGGER.info("Teacher found: id={}", id);

            return readOnlyDTO;

        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.warn("Teacher not found: id={}", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TeacherReadOnlyDTO> getAllTeachers() {
        try {
            JPAHelper.beginTransaction();

            List<TeacherReadOnlyDTO> readOnlyDTOS = teacherDAO.getAll()
                    .stream()
                    .map(Mapper::mapToTeacherReadOnlyDTO)
                    .toList();

            JPAHelper.commitTransaction();
            return readOnlyDTOS;

        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TeacherReadOnlyDTO> getTeachersByCriteria(Map<String, Object> criteria) {
        try {
            JPAHelper.beginTransaction();

            List<TeacherReadOnlyDTO> readOnlyDTOS = teacherDAO.getByCriteria(criteria)
                    .stream()
                    .map(Mapper::mapToTeacherReadOnlyDTO)
                    .toList();

            JPAHelper.commitTransaction();
            return readOnlyDTOS;

        } finally {
            JPAHelper.closeEntityManager();
        }
    }
}
