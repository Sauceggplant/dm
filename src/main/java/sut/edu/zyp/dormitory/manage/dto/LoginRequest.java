package sut.edu.zyp.dormitory.manage.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 登录数据请求传输对象
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Getter
@Setter
public class LoginRequest extends AbstractBaseRequest implements Serializable {

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 类型
     */
    private String type;

    /**
     * 验证码
     */
    private String captcha;
}
