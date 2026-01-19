package gr.aueb.cf.schoolapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Base abstract entity that provides common fields and lifecycle audit behavior
 * for all persistent entities in the application.
 *
 * <p>This class defines:</p>
 * <ul>
 *     <li>Automatic creation and update timestamps</li>
 *     <li>A unique immutable UUID for external referencing</li>
 *     <li>Support for JPA entity inheritance via {@code @MappedSuperclass}</li>
 * </ul>
 *
 * <p>Entities extending this class inherit audit capabilities without
 * repeating boilerplate code.</p>
 *
 * <p>Annotation details:</p>
 * <ul>
 *     <li>{@code @MappedSuperclass} — ensures fields are mapped to the database</li>
 *     <li>{@code @DynamicUpdate} — prevents updating unchanged fields in UPDATE queries</li>
 *     <li>Lifecycle callbacks {@code @PrePersist} and {@code @PreUpdate}</li>
 * </ul>
 */
@Getter
@Setter
@DynamicUpdate
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    /**
     * Timestamp indicating when the entity was created.
     * Set automatically and never updated afterward.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp indicating the last time the entity was updated.
     * Automatically refreshed before persistence on update operations.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Global immutable and unique UUID that can be safely exposed externally
     * without leaking internal database IDs.
     */
    @Column(unique = true, updatable = false, nullable = false)
    private String uuid;

    /**
     * JPA lifecycle callback invoked before initial persistence.
     * Initializes timestamps and UUID.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }

    /**
     * JPA lifecycle callback invoked before an entity update operation.
     * Refreshes the update timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
