package xyz.joseyamut.updatequerybuilder.repository.builders;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
@Getter
@Setter
class UpdateQueryHelper {

    private String schema;
    private String table;
    private String operator;
    private Object update;
    private List<String> tableKeyColumns = new ArrayList<>();
    private List<String> ignoreColumns = new ArrayList<>();
    private List<String> mandatoryColumns = new ArrayList<>();

    String buildStatement() {
        Field[] updateEntityColumns = update.getClass().getDeclaredFields();
        List<String> columnsForUpdate = new ArrayList<>();
        List<String> queryConditions = new ArrayList<>();
        int totalColumnsForUpdateCount = 0;

        for (Field updateEntityColumn : updateEntityColumns) {
            updateEntityColumn.setAccessible(true);
            try {
                if (updateEntityColumn.get(update) != null && updateEntityColumn.getAnnotation(Column.class) != null) {
                    String columnName = updateEntityColumn.getAnnotation(Column.class).name();
                    Object columnValue = updateEntityColumn.get(update);

                    /*If these columns are present in the request, these will be ignored*/
                    if (ignoreColumns.contains(columnName)) {
                        continue;
                    }
                    /*We set this as the query conditions*/
                    if (updateEntityColumn.isAnnotationPresent(Id.class) && tableKeyColumns.contains(columnName)) {
                        queryConditions.add(conditionJoiner(columnName, columnValue.toString()));
                        continue;
                    }
                    /*Count how many non-mandatory columns to be updated*/
                    if (!mandatoryColumns.contains(columnName)) {
                        totalColumnsForUpdateCount++;
                    }

                    String keyValuePair = keyValuePairJoiner(columnName, prepareObjectType(columnValue));

                    columnsForUpdate.add(keyValuePair);
                }
            } catch (IllegalAccessException exception) {
                log.error("Error encountered: {}", exception.getMessage());
            }
        }
        /*Don't allow update if non-mandatory fields are not found*/
        if (totalColumnsForUpdateCount == 0) {
            throw new IllegalArgumentException("Error: Zero non-mandatory fields found!");
        }
        return queryStatement(columnsForUpdate, queryConditions);
    }

    private String keyValuePairJoiner(String columnName, String columnValue) {
        StringJoiner nameValuePairJoiner = new StringJoiner("=");
        nameValuePairJoiner.add(columnName);
        nameValuePairJoiner.add(columnValue);
        return nameValuePairJoiner.toString();
    }

    private String conditionJoiner(String columnName, String value) {
        StringJoiner queryConditions = new StringJoiner("=");
        queryConditions.add(columnName);
        queryConditions.add(value);
        return queryConditions.toString();
    }

    /*Prepare and sanitize the query values*/
    private String prepareObjectType(Object object) {
        StringBuilder preparedString = new StringBuilder();
        if (object instanceof String || object instanceof Timestamp) {
            String sanitized = StringEscapeUtils.escapeSql(object.toString());
            preparedString.append("'").append(sanitized).append("'");
        } else if (object instanceof Integer) {
            preparedString.append(object.toString());
        } else if (object instanceof Boolean) {
            preparedString.append(((Boolean) object).booleanValue() ? "'Y'" : "'N'");
        }
        return preparedString.toString();
    }

    private String queryStatement(List<String> columns, List<String> queryConditions) {
        StringBuilder queryStatement = new StringBuilder();
        String spacer = " ";
        queryStatement.append("UPDATE").append(spacer);
        queryStatement.append(this.schema).append(".").append(this.table).append(spacer);
        queryStatement.append("SET").append(spacer);
        queryStatement.append(String.join(",", columns)).append(spacer);
        queryStatement.append("WHERE").append(spacer);
        queryStatement.append(String.join(spacer + this.operator + spacer, queryConditions));
        log.info("Custom query: {}", queryStatement);
        return queryStatement.toString();
    }
}
