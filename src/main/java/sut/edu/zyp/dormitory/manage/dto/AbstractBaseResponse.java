package sut.edu.zyp.dormitory.manage.dto;

import lombok.Getter;
import lombok.Setter;
import sut.edu.zyp.dormitory.manage.enums.ResponseCodeEnum;

import java.io.Serializable;

/**
 * 基础返回
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Getter
@Setter
public abstract class AbstractBaseResponse<T> implements Serializable {

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
    private long timestamp;

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回信息
     */
    private String info;

    /**
     * 返回数据
     */
    private T data;

    public void setResponseCode(ResponseCodeEnum responseCode){
        this.code = responseCode.getCode();
        this.info = responseCode.getInfo();
    }
}
