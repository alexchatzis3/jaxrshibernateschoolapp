package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import gr.aueb.cf.schoolapp.model.Course;

/**
 * Data Transfer Object (DTO) used for inserting a new
 * {@link Course} into the system.
 *
 * <p>Contains only the necessary fields for creation and
 * includes basic validation constraints.</p>
 *
 * <p>This DTO is typically sent by clients when creating new courses via REST.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseInsertDTO {

    /**
     * The course title.
     * Must not be null and must follow length constraints.
     */
    @NotNull(message = "Title is required")
    @Size(min = 2, max = 255, message = "Title should be between 2 and 255 characters.")
    private String title;

    /**
     * The ID of the teacher assigned to the course.
     * Optional: may be null if the course is created without teacher assignment.
     */
    private Long teacherId;
}
