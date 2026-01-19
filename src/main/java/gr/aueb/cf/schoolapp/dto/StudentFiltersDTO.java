package gr.aueb.cf.schoolapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO used to filter Student entities when querying the database.
 *
 * <p>All fields are optional. Any combination of these fields
 * can be used to narrow down search results.</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentFiltersDTO {

    /** Optional filter by student's first name. */
    private String firstname;

    /** Optional filter by student's last name. */
    private String lastname;

    /** Optional filter by student's email. */
    private String email;
}
