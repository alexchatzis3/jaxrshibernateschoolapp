package gr.aueb.cf.schoolapp.mapper;

import gr.aueb.cf.schoolapp.dto.*;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.model.Teacher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between entities and DTOs.
 *
 * <p>This class contains static methods to convert between domain models
 * (Teacher, Student, Course) and their corresponding Data Transfer Objects (DTOs)
 * for use in REST APIs or service layers.</p>
 *
 * <p>All methods are static and the class cannot be instantiated.</p>
 */
public class Mapper {

    /** Private constructor to prevent instantiation. */
    private Mapper() {}

    // ===================== Teacher mappings ===================== //

    /**
     * Maps a {@link TeacherInsertDTO} to a {@link Teacher} entity.
     *
     * @param insertDTO the DTO containing teacher information
     * @return a new Teacher entity
     */
    public static Teacher mapToTeacher(TeacherInsertDTO insertDTO) {
        return new Teacher(null, insertDTO.getVat(), insertDTO.getFirstname(), insertDTO.getLastname());
    }

    /**
     * Maps a {@link TeacherUpdateDTO} to a {@link Teacher} entity.
     *
     * @param updateDTO the DTO containing updated teacher information
     * @return a Teacher entity with the provided id
     */
    public static Teacher mapToTeacher(TeacherUpdateDTO updateDTO) {
        return new Teacher(updateDTO.getId(), updateDTO.getVat(), updateDTO.getFirstname(), updateDTO.getLastname());
    }

    /**
     * Maps a {@link Teacher} entity to a read-only DTO.
     *
     * @param teacher the teacher entity
     * @return a {@link TeacherReadOnlyDTO} containing teacher info
     */
    public static TeacherReadOnlyDTO mapToTeacherReadOnlyDTO(Teacher teacher) {
        return new TeacherReadOnlyDTO(teacher.getId(), teacher.getVat(), teacher.getFirstname(), teacher.getLastname());
    }

    /**
     * Converts a list of Teacher entities to a list of read-only DTOs.
     *
     * @param teachers the list of Teacher entities
     * @return list of {@link TeacherReadOnlyDTO}
     */
    public static List<TeacherReadOnlyDTO> teachersToReadOnlyDTOs(List<Teacher> teachers) {
        return teachers.stream().map(Mapper::mapToTeacherReadOnlyDTO).collect(Collectors.toList());
    }

    // ===================== Criteria mappers ===================== //

    /**
     * Maps TeacherFiltersDTO to a criteria map for dynamic queries.
     *
     * @param filtersDTO filters for teachers
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
     * Maps CourseFiltersDTO to a criteria map for dynamic queries.
     *
     * @param filtersDTO filters for courses
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
     * Maps StudentFiltersDTO to a criteria map for dynamic queries.
     *
     * @param filtersDTO filters for students
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

    // ===================== Student mappings ===================== //

    /**
     * Maps a {@link StudentInsertDTO} to a {@link Student} entity with courses.
     *
     * @param dto student insert DTO
     * @param courses set of course entities
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
     * Maps a {@link StudentUpdateDTO} to a {@link Student} entity with courses.
     *
     * @param dto student update DTO
     * @param courses set of course entities
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
     * Maps a Student entity to a read-only DTO.
     *
     * @param student student entity
     * @return {@link StudentReadOnlyDTO} containing student info and course titles
     */
    public static StudentReadOnlyDTO mapToStudentReadOnlyDTO(Student student) {
        StudentReadOnlyDTO dto = new StudentReadOnlyDTO();
        dto.setId(student.getId());
        dto.setFirstname(student.getFirstname());
        dto.setLastname(student.getLastname());
        dto.setEmail(student.getEmail());
        dto.setCourseTitles(student.getCourses()
                .stream()
                .map(Course::getTitle)
                .collect(Collectors.toSet()));
        return dto;
    }

    // ===================== Course mappings ===================== //

    /**
     * Maps a {@link CourseInsertDTO} to a {@link Course} entity with the given teacher.
     *
     * @param dto course insert DTO
     * @param teacher teacher entity assigned to course
     * @return new Course entity
     */
    public static Course mapToCourse(CourseInsertDTO dto, Teacher teacher) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setTeacher(teacher);
        return course;
    }

    /**
     * Maps a {@link CourseUpdateDTO} to an existing {@link Course} entity.
     *
     * @param dto course update DTO
     * @param teacher teacher entity assigned to course
     * @param existing existing Course entity to update
     * @return updated Course entity
     */
    public static Course mapToCourse(CourseUpdateDTO dto, Teacher teacher, Course existing) {
        existing.setTitle(dto.getTitle());
        existing.setTeacher(teacher);
        return existing;
    }

    /**
     * Maps a Course entity to a read-only DTO.
     *
     * @param course course entity
     * @return {@link CourseReadOnlyDTO} with course info and teacher name
     */
    public static CourseReadOnlyDTO mapToCourseReadOnlyDTO(Course course) {
        CourseReadOnlyDTO dto = new CourseReadOnlyDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setTeacherName(course.getTeacher() != null ?
                course.getTeacher().getFirstname() + " " + course.getTeacher().getLastname() : null);
        return dto;
    }
}
