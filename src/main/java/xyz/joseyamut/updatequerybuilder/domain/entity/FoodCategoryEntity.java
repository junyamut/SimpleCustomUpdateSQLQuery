package xyz.joseyamut.updatequerybuilder.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import xyz.joseyamut.updatequerybuilder.domain.dto.FoodCategory;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "food_category", schema = "food_store")
public class FoodCategoryEntity implements Serializable {

    @Id
    @Column(name="food_category_id", updatable = false)
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="group_id")
    private Integer groupId;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "created_on", updatable = false)
    private Timestamp createdOn;

    @Column(name = "updated_by", nullable = true)
    private String updatedBy;

    @Column(name = "updated_on", nullable = true)
    private Timestamp updatedOn;

    @Column(name = "is_active")
    private String isActive;

    public void populate(FoodCategory dto) {
        this.name = dto.getName();
        this.groupId = dto.getGroupId();
        this.description = dto.getDescription();
        this.createdBy = dto.getCreatedBy();
        this.createdOn = new Timestamp(new Date().getTime());
        this.isActive = dto.isActive() ? "Y" : "N";
    }

    public void update(FoodCategory dto) {
        populate(dto);
        this.id = dto.getId();
        this.updatedBy = dto.getUpdatedBy();
        this.updatedOn = new Timestamp(new Date().getTime());
    }
}
