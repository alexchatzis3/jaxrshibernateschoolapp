package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

/**
 * DTO used to insert a new Student entity.
 *
 * Contains validation rules for firstname, lastname, email,
 * and a set of course IDs to associate the student with.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentInsertDTO {

    /** The student's first name. Required, 2-255 characters. */
    @NotNull(message = "Firstname is required")
    @Size(min = 2, max = 255, message = "Firstname should be between 2 and 255 characters.")
    private String firstname;

    /** The student's last name. Required, 2-255 characters. */
    @NotNull(message = "Lastname is required")
    @Size(min = 2, max = 255, message = "Lastname should be between 2 and 255 characters.")
    private String lastname;

    /** The student's email. Must be unique and follow email format. */
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email should not be longer than 255 characters.")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format"
    )
    private String email;

    /** IDs of courses the student will be enrolled in. Optional. */
    private Set<Long> courseIds;
}
