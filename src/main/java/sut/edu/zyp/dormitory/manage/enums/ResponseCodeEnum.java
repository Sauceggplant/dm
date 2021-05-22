package sut.edu.zyp.dormitory.manage.enums;

/**
 * 返回码枚举
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
public enum ResponseCodeEnum {

    /**
     * 成功
     */
    SUCCESS("0000", "成功"),

    /**
     * 入参错误
     */
    PARAM_ERROR("0001", "系统繁忙，请您稍后再试！"),

    /**
     * 验证码已失效、验证码已过期、验证码输入错误
     */
    CPACHA_INVALID("0002", "验证码错误，请您刷新验证码后重新登录！"),

    /**
     * 账号不存在
     */
    ACCOUNT_NOT_EXIST("0003", "账号不存在，请您检查录入的账号信息！"),

    /**
     * 密码错误
     */
    PASSWORD_ERROR("0004", "密码错误，如需重置密码请联系管理员！"),

    /**
     * 学生重名
     */
    SAME_NAME("0005", "登录失败，请使用学生编号登录"),

    /**
     * 上传文件为空
     */
    FILE_IS_EMPTY("0006", "上传文件为空"),

    /**
     * Excel没有上传数据
     */
    NO_DATA("0007", "没有找到需要上传的数据"),

    /**
     * Excel的Sheet页错误
     */
    NO_TABLE("0008","没有找到上传的目标表");

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回描述信息
     */
    private String info;

    ResponseCodeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
