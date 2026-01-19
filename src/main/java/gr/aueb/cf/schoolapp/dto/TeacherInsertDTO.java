package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import gr.aueb.cf.schoolapp.model.Teacher;

/**
 * Data Transfer Object (DTO) used for inserting a new {@link Teacher} entity.
 * <p>
 * This DTO carries only the fields required to create a teacher and applies
 * basic validation rules for input safety.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherInsertDTO {

    /**
     * Teacher's first name.
     * Cannot be null and must follow basic size constraints.
     */
    @NotNull(message = "Firstname is required")
    @Size(min = 2, max = 255, message = "Firstname should be between 2 and 255 characters.")
    private String firstname;

    /**
     * Teacher's last name.
     * Cannot be null and must follow basic size constraints.
     */
    @NotNull(message = "Lastname is required")
    @Size(min = 2, max = 255, message = "Lastname should be between 2 and 255 characters.")
    private String lastname;

    /**
     * Teacher's VAT number.
     * Must consist of exactly 9 characters.
     */
    @NotNull(message = "Vat is required")
    @Size(min = 9, max = 9, message = "Vat must include exactly 9 digits.")
    private String vat;
}
