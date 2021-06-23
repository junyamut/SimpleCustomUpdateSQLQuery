package xyz.joseyamut.updatequerybuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;
import xyz.joseyamut.updatequerybuilder.repository.interfaces.CustomRepository;

@EnableJpaRepositories
public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity, Integer>, CustomRepository {
}
