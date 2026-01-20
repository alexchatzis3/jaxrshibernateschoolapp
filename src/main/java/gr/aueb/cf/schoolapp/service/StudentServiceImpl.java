package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dao.IStudentDAO;
import gr.aueb.cf.schoolapp.dao.ICourseDAO;
import gr.aueb.cf.schoolapp.dto.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.StudentReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.StudentUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Service layer implementation for managing {@code Student} entities.
 *
 * <p>This class applies business rules, validation, transaction control,
 * DTO ↔ Entity transformations, and maps exceptions to domain-specific types.</p>
 *
 * <p>DAO interactions are delegated to {@link IStudentDAO} and {@link ICourseDAO}.</p>
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class StudentServiceImpl implements IStudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    /** DAO dependency used for persistence operations on students. */
    private final IStudentDAO studentDAO;

    /** DAO dependency used for loading related courses. */
    private final ICourseDAO courseDAO;


    /**
     * {@inheritDoc}
     */
    @Override
    public StudentReadOnlyDTO insertStudent(StudentInsertDTO dto)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {

        try {
            JPAHelper.beginTransaction();

            // Check email uniqueness constraint
            if (studentDAO.getByEmail(dto.getEmail()).isPresent()) {
                throw new EntityAlreadyExistsException("Student",
                        "Email " + dto.getEmail() + " already exists");
            }

            // Load referenced courses
            Set<Course> courses = loadCourses(dto.getCourseIds());

            Student student = Mapper.mapToStudent(dto, courses);

            Student saved = studentDAO.insert(student)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Student", "not inserted"));

            StudentReadOnlyDTO result = Mapper.mapToStudentReadOnlyDTO(saved);

            JPAHelper.commitTransaction();
            LOGGER.info("Inserted student id={} email={}", saved.getId(), saved.getEmail());

            return result;

        } catch(Exception e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error.Student not inserted : email: {}", dto.getEmail());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }


    /**
     *
     * {@inheritDoc}
     */
    @Override
    public StudentReadOnlyDTO updateStudent(StudentUpdateDTO dto)
            throws EntityNotFoundException, EntityInvalidArgumentException {

        try {
            JPAHelper.beginTransaction();

            // Ensure student exists before update
            Student existing = studentDAO.getById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Student",
                            "id " + dto.getId() + " not found"));

            // Load referenced courses (replace set)
            Set<Course> courses = loadCourses(dto.getCourseIds());

            existing.setFirstname(dto.getFirstname());
            existing.setLastname(dto.getLastname());
            existing.setEmail(dto.getEmail());
            existing.setCourses(courses);

            Student updated = studentDAO.update(existing)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Student", "not updated"));

            StudentReadOnlyDTO result = Mapper.mapToStudentReadOnlyDTO(updated);

            JPAHelper.commitTransaction();
            LOGGER.info("Updated student id={} email={}", updated.getId(), updated.getEmail());

            return result;

        } catch(Exception e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Student with id {},  email {} not updated",
                    dto.getId(), dto.getEmail());
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteStudent(Object id) throws EntityNotFoundException {

        try {
            JPAHelper.beginTransaction();

            // Ensure student exists before delete
            studentDAO.getById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Student",
                            "id " + id + " not found"));

            studentDAO.delete(id);

            JPAHelper.commitTransaction();
            LOGGER.info("Deleted student id={}", id);

        } catch(Exception e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error. Student with id {} not deleted", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentReadOnlyDTO getStudentById(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            StudentReadOnlyDTO readOnlyDTO = studentDAO.getById(id)
                    .map(Mapper::mapToStudentReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException("Student", "Student with id " + id + " was not found."));

            JPAHelper.commitTransaction();
            LOGGER.info("Student with id {} was found", id);
            return readOnlyDTO;
        } catch (EntityNotFoundException e) {
            LOGGER.warn("Student with id {} was not found", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<StudentReadOnlyDTO> getAllStudents() {

        try {
            JPAHelper.beginTransaction();

            List<StudentReadOnlyDTO> readOnlyDTOS = studentDAO.getAll()
                    .stream()
                    .map(Mapper::mapToStudentReadOnlyDTO)
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
    public List<StudentReadOnlyDTO> getStudentsByCriteria(Map<String, Object> criteria) {

        try {
            JPAHelper.beginTransaction();

            List<StudentReadOnlyDTO> result = studentDAO.getByCriteria(criteria)
                    .stream()
                    .map(Mapper::mapToStudentReadOnlyDTO)
                    .collect(Collectors.toList());

            JPAHelper.commitTransaction();
            return result;

        } finally {
            JPAHelper.closeEntityManager();
        }
    }


    /**
     * Helper method for loading courses by id set.
     *
     * @param courseIds the ids to load
     * @return a set of {@link Course} entities
     * @throws EntityNotFoundException if any referenced course does not exist
     */
    private Set<Course> loadCourses(Set<Long> courseIds) throws EntityNotFoundException {
        if (courseIds == null || courseIds.isEmpty()) return new HashSet<>();

        Set<Course> courses = new HashSet<>();
        for (Long id : courseIds) {
            Course c = courseDAO.getById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Course",
                            "id " + id + " not found"));
            courses.add(c);
        }
        return courses;
    }

}
