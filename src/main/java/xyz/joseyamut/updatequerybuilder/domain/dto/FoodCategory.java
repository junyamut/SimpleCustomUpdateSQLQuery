package xyz.joseyamut.updatequerybuilder.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class FoodCategory {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_on")
    private String createdOn;

    @JsonProperty("updated_by")
    private String updatedBy;

    @JsonProperty("updated_on")
    private String updatedOn;

    @JsonProperty("is_active")
    private boolean isActive;

    public void populate(FoodCategoryEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.groupId = entity.getGroupId();
        this.createdBy = entity.getCreatedBy();
        this.createdOn = formatDate(entity.getCreatedOn());
        this.updatedBy = entity.getUpdatedBy();
        this.updatedOn = formatDate(entity.getUpdatedOn());
        this.isActive = entity.getIsActive().equalsIgnoreCase("Y");
    }

    public void update(FoodCategoryEntity entity) {
        populate(entity);
        this.updatedBy = entity.getUpdatedBy();
        this.updatedOn = formatDate(entity.getUpdatedOn());
    }

    private String formatDate(Timestamp timestamp) {
        if (timestamp != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
            return dateFormat.format(new Date(timestamp.getTime()));
        }
        return null;
    }
}
