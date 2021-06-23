package xyz.joseyamut.updatequerybuilder.repository.interfaces;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

public class CustomRepositoryImpl implements CustomRepository {

    @PersistenceContext
    protected EntityManager manager;

    @Override
    @Transactional
    public void customQueryUpdate(String statement) {
        Query query = manager.createNativeQuery(statement);
        query.executeUpdate();
        manager.flush();
    }
}
