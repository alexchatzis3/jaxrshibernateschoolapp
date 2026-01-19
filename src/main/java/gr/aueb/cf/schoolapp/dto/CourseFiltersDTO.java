package gr.aueb.cf.schoolapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object used to carry course search filters
 * from API requests.
 *
 * <p>All fields are optional and can be combined to apply
 * multiple filtering criteria.</p>
 *
 * <p>Typical use case: filtering course entities via REST
 * query parameters (e.g., title contains, teacherId equals).</p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseFiltersDTO {

    /**
     * Optional filter for course title (partial match).
     */
    private String title;

    /**
     * Optional filter for teacher identifier (exact match).
     */
    private Long teacherId;
}
