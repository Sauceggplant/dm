package sut.edu.zyp.dormitory.manage.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 楼宇实体
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDormitoryManagerId() {
        return dormitoryManagerId;
    }

    public void setDormitoryManagerId(String dormitoryManagerId) {
        this.dormitoryManagerId = dormitoryManagerId;
    }
}
