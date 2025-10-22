package com.terrabase.enterprise.api.response;

/**
 * 通用响应结果包装类
 * 用于统一API响应格式
 * 
 * @param <T> 响应数据类型
 * @author Yehong Pan
 * @version 1.0.0
 */
public class ResultVo<T> {
    
    /**
     * 响应状态码
     */
    private String code;
    
    /**
     * 响应消息
     */
    private String msg;
    
    /**
     * 响应数据
     */
    private T data;
    
    public ResultVo() {}
    
    public ResultVo(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public ResultVo(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    /**
     * 成功响应
     */
    public static <T> ResultVo<T> success() {
        return new ResultVo<>("200", "操作成功");
    }
    
    /**
     * 成功响应带数据
     */
    public static <T> ResultVo<T> success(T data) {
        return new ResultVo<>("200", "操作成功", data);
    }
    
    /**
     * 成功响应带消息和数据
     */
    public static <T> ResultVo<T> success(String msg, T data) {
        return new ResultVo<>("200", msg, data);
    }
    
    /**
     * 失败响应
     */
    public static <T> ResultVo<T> error(String code, String msg) {
        return new ResultVo<>(code, msg);
    }
    
    /**
     * 失败响应带数据
     */
    public static <T> ResultVo<T> error(String code, String msg, T data) {
        return new ResultVo<>(code, msg, data);
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "ResultVo{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

