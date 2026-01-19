package gr.aueb.cf.schoolapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object used to carry teacher search filters
 * from API requests.
 *
 * <p>All fields are optional and may be used in combination.
 * Typical usage: filtering teacher entities via REST query
 * parameters.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherFiltersDTO {

    /**
     * Optional filter for teacher's first name (partial match).
     */
    private String firstname;

    /**
     * Optional filter for teacher's last name (partial match).
     */
    private String lastname;

    /**
     * Optional filter for teacher's VAT number (exact match).
     */
    private String vat;
}
