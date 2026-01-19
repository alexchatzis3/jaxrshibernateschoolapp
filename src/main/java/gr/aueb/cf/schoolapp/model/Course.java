package gr.aueb.cf.schoolapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a course taught by a {@link Teacher} within the school application domain.
 *
 * <p>This entity extends {@link AbstractEntity} which provides common audit metadata
 * such as creation and update timestamps along with a unique UUID.
 * It also implements {@link IdentifiableEntity} to provide uniform access to
 * the primary key.</p>
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "courses")
public class Course extends AbstractEntity implements IdentifiableEntity {

    /**
     * Primary auto-generated identifier of the course.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique course title.
     */
    @Column(unique = true)
    private String title;

    /**
     * The teacher responsible for teaching this course.
     *
     * <p>Relationship type: Many-to-One</p>
     *
     * <p>Fetching mode is set to LAZY to avoid prematurely loading teacher data
     * unless explicitly accessed.</p>
     *
     * <p>The foreign key column {@code teacher_id} is stored in the "courses" table.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
