package sut.edu.zyp.dormitory.manage.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

/**
 * 学生入住信息实体
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "live")
public class LiveEntity extends AbstractBaseEntity implements Serializable {

    /**
     * 学生id
     */
    @Column(name = "student_id", length = 32, nullable = false)
    private String studentId;

    /**
     * 宿舍id
     */
    @Column(name = "dormitory_id", length = 32, nullable = false)
    private String dormitoryId;

    /**
     * 入住时间
     */
    @Column(name = "live_date", nullable = false, columnDefinition = "TIMESTAMP")
    private Date liveDate;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDormitoryId() {
        return dormitoryId;
    }

    public void setDormitoryId(String dormitoryId) {
        this.dormitoryId = dormitoryId;
    }

    public Date getLiveDate() {
        return liveDate;
    }

    public void setLiveDate(Date liveDate) {
        this.liveDate = liveDate;
    }
}
