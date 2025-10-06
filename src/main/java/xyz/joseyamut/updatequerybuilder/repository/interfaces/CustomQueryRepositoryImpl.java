package xyz.joseyamut.updatequerybuilder.repository.interfaces;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

public class CustomQueryRepositoryImpl implements CustomQueryRepository {

    @PersistenceContext
    protected EntityManager manager;

    @Override
    @Transactional
    public void update(String statement) {
        Query query = manager.createNativeQuery(statement);
        query.executeUpdate();
        manager.flush();
    }
}
