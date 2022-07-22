package data;

import java.util.Collection;

/**
 * Interface for CRUD operations on entities.
 *
 * @param <E> type of the entity this DAO operates on
 */
public interface DataAccessObject<E> {

    /**
     * Creates a new entity using the underlying data source
     *
     * @param entity entity to be persisted
     * @throws DataAccessException when anything goes wrong with the data source
     */
    void create(E entity) throws DataAccessException;

    /**
     * Reads all entities from the underlying data source
     *
     * @return collection of all entities known to the data source
     * @throws DataAccessException when anything goes wrong with the underlying data source
     */
    Collection<E> findAll() throws DataAccessException;

    /**
     * Updates an entity using the underlying data source
     *
     * @param entity entity to be deleted
     * @throws DataAccessException when anything goes wrong with the data source
     */
    void update(E entity) throws DataAccessException;

    /**
     * Deletes an entity using the underlying data source
     *
     * @param entity entity to be deleted
     * @throws DataAccessException when anything goes wrong with the data source
     */
    void delete(E entity) throws DataAccessException;
}
