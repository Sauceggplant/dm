package sut.edu.zyp.dormitory.manage.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;

/**
 * 基础实体对象父类，所有的数据库表都要有的通用字段，其他所有的实体类（entity类)都需要继承此父类
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@MappedSuperclass
public abstract class AbstractBaseEntity implements Serializable {

    /**
     * id，用于唯一标识数据库的某一行记录，全局唯一
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGenerator")
    @Column(unique = true, nullable = false, length = 32)
    private String id;

    /**
     * 创建时间，数据库记录第一次创建时的时间
     */
    @Column(name = "create_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private Date createTime;

    /**
     * 更新时间，数据库数据记录最后一次变更时间
     */
    @Column(name = "update_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @LastModifiedDate
    private Date updateTime;

    /**
     * 版本号，用于数据库的乐观锁，每次数据库记录发生变更，自动加1，首次变更为0
     */
    @Version
    private Integer version;

    /**
     * 是否有效，用于数据库数据的逻辑删除，而非物理删除
     */
    @Column(length = 1)
    private String valid = "1";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
}
