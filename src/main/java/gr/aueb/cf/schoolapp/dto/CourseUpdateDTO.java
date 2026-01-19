package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import gr.aueb.cf.schoolapp.model.Course;

/**
 * Data Transfer Object (DTO) used for updating an existing
 * {@link Course}.
 *
 * <p>Contains only updatable fields and includes validation
 * to ensure request integrity. Clients typically send this DTO
 * when modifying course data through REST APIs.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseUpdateDTO {

    /**
     * The identifier of the course to update.
     * Must not be null.
     */
    @NotNull(message = "Id is required")
    private Long id;

    /**
     * Updated course title.
     * Must not be null and must follow defined length rules.
     */
    @NotNull(message = "Title is required")
    @Size(min = 2, max = 255, message = "Title should be between 2 and 255 characters.")
    private String title;

    /**
     * Updated teacher assignment.
     * Optional: may be null if no reassignment is desired.
     */
    private Long teacherId;
}
