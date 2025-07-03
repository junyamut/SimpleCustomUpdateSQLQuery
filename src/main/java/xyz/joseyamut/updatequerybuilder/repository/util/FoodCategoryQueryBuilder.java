package xyz.joseyamut.updatequerybuilder.repository.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FoodCategoryQueryBuilder extends BaseQueryBuilder {

    private final String TABLE_NAME = "food_category";
    private final List<String> PRIMARY_KEY = Collections.singletonList("food_category_id");
    private List<String> additionalIgnoreColumns = new ArrayList<>();

    @PostConstruct
    public void initializeQueryBuilder() {
        Stream<String> combined = Stream.concat(IGNORE_COLUMNS.stream(), additionalIgnoreColumns.stream());
        getHelper().setIgnoreColumns(combined.collect(Collectors.toList()));
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
