package sut.edu.zyp.dormitory.manage.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * 宿舍实体
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dormitory")
public class DormitoryEntity extends AbstractBaseEntity implements Serializable {

    /**
     * 宿舍编号
     */
    @Column(unique = true, length = 32, nullable = false)
    private String sn;

    /**
     * 所属楼宇
     */
    @Column(name = "building_id", length = 32)
    private String buildingId;

    /**
     * 所属楼层
     */
    @Column(length = 32, nullable = false)
    private String floor;

    /**
     * 最大可住人数
     */
    @Column(name = "max_number", nullable = false)
    private Integer maxNumber;

    /**
     * 已住人数
     */
    @Column(name = "lived_number")
    private Integer livedNumber = 0;
}
