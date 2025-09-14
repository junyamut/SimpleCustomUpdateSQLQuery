package xyz.joseyamut.updatequerybuilder.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.joseyamut.updatequerybuilder.domain.validation.CreateFoodCategoryGroup;
import xyz.joseyamut.updatequerybuilder.domain.validation.UpdateFoodCategoryGroup;
import xyz.joseyamut.updatequerybuilder.domain.entity.FoodCategoryEntity;
import xyz.joseyamut.updatequerybuilder.util.DateTimeFormatHelper;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.ZoneId;

import static xyz.joseyamut.updatequerybuilder.util.DateTimeFormatHelper.DEFAULT_TARGET_ZONE_ID;

@Slf4j
@Data
public class FoodCategory {

    @JsonProperty("id")
    @NotNull(message = "Provide the {id} of the category to be updated.", groups = {UpdateFoodCategoryGroup.class})
    @Min(value = 1, message = "Invalid value of {id} field.", groups = {UpdateFoodCategoryGroup.class})
    private Integer id;

    @JsonProperty("name")
    @NotBlank(message = "Field {name} must not be blank.", groups = {CreateFoodCategoryGroup.class})
    @Size(min = 3, max = 64, message = "Value of {name} field must be at least 3 chars and not more than 64 chars.", groups = {CreateFoodCategoryGroup.class, UpdateFoodCategoryGroup.class})
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("group_id")
    @Min(value = 1, message = "Value of {group_id} field cannot be less than 1.", groups = {CreateFoodCategoryGroup.class, UpdateFoodCategoryGroup.class})
    private Integer groupId;

    @JsonProperty("created_by")
    @NotBlank(message = "Field {created_by} must not be blank.", groups = {CreateFoodCategoryGroup.class})
    @Size(min = 3, max = 48, message = "Value of {created_by} field must be at least 3 chars and not more than 254 chars.", groups = {CreateFoodCategoryGroup.class})
    private String createdBy;

    @JsonProperty("created_on")
    private String createdOn;

    @JsonProperty("updated_by")
    @NotBlank(message = "Field {updated_by} must not be blank.", groups = {UpdateFoodCategoryGroup.class})
    @Size(min = 3, max = 48, message = "Value of {updated_by} field must be at least 3 chars and not more than 254 chars.", groups = {UpdateFoodCategoryGroup.class})
    private String updatedBy;

    @JsonProperty("updated_on")
    private String updatedOn;

    @JsonProperty("is_active")
    @NotNull(message = "Field {is_active} must be either true or false.", groups = {CreateFoodCategoryGroup.class})
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
        this.isActive = entity.getIsActive();
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
