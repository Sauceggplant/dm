package sut.edu.zyp.dormitory.manage.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * 楼宇实体
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "building")
public class BuildingEntity extends AbstractBaseEntity implements Serializable {

    /**
     * 楼宇名称
     */
    @Column(unique = true, length = 64, nullable = false)
    private String name;

    /**
     * 楼宇所属位置
     */
    @Column(length = 128, nullable = false)
    private String location;

    /**
     * 所属宿管
     */
    @Column(name = "dormitory_manager_id", length = 32)
    private String dormitoryManagerId;

    /**
     * 楼宇占地面积
     */
    @Column(name = "area")
    private Integer area;

    /**
     * 楼层数
     */
    @Column(name = "floors")
    private Integer floors;
}
