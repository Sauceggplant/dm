package sut.edu.zyp.dormitory.manage.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 宿舍实体
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }

    public Integer getLivedNumber() {
        return livedNumber;
    }

    public void setLivedNumber(Integer livedNumber) {
        this.livedNumber = livedNumber;
    }
}
