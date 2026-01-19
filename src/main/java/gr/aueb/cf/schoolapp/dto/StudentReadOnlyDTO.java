package gr.aueb.cf.schoolapp.dto;

import lombok.*;

import java.util.Set;

/**
 * Read-only Data Transfer Object (DTO) for Student entities.
 *
 * <p>This DTO is used to send student information to clients
 * without exposing the internal entity. It includes the student's
 * basic information and the titles of the courses they are enrolled in.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StudentReadOnlyDTO {

    /** Unique identifier of the student. */
    private Long id;

    /** First name of the student. */
    private String firstname;

    /** Last name of the student. */
    private String lastname;

    /** Email of the student. Must be unique. */
    private String email;

    /** Titles of the courses this student is enrolled in. */
    private Set<String> courseTitles;
}
