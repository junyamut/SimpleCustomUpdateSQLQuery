package xyz.joseyamut.updatequerybuilder.repository.builders;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
public class FoodCategoryQueryBuilder extends BaseQueryBuilder {

    private final String TABLE_NAME = "food_category";
    private final List<String> PRIMARY_KEY = Collections.singletonList("food_category_id");

    @PostConstruct
    public void initializeQueryBuilder() {
        getHelper().setSchema(SCHEMA_NAME);
        getHelper().setTable(TABLE_NAME);
        getHelper().setOperator(AND_OPERATOR);
        getHelper().setTableKeyColumns(PRIMARY_KEY);
    }

    @Override
    public void setUpdateQueryStatement(Object entity) {
        getHelper().setUpdate(entity);
    }
}
