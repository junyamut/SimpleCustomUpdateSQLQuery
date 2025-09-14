package xyz.joseyamut.updatequerybuilder.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.joseyamut.updatequerybuilder.domain.constraints.CreateFoodCategoryGroup;
import xyz.joseyamut.updatequerybuilder.domain.constraints.UpdateFoodCategoryGroup;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;
import xyz.joseyamut.updatequerybuilder.util.DateTimeFormatHelper;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    @NotBlank(groups = {CreateFoodCategoryGroup.class})
    @Size(min = 3, max = 64, message = "Value of {name} field must be at least 3 chars and not more than 64 chars.", groups = {CreateFoodCategoryGroup.class})
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("group_id")
    @NotBlank(groups = {CreateFoodCategoryGroup.class, UpdateFoodCategoryGroup.class})
    @Min(value = 1, message = "Value of {group_id} field cannot be less than 1.", groups = {CreateFoodCategoryGroup.class, UpdateFoodCategoryGroup.class})
    private Integer groupId;

    @JsonProperty("created_by")
    @NotBlank(groups = {CreateFoodCategoryGroup.class})
    @Size(min = 3, max = 48, message = "Value of {created_by} field must be at least 3 chars and not more than 254 chars.", groups = {CreateFoodCategoryGroup.class})
    private String createdBy;

    @JsonProperty("created_on")
    private String createdOn;

    @JsonProperty("updated_by")
    @NotBlank(groups = {UpdateFoodCategoryGroup.class})
    @Size(min = 3, max = 48, message = "Value of {updated_by} field must be at least 3 chars and not more than 254 chars.", groups = {UpdateFoodCategoryGroup.class})
    private String updatedBy;

    // Todo: Create custom constraint accepting values: true|false
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
