package xyz.joseyamut.updatequerybuilder.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

public class ExtendedRepositoryDefinition implements ExtendedRepository {

    private final EntityManager entityManager;

    public ExtendedRepositoryDefinition(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void update(String statement) {
        Query query = entityManager.createNativeQuery(statement);
        query.executeUpdate();
        entityManager.flush();
    }
}
