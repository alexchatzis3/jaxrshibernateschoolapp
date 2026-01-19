package gr.aueb.cf.schoolapp.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generic Data Access Object (DAO) interface providing basic CRUD operations
 * and criteria-based queries for persistent entities.
 *
 * <p>Multiple entity-specific
 * DAO implementations can reuse these common persistence operations.</p>
 *
 * <p><strong>Type Parameters:</strong></p>
 * <ul>
 *     <li>{@code <T>} — the entity type handled by the DAO</li>
 * </ul>
 */
public interface IGenericDAO<T> {

    /**
     * Persists a new entity of type {@code T}.
     *
     * @param t the entity instance to insert
     * @return an {@link Optional} containing the stored entity,
     *         or {@link Optional#empty()} if the insert failed
     */
    Optional<T> insert(T t);

    /**
     * Updates an existing persistent entity of type {@code T}.
     *
     * @param t the entity instance with updated state
     * @return an {@link Optional} containing the updated entity,
     *         or {@link Optional#empty()} if the update failed or entity was not found
     */
    Optional<T> update(T t);

    /**
     * Deletes an entity using its identifier.
     *
     * @param id the identifier of the entity to delete
     */
    void delete(Object id);

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the identifier of the entity to fetch
     * @return an {@link Optional} containing the found entity,
     *         or {@link Optional#empty()} if no matching entity exists
     */
    Optional<T> getById(Object id);

    /**
     * Retrieves all entities of type {@code T}.
     *
     * @return a {@link List} containing all persisted entities,
     *         or an empty list if no data exists
     */
    List<T> getAll();

    /**
     * Retrieves entities matching a criteria map for the default target type.
     *
     * @param criteria a map where keys represent attribute names and values represent expected values
     * @return a {@link List} of matching entities, possibly empty
     */
    List<T> getByCriteria(Map<String, Object> criteria);

    /**
     * Retrieves entities matching a criteria map for the specified target type.
     **
     * @param clazz    the class type of the result
     * @param criteria a map where keys represent attribute names and values represent expected values
     * @return a {@link List} of matching entities, possibly empty
     */
    List<T> getByCriteria(Class<T> clazz, Map<String, Object> criteria);
}
