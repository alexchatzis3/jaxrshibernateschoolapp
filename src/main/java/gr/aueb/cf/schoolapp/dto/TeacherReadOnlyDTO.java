package gr.aueb.cf.schoolapp.dto;

import lombok.*;
import gr.aueb.cf.schoolapp.model.Teacher;

/**
 * Read-only Data Transfer Object (DTO) used for exposing
 * {@link Teacher} data to clients.
 *
 * <p>This DTO is typically used in responses for:</p>
 * <ul>
 *     <li>fetching a single teacher</li>
 *     <li>fetching teacher lists</li>
 *     <li>returning results after create/update operations</li>
 * </ul>
 *
 * <p>It contains only fields that should be visible externally and
 * does not include sensitive or internal metadata.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeacherReadOnlyDTO {

    /** Unique identifier of the teacher. */
    private Long id;

    /** Public VAT identifier of the teacher. */
    private String vat;

    /** First name of the teacher. */
    private String firstname;

    /** Last name of the teacher. */
    private String lastname;
}
