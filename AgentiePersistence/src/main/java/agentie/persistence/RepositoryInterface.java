package agentie.persistence;

public interface RepositoryInterface<ID, E> {
    int size();

    void save(E entity);

    void delete(ID id);

    void update(ID id, E entity);

    E findOne(ID id);

    Iterable<E> findAll();
}
