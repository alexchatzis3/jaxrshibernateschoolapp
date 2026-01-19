package gr.aueb.cf.schoolapp.model;

/**
 *Any entity implementing this interface is expected to have a primary key
 * accessible through {@link #getId()}.
 */
public interface IdentifiableEntity {
    /**
     * Returns the unique identifier of the entity. This value usually corresponds
     * to the primary key in the database.
     * @return entity ID as a Long
     */
    Long getId();
}
