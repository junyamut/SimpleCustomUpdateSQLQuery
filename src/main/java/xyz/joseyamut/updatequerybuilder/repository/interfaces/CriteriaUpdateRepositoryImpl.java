package xyz.joseyamut.updatequerybuilder.repository.interfaces;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.lang.reflect.Field;

@Slf4j
public class CriteriaUpdateRepositoryImpl implements CriteriaUpdateRepository {

    @PersistenceContext
    protected EntityManager manager;

    @Override
    @Transactional
    public <T> void update(T entity, Class<T> clazz) throws IllegalAccessException {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaUpdate<T> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(clazz);
        Root<T> root = criteriaUpdate.from(clazz);
        Integer id = 0;
        String entityIdName = "";
        Field[] entityColumns = entity.getClass().getDeclaredFields();
        for (Field entityColumn : entityColumns) {
            entityColumn.setAccessible(true);
            if (entityColumn.get(entity) == null || entityColumn.getAnnotation(Column.class) == null) {
                continue;
            }
            String columnName = entityColumn.getName();
            Object columnValue = entityColumn.get(entity);
            if (entityColumn.isAnnotationPresent(Id.class)) {
                id = (Integer) columnValue;
                entityIdName = columnName;
                log.debug("Update on {} with ID: {}", clazz, id);
                continue;
            }
            criteriaUpdate.set(root.get(columnName), columnValue);
        }
        criteriaUpdate.where(criteriaBuilder.equal(root.get(entityIdName), id));
        manager.createQuery(criteriaUpdate).executeUpdate();
        manager.flush();
    }

}
