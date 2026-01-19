package gr.aueb.cf.schoolapp.dto;

import lombok.*;

/**
 * Read-only Data Transfer Object (DTO) used for exposing
 * course information to API clients.
 *
 * <p>This DTO is returned from REST endpoints and includes
 * only fields that should be visible externally.
 * It does not contain any mutable or internal-only data.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseReadOnlyDTO {

    /**
     * Unique identifier of the course.
     */
    private Long id;

    /**
     * Display title of the course.
     */
    private String title;

    /**
     * The full name of the course's assigned teacher.
     * May be null if no teacher is assigned.
     */
    private String teacherName;
}
