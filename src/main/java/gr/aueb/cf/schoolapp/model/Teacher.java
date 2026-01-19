package gr.aueb.cf.schoolapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Teacher entity within the school application domain.
 *
 * <p>This entity extends {@link AbstractEntity}, inheriting audit fields such as
 * creation/update timestamps and a unique UUID. It also implements
 * {@link IdentifiableEntity} to standardize access to the primary identifier.</p>
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "teachers")
public class Teacher extends AbstractEntity implements IdentifiableEntity {

    /**
     * Primary database identifier, auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Teacher VAT (Tax Identification Number).
     * Must be unique within the system.
     *
     */
    @Column(unique = true)
    private String vat;

    /**
     * Teacher first name.
     */
    private String firstname;

    /**
     * Teacher last name.
     */
    private String lastname;
}
