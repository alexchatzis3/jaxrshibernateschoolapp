package gr.aueb.cf.schoolapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a student enrolled in one or more {@link Course} entities within the school domain.
 *
 * <p>This entity extends {@link AbstractEntity} which provides auditing metadata
 * (creation timestamp, update timestamp, UUID) and implements {@link IdentifiableEntity}
 * to expose a unified primary key getter.</p>
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "students")
public class Student extends AbstractEntity implements IdentifiableEntity {

    /**
     * Primary auto-generated identifier for the student.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Optional student first name.
     */
    private String firstname;

    /**
     * Optional student last name.
     */
    private String lastname;

    /**
     * Unique and non-null email address.
     * Acts as a user-level natural identifier for lookup and validation.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Represents all courses attended by the student.
     *
     * <p>Relationship type: Many-to-Many</p>
     *
     * <p>The join table <strong>students_courses</strong> contains:</p>
     * <ul>
     *     <li>{@code student_id} — foreign key to the Student entity</li>
     *     <li>{@code course_id} — foreign key to the Course entity</li>
     * </ul>
     *
     * <p>This side is the owning side of the relationship since the join table
     * is defined here.</p>
     */
    @ManyToMany
    @JoinTable(
            name = "students_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}
