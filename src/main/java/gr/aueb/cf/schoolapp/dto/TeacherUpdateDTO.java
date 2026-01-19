package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import gr.aueb.cf.schoolapp.model.Teacher;

/**
 * Data Transfer Object (DTO) used for updating an existing
 * {@link Teacher} entity.
 * <p>
 * This DTO carries both the identifier of the teacher and
 * the fields that can be modified.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherUpdateDTO {

    /**
     * Unique identifier of the teacher to update.
     * Must not be null.
     */
    @NotNull(message = "Id is required")
    private Long id;

    /**
     * Updated first name of the teacher.
     * Cannot be null and must follow basic size rules.
     */
    @NotNull(message = "Firstname is required")
    @Size(min = 2, max = 255, message = "Firstname should be between 2 and 255 characters.")
    private String firstname;

    /**
     * Updated last name of the teacher.
     * Cannot be null and must follow basic size rules.
     */
    @NotNull(message = "Lastname is required")
    @Size(min = 2, max = 255, message = "Lastname should be between 2 and 255 characters.")
    private String lastname;

    /**
     * Updated VAT number of the teacher.
     * Must contain exactly 9 characters.
     */
    @NotNull(message = "Vat is required")
    @Size(min = 9, max = 9, message = "Vat must include exactly 9 digits.")
    private String vat;
}
