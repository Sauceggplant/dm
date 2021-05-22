package sut.edu.zyp.dormitory.manage.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 学生实体
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "student")
public class StudentEntity extends AbstractBaseEntity implements Serializable {

    /**
     * 学生编号
     */
    @Column(unique = true, length = 32, nullable = false)
    private String sn;

    /**
     * 学生姓名
     */
    @Column(length = 16, nullable = false)
    private String name;

    /**
     * 密码
     */
    @Column(length = 32)
    private String password = "111111";

    /**
     * 学生性别
     */
    @Column(length = 4, nullable = false)
    private String sex;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
