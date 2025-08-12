package xyz.joseyamut.updatequerybuilder.repository.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static xyz.joseyamut.updatequerybuilder.util.DateTimeFormatHelper.DEFAULT_TARGET_ZONE_ID;

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
        getHelper().setTargetZoneId(DEFAULT_TARGET_ZONE_ID);

        MANDATORY_COLUMNS.add("updated_by");
        MANDATORY_COLUMNS.add("updated_on");

        getHelper().setMandatoryColumns(MANDATORY_COLUMNS);
    }

    @Override
    public void setUpdateQueryStatement(Object entity) {
        getHelper().setUpdate(entity);
    }
}
