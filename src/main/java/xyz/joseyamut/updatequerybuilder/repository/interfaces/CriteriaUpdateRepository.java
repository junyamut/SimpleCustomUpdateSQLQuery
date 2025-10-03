package xyz.joseyamut.updatequerybuilder.repository.interfaces;

public interface CriteriaUpdateRepository {
    <T> void update(T entity, Class<T> clazz) throws IllegalAccessException;
}
