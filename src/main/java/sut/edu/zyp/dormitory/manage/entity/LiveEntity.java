package sut.edu.zyp.dormitory.manage.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Date;

/**
 * 学生入住信息实体
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Getter
@Setter
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
}
