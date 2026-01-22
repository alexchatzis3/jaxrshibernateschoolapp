package gr.aueb.cf.schoolapp.mapper;

import gr.aueb.cf.schoolapp.core.enums.RoleType;
import gr.aueb.cf.schoolapp.dto.*;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.security.SecUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between domain entities and DTOs.
 *
 * <p>This class provides static methods to convert entities (Teacher, Student, Course, User)
 * to their corresponding Data Transfer Objects (DTOs) for REST responses or service layers.
 *
 * <p>It also provides mapping from filter DTOs to criteria maps for dynamic queries.
 *
 * <p>This class cannot be instantiated; all methods are static.
 */
public class Mapper {

    /** Private constructor to prevent instantiation */
    private Mapper() {}

    // ===================== TEACHER ===================== //

    /**
     * Converts a TeacherInsertDTO to a Teacher entity.
     *
     * @param insertDTO the input DTO
     * @return a new Teacher entity
     */
    public static Teacher mapToTeacher(TeacherInsertDTO insertDTO) {
        return new Teacher(null, insertDTO.getVat(), insertDTO.getFirstname(), insertDTO.getLastname());
    }

    /**
     * Converts a TeacherUpdateDTO to a Teacher entity.
     *
     * @param updateDTO the input DTO
     * @return a Teacher entity with the given ID
     */
    public static Teacher mapToTeacher(TeacherUpdateDTO updateDTO) {
        return new Teacher(updateDTO.getId(), updateDTO.getVat(), updateDTO.getFirstname(), updateDTO.getLastname());
    }

    /**
     * Converts a Teacher entity to a read-only DTO.
     *
     * @param teacher entity
     * @return TeacherReadOnlyDTO
     */
    public static TeacherReadOnlyDTO mapToTeacherReadOnlyDTO(Teacher teacher) {
        return new TeacherReadOnlyDTO(
                teacher.getId(),
                teacher.getVat(),
                teacher.getFirstname(),
                teacher.getLastname()
        );
    }

    /**
     * Converts a list of Teacher entities to a list of read-only DTOs.
     *
     * @param teachers list of entities
     * @return list of TeacherReadOnlyDTO
     */
    public static List<TeacherReadOnlyDTO> teachersToReadOnlyDTOs(List<Teacher> teachers) {
        return teachers.stream().map(Mapper::mapToTeacherReadOnlyDTO).collect(Collectors.toList());
    }

    // ===================== FILTER CRITERIA ===================== //

    /**
     * Maps TeacherFiltersDTO to a criteria map for queries.
     *
     * @param filtersDTO the filter DTO
     * @return map of non-null criteria
     */
    public static Map<String, Object> mapToCriteria(TeacherFiltersDTO filtersDTO) {
        Map<String, Object> filters = new HashMap<>();
        if (filtersDTO.getFirstname() != null && !filtersDTO.getFirstname().isEmpty()) {
            filters.put("firstname", filtersDTO.getFirstname());
        }
        if (filtersDTO.getLastname() != null && !filtersDTO.getLastname().isEmpty()) {
            filters.put("lastname", filtersDTO.getLastname());
        }
        if (filtersDTO.getVat() != null && !filtersDTO.getVat().isEmpty()) {
            filters.put("vat", filtersDTO.getVat());
        }
        return filters;
    }

    /**
     * Maps CourseFiltersDTO to a criteria map for queries.
     *
     * @param filtersDTO the filter DTO
     * @return map of non-null criteria
     */
    public static Map<String, Object> mapToCriteria(CourseFiltersDTO filtersDTO) {
        Map<String, Object> filters = new HashMap<>();
        if (filtersDTO.getTitle() != null && !filtersDTO.getTitle().isEmpty()) {
            filters.put("title", filtersDTO.getTitle());
        }
        if (filtersDTO.getTeacherId() != null) {
            filters.put("teacherId", filtersDTO.getTeacherId());
        }
        return filters;
    }

