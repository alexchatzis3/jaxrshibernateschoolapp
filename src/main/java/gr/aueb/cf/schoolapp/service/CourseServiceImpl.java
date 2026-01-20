package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dao.ICourseDAO;
import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dto.CourseInsertDTO;
import gr.aueb.cf.schoolapp.dto.CourseReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.CourseUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service layer implementation for managing {@code Course} entities.
 *
 * <p>This class handles business logic, validation, transactions,
 * data transformations (DTO ↔ Entity), and exception handling.</p>
 *
 * <p>DAO operations are delegated to {@link ICourseDAO} and {@link ITeacherDAO}.</p>
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CourseServiceImpl implements ICourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

    /** DAO layer dependency for course operations. */
    private final ICourseDAO courseDAO;

    /** DAO layer dependency for teacher operations. */
    private final ITeacherDAO teacherDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseReadOnlyDTO insertCourse(CourseInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();

            // Ensure course title is unique
            if (courseDAO.getCourseByTitle(insertDTO.getTitle()).isPresent()) {
                throw new EntityAlreadyExistsException("Course",
                        "Course with title: " + insertDTO.getTitle() + " already exists");
            }

            // Load teacher for this course
            Teacher teacher = teacherDAO.getById(insertDTO.getTeacherId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher",
                            "Teacher with id " + insertDTO.getTeacherId() + " not found"));

            Course course = Mapper.mapToCourse(insertDTO, teacher);

            Course saved = courseDAO.insert(course)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Course",
                            "Course with title " + insertDTO.getTitle() + " not inserted"));

            CourseReadOnlyDTO result = Mapper.mapToCourseReadOnlyDTO(saved);

            JPAHelper.commitTransaction();
            LOGGER.info("Course inserted successfully: id={}, title={}", saved.getId(), result.getTitle());
            return result;
        } catch (Exception e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error inserting course: title={}", insertDTO.getTitle(), e);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseReadOnlyDTO updateCourse(CourseUpdateDTO updateDTO)
            throws EntityInvalidArgumentException, EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();

            Course existing = courseDAO.getById(updateDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Course",
                            "Course with id " + updateDTO.getId() + " not found"));

            Teacher teacher = teacherDAO.getById(updateDTO.getTeacherId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher",
                            "Teacher with id " + updateDTO.getTeacherId() + " not found"));

            existing.setTitle(updateDTO.getTitle());
            existing.setTeacher(teacher);

            Course updated = courseDAO.update(existing)
                    .orElseThrow(() -> new EntityInvalidArgumentException("Course", "Error during update"));

            CourseReadOnlyDTO result = Mapper.mapToCourseReadOnlyDTO(updated);

            JPAHelper.commitTransaction();
            LOGGER.info("Course updated successfully: id={}, title={}", updated.getId(), updated.getTitle());
            return result;
        } catch (Exception e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error updating course: id={}, title={}", updateDTO.getId(), updateDTO.getTitle(), e);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCourse(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();

            courseDAO.getById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Course",
                            "Course with id " + id + " not found"));

            courseDAO.delete(id);

            JPAHelper.commitTransaction();
            LOGGER.info("Course deleted successfully: id={}", id);
        } catch (Exception e) {
            JPAHelper.rollbackTransaction();
            LOGGER.error("Error deleting course: id={}", id, e);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseReadOnlyDTO getCourseById(Object id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();

            CourseReadOnlyDTO readOnlyDTO = courseDAO.getById(id)
                    .map(Mapper::mapToCourseReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException("Course",
                            "Course with id " + id + " not found"));

            JPAHelper.commitTransaction();
            LOGGER.info("Course found: id={}", id);
            return readOnlyDTO;
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LOGGER.warn("Course not found: id={}", id);
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseReadOnlyDTO> getAllCourses() {
        try {
            JPAHelper.beginTransaction();

            List<CourseReadOnlyDTO> result = courseDAO.getAll()
                    .stream()
                    .map(Mapper::mapToCourseReadOnlyDTO)
                    .toList();

            JPAHelper.commitTransaction();
            return result;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseReadOnlyDTO> getCoursesByCriteria(Map<String, Object> criteria) {
        try {
            JPAHelper.beginTransaction();

            List<CourseReadOnlyDTO> result = courseDAO.getByCriteria(criteria)
                    .stream()
                    .map(Mapper::mapToCourseReadOnlyDTO)
                    .toList();

            JPAHelper.commitTransaction();
            return result;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }
}
