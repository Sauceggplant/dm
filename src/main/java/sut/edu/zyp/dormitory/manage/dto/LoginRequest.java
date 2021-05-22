package sut.edu.zyp.dormitory.manage.dto;

import java.io.Serializable;

/**
 * 登录数据请求传输对象
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
