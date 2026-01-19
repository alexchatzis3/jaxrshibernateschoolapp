package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.IdentifiableEntity;
import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.*;

/**
 * Abstract generic DAO implementation providing common JPA-based CRUD operations
 * and dynamic criteria query functionality for entities implementing {@link IdentifiableEntity}.
 *
 * @param <T> the entity type handled by this DAO, must implement {@link IdentifiableEntity}
 */
public abstract class AbstractDAO<T extends IdentifiableEntity> implements IGenericDAO<T> {

    /**
     * Sets the entity class for this DAO. Usually called once when the DAO is created.
     */
    private Class<T> persistenceClass;

    public AbstractDAO() {}

    /**
     * @return the entity type associated with this DAO
     */
    public Class<T> getPersistenceClass() {
        return persistenceClass;
    }

    /**
     * Sets the persistent entity type.
     *
     * @param persistentClass the class type for T
     */
    public void setPersistenceClass(Class<T> persistentClass) {
        this.persistenceClass = persistentClass;
    }

    /**
     * Inserts a new entity into persistence context.
     */
    @Override
    public Optional<T> insert(T t) {
        EntityManager em = getEntityManager();
        em.persist(t);
        return Optional.of(t);
    }

    /**
     * Updates an existing entity instance.
     * Uses JPA merge() to apply changes.
     */
    @Override
    public Optional<T> update(T t) {
        EntityManager em = getEntityManager();
        em.merge(t); // merge returns a managed copy, although we return original for simplicity
        return Optional.of(t);
    }

    /**
     * Deletes an entity by its identifier if found.
     */
    @Override
    public void delete(Object id) {
        EntityManager em = getEntityManager();
        Optional<T> toDelete = getById(id);
        toDelete.ifPresent(em::remove);
    }

    /**
     * Finds an entity by its ID.
     */
    @Override
    public Optional<T> getById(Object id) {
        EntityManager em = getEntityManager();
        return Optional.ofNullable(em.find(persistenceClass, id));
    }

    /**
     * Retrieves all entities of type T.
     */
    @Override
    public List<T> getAll() {
        return getByCriteria(getPersistenceClass(), Collections.emptyMap());
    }

    /**
     * Retrieves all matching entities given criteria map.
     */
    @Override
    public List<T> getByCriteria(Map<String, Object> criteria) {
        return getByCriteria(getPersistenceClass(), criteria);
    }

    /**
     * Builds and executes a dynamic Criteria API query using attribute-value criteria.
     *
     * @param clazz    the entity class for result binding
     * @param criteria filtering attributes (supports nested paths via dot notation)
     */
    @Override
    public List<T> getByCriteria(Class<T> clazz, Map<String, Object> criteria) {
        EntityManager em = getEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> selectQuery = builder.createQuery(clazz);
        Root<T> entityRoot = selectQuery.from(clazz);

        // Build dynamic predicate set
        List<Predicate> predicates = getPredicatesList(builder, entityRoot, criteria);

        selectQuery.select(entityRoot).where(predicates.toArray(new Predicate[0]));
        TypedQuery<T> query = em.createQuery(selectQuery);

        // Add runtime parameters
        addParametersToQuery(query, criteria);

        return query.getResultList();
    }

    /**
     * Creates LIKE-based predicates for each criteria entry.
     *
     * <p>Supports nested attribute resolution such as "teacher.firstname".</p>
     */
    protected List<Predicate> getPredicatesList(CriteriaBuilder builder, Root<T> entityRoot, Map<String, Object> criteria) {
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Build parameter reference for safe binding
            ParameterExpression<?> val = builder.parameter(value.getClass(), buildParameterAlias(key));

            // Create LIKE predicate (string-based filtering)
            Predicate predicateLike = builder.like(
                    (Expression<String>) resolvePath(entityRoot, key),
                    (Expression<String>) val
            );
            predicates.add(predicateLike);
        }
        return predicates;
    }

    /**
     * Resolves nested paths using dot notation (e.g. "teacher.lastname").
     */
    protected Path<?> resolvePath(Root<T> root, String expression) {
        String[] fields = expression.split("\\.");
        Path<?> path = root.get(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            path = path.get(fields[i]);
        }
        return path;
    }

    /**
     * Binds query parameters safely to avoid injection and improve caching.
     */
    protected void addParametersToQuery(TypedQuery<?> query, Map<String, Object> criteria) {
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            Object value = entry.getValue();
            query.setParameter(buildParameterAlias(entry.getKey()), value);
        }
    }

    /**
     * Converts attribute names into parameter alias-friendly identifiers.
     *
     * Example: "teacher.firstname" -> "teacherfirstname"
     */
    protected String buildParameterAlias(String alias) {
        return alias.replaceAll("\\.", "");
    }

    /**
     * Provides the underlying {@link EntityManager} via helper class.
     */
    public EntityManager getEntityManager() {
        return JPAHelper.getEntityManager();
    }
}
