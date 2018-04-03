package com.hexun.attention.web.wrapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 包装类.
 * 
 * @param <T>
 *            the generic type
 * @author zhc2054
 */
@ApiModel
public class APIWrapper<T> implements Serializable {

    /** 序列化标识 */
    private static final long serialVersionUID = 4893280118017319089L;

    /** 成功码. */
    public static final int SUCCESS_CODE = 200;

    /** 成功信息. */
    public static final String SUCCESS_MESSAGE = "OK";
    
    /** 成功，但无数据 */
    public static final int SUCCESS_NODATA_CODE = 2200;
    
    /** 成功，但无数据 */
    public static final String SUCCESS_NODATA_MESSAGE = "OK_NODATA";
    
    /** 错误码. */
    public static final int ERROR_CODE = 500;

    /** 错误信息. */
    public static final String ERROR_MESSAGE = "内部异常";

    /** 错误码：参数非法 */
    public static final int ILLEGAL_ARGUMENT_CODE_ = 100;

    /** 错误信息：参数非法 */
    public static final String ILLEGAL_ARGUMENT_MESSAGE = "参数非法";

    /** 编号. */
    @ApiModelProperty(value = "状态码", required = true)
    private int code;

    /** 信息. */
    @ApiModelProperty(value = "消息", required = true)
    private String message;

    /** 结果数据 */
    @ApiModelProperty(value = "结果", required = true)
    private T result;
        
    /**
     * Instantiates a new wrapper. default code=200
     */
    public APIWrapper() {
        this(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    /**
     * Instantiates a new wrapper.
     * 
     * @param code
     *            the code
     * @param message
     *            the message
     */
    public APIWrapper(int code, String message) {
        this(code, message, null);
    }

    /**
     * Instantiates a new wrapper.
     * 
     * @param code
     *            the code
     * @param message
     *            the message
     * @param result
     *            the result
     */
    public APIWrapper(int code, String message, T result) {
        super();
        this.code(code).message(message).result(result);
    }
    
	/**
     * Gets the 编号.
     * 
     * @return the 编号
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the 编号.
     * 
     * @param code
     *            the new 编号
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets the 信息.
     * 
     * @return the 信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the 信息.
     * 
     * @param message
     *            the new 信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the 结果数据.
     * 
     * @return the 结果数据
     */
    public T getResult() {
        return result;
    }

    /**
     * Sets the 结果数据.
     * 
     * @param result
     *            the new 结果数据
     */
    public void setResult(T result) {
        this.result = result;
    }

    /**
     * Sets the 编号 ，返回自身的引用.
     * 
     * @param code
     *            the new 编号
     * 
     * @return the wrapper
     */
    public APIWrapper<T> code(int code) {
        this.setCode(code);
        return this;
    }

    /**
     * Sets the 信息 ，返回自身的引用.
     * 
     * @param message
     *            the new 信息
     * 
     * @return the wrapper
     */
    public APIWrapper<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * Sets the 结果数据 ，返回自身的引用.
     * 
     * @param result
     *            the new 结果数据
     * 
     * @return the wrapper
     */
    public APIWrapper<T> result(T result) {
        this.setResult(result);
        return this;
    }
    
//	/**
//     * 判断是否成功： 依据 Wrapper.SUCCESS_CODE == this.code
//     * 
//     * @return code=200,true;否则 false.
//     */
//    public boolean isSuccess() {
//        return Wrapper.SUCCESS_CODE == this.code;
//    }

}
