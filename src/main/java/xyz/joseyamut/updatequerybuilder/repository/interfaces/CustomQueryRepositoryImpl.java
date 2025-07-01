package xyz.joseyamut.updatequerybuilder.repository.interfaces;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

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
