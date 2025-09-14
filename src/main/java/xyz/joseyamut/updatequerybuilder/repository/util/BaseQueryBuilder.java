package xyz.joseyamut.updatequerybuilder.repository.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseQueryBuilder {

    private UpdateQueryHelper updateQueryHelper = new UpdateQueryHelper();

    public final String SCHEMA_NAME = "food_store";
    public final String AND_OPERATOR = "AND";
    public final String OR_OPERATOR = "OR";
    public final List<String> IGNORE_COLUMNS = Arrays.asList("created_by", "created_on");
    public final List<String> MANDATORY_COLUMNS = new ArrayList<>();

    protected abstract void setUpdateQueryStatement(Object entity);

    public String getUpdateQueryStatement() {
        return updateQueryHelper.buildStatement();
    }

    protected UpdateQueryHelper getHelper() {
        if (updateQueryHelper.getIgnoreColumns().isEmpty()) {
            updateQueryHelper.setIgnoreColumns(IGNORE_COLUMNS);
        }

        if (updateQueryHelper.getMandatoryColumns().isEmpty()) {
            updateQueryHelper.setMandatoryColumns(MANDATORY_COLUMNS);
        }

        return updateQueryHelper;
    }
}
