package com.hexun.attention.web.wrapper;

/**
 * 包装辅助类.
 * 
 * @author zhc2054
 */
public class APIWrapMapper {

    /**
     * Instantiates a new wrap mapper.
     */
    private APIWrapMapper() {
    }
    
    /**
     * Wrap.
     * 
     * @param <E>
     *            the element type
     * @param code
     *            the code
     * @param message
     *            the message
     * @param o
     *            the o
     * @return the wrapper
     */
    public static <E> APIWrapper<E> wrap(int code, String message, E o) {
        return new APIWrapper<E>(code, message, o);
    }
    
    /**
     * 异构返回结果封装
     * @param code
     * @param message
     * @param o:返回id
     * @param obj:返回对象
     * @return
     */
    public static <E> APIWrapperExt<E> wrap(int code, String message, E o ,Object obj) {
    	return new APIWrapperExt<E>(code, message, o,obj);
    }

    /**
     * Wrap.
     * 
     * @param <E>
     *            the element type
     * @param code
     *            the code
     * @param message
     *            the message
     * @return the wrapper
     */
    public static <E> APIWrapper<E> wrap(int code, String message) {
        return wrap(code, message, null);
    }

    /**
     * Wrap.
     * 
     * @param <E>
     *            the element type
     * @param code
     *            the code
     * @return the wrapper< e>
     */
    public static <E> APIWrapper<E> wrap(int code) {
        return wrap(code, null);
    }

    /**
     * Wrap.
     * 
     * @param <E>
     *            the element type
     * @param e
     *            the e
     * @return the wrapper
     */
    public static <E> APIWrapper<E> wrap(Exception e) {
        return new APIWrapper<E>(APIWrapper.ERROR_CODE, e.getMessage());
    }

    /**
     * Un wrapper.
     * 
     * @param <E>
     *            the element type
     * @param wrapper
     *            the wrapper
     * @return the e
     */
    public static <E> E unWrap(APIWrapper<E> wrapper) {
        return wrapper.getResult();
    }

    /**
     * Wrap ERROR. code=100
     * 
     * @param <E>
     *            the element type
     * @return the wrapper< e>
     */
    public static <E> APIWrapper<E> illegalArgument() {
        return wrap(APIWrapper.ILLEGAL_ARGUMENT_CODE_, APIWrapper.ILLEGAL_ARGUMENT_MESSAGE);
    }

    /**
     * Wrap ERROR. code=500
     * 
     * @param <E>
     *            the element type
     * @return the wrapper< e>
     */
    public static <E> APIWrapper<E> error() {
        return wrap(APIWrapper.ERROR_CODE, APIWrapper.ERROR_MESSAGE);
    }

    /**
     * Wrap SUCCESS. code=200
     * 
     * @param <E>
     *            the element type
     * @return the wrapper< e>
     */
    public static <E> APIWrapper<E> ok() {
        return new APIWrapper<E>();
    }
}
