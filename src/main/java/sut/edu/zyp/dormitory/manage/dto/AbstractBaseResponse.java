package sut.edu.zyp.dormitory.manage.dto;

import sut.edu.zyp.dormitory.manage.enums.ResponseCodeEnum;

import java.io.Serializable;

/**
 * 基础返回
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
