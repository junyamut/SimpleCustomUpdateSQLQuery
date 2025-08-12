package xyz.joseyamut.updatequerybuilder.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;
import xyz.joseyamut.updatequerybuilder.util.DateTimeFormatHelper;

import java.sql.Timestamp;
import java.time.ZoneId;

import static xyz.joseyamut.updatequerybuilder.util.DateTimeFormatHelper.DEFAULT_TARGET_ZONE_ID;

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
    private Boolean isActive;

    public void populate(FoodCategoryEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.groupId = entity.getGroupId();
        this.createdBy = entity.getCreatedBy();
        this.createdOn = displayAsLocalDateTime(entity.getCreatedOn());
        this.updatedBy = entity.getUpdatedBy();
        this.updatedOn = displayAsLocalDateTime(entity.getUpdatedOn());
        this.isActive = entity.getIsActive().equalsIgnoreCase("Y");
    }

    private String displayAsLocalDateTime(Timestamp timestamp) {
        if (timestamp != null) {
            return DateTimeFormatHelper.convertWithZonedDateTime(timestamp,
                    DEFAULT_TARGET_ZONE_ID, ZoneId.systemDefault().toString(),
                    "yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }
}