    /**
     * Maps StudentFiltersDTO to a criteria map for queries.
     *
     * @param filtersDTO the filter DTO
     * @return map of non-null criteria
     */
    public static Map<String, Object> mapToCriteria(StudentFiltersDTO filtersDTO) {
        Map<String, Object> filters = new HashMap<>();
        if (filtersDTO.getFirstname() != null && !filtersDTO.getFirstname().isEmpty()) {
            filters.put("firstname", filtersDTO.getFirstname());
        }
        if (filtersDTO.getLastname() != null && !filtersDTO.getLastname().isEmpty()) {
            filters.put("lastname", filtersDTO.getLastname());
        }
        if (filtersDTO.getEmail() != null && !filtersDTO.getEmail().isEmpty()) {
            filters.put("email", filtersDTO.getEmail());
        }
        return filters;
    }

    // ===================== STUDENT ===================== //

    /**
     * Maps StudentInsertDTO to a Student entity with courses.
     *
     * @param dto input DTO
     * @param courses associated courses
     * @return new Student entity
     */
    public static Student mapToStudent(StudentInsertDTO dto, Set<Course> courses) {
        Student student = new Student();
        student.setFirstname(dto.getFirstname());
        student.setLastname(dto.getLastname());
        student.setEmail(dto.getEmail());
        student.setCourses(courses);
        return student;
    }

    /**
     * Maps StudentUpdateDTO to a Student entity with courses.
     *
     * @param dto input DTO
     * @param courses associated courses
     * @return updated Student entity
     */
    public static Student mapToStudent(StudentUpdateDTO dto, Set<Course> courses) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setFirstname(dto.getFirstname());
        student.setLastname(dto.getLastname());
        student.setEmail(dto.getEmail());
        student.setCourses(courses);
        return student;
    }

    /**
     * Converts a Student entity to a read-only DTO.
     *
     * @param student entity
     * @return StudentReadOnlyDTO with course titles
     */
    public static StudentReadOnlyDTO mapToStudentReadOnlyDTO(Student student) {
        StudentReadOnlyDTO dto = new StudentReadOnlyDTO();
        dto.setId(student.getId());
        dto.setFirstname(student.getFirstname());
        dto.setLastname(student.getLastname());
        dto.setEmail(student.getEmail());
        dto.setCourseTitles(student.getCourses().stream()
                .map(Course::getTitle)
                .collect(Collectors.toSet()));
        return dto;
    }

    // ===================== COURSE ===================== //

    /**
     * Maps CourseInsertDTO to a Course entity with teacher.
     *
     * @param dto input DTO
     * @param teacher assigned teacher
     * @return new Course entity
     */
    public static Course mapToCourse(CourseInsertDTO dto, Teacher teacher) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setTeacher(teacher);
        return course;
    }

    /**
     * Updates an existing Course entity from a CourseUpdateDTO.
     *
     * @param dto input DTO
     * @param teacher assigned teacher
     * @param existing existing course entity
     * @return updated Course entity
     */
    public static Course mapToCourse(CourseUpdateDTO dto, Teacher teacher, Course existing) {
        existing.setTitle(dto.getTitle());
        existing.setTeacher(teacher);
        return existing;
    }

    /**
     * Converts a Course entity to a read-only DTO.
     *
     * @param course entity
     * @return CourseReadOnlyDTO with teacher name
     */
    public static CourseReadOnlyDTO mapToCourseReadOnlyDTO(Course course) {
        CourseReadOnlyDTO dto = new CourseReadOnlyDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setTeacherName(course.getTeacher() != null
                ? course.getTeacher().getFirstname() + " " + course.getTeacher().getLastname()
                : null);
        return dto;
    }

    // ===================== USER ===================== //

    /**
     * Maps UserInsertDTO to a User entity with hashed password.
     *
     * @param dto input DTO
     * @return new User entity
     */
    public static User mapToUser(UserInsertDTO dto) {
        return new User(
                null,
                dto.getUsername(),
                SecUtil.hashPassword(dto.getPassword()),
                RoleType.valueOf(dto.getRole())
        );
    }

    /**
     * Converts a User entity to a read-only DTO.
     *
     * @param user entity
     * @return UserReadOnlyDTO
     */
    public static UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoleType().name()
        );
    }
}
