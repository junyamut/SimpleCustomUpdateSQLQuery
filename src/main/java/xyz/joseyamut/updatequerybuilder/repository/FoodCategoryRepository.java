package xyz.joseyamut.updatequerybuilder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity, Integer>, ExtendedRepository {

    @Query(
            value = "SELECT * FROM food_category ORDER BY food_category_id ASC",
            nativeQuery = true
    )
    Page<FoodCategoryEntity> listByPageOrderById(Pageable pageable);

    @Query(
            value = "SELECT * FROM food_category ORDER BY name ASC",
            nativeQuery = true
    )
    Page<FoodCategoryEntity> listByPageOrderByName(Pageable pageable);

}
