package sut.edu.zyp.dormitory.manage.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * 系统管理员实体
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "admin")
public class AdminEntity extends AbstractBaseEntity implements Serializable {

    /**
     * 管理员账号
     */
    @Column(unique = true, length = 32, nullable = false)
    private String name;

    /**
     * 管理员密码
     */
    @Column(length = 32, nullable = false)
    private String password;
}
