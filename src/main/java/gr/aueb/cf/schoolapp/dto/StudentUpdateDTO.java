package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import java.util.Set;

/**
 * DTO used to update an existing Student entity.
 *
 * Requires the student's ID and allows updating firstname, lastname, email,
 * and course associations.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StudentUpdateDTO {

    /** The unique identifier of the student to update. Required. */
    @NotNull(message = "Id is required")
    private Long id;

    /** Updated first name. Required, 2-255 characters. */
    @NotNull(message = "Firstname is required")
    @Size(min = 2, max = 255, message = "Firstname should be between 2 and 255 characters.")
    private String firstname;

    /** Updated last name. Required, 2-255 characters. */
    @NotNull(message = "Lastname is required")
    @Size(min = 2, max = 255, message = "Lastname should be between 2 and 255 characters.")
    private String lastname;

    /** Updated email. Required, must be valid format and <=255 characters. */
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email should not be longer than 255 characters.")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format"
    )
    private String email;

    /** Updated set of course IDs the student is enrolled in. Optional. */
    private Set<Long> courseIds;
}
