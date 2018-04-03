package com.hexun.attention.web.wrapper;

/**
 * 分页包装辅助类.
 * 
 * @author zhc2054
 */
public class PageWrapMapper {

    /**
     * Instantiates a new page wrap mapper.
     */
    private PageWrapMapper() {
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
     * @param pageUtil
     *            the pageUtil
     * @return the wrapper
     */
    public static <E> PageWrapper<E> wrap(int code, String message, E o) {
        return new PageWrapper<E>(code, message, o);
    }

    /**
     * Wrap data with default code=200.
     * 
     * @param <E>
     *            the element type
     * @param o
     *            the o
     * @param pageUtil
     *            the pageUtil
     * @return the wrapper
     */
    public static <E> PageWrapper<E> wrap(E o) {
        return wrap(APIWrapper.SUCCESS_CODE, APIWrapper.SUCCESS_MESSAGE, o);
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
    public static <E> PageWrapper<E> wrap(int code, String message) {
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
    public static <E> PageWrapper<E> wrap(int code) {
        return wrap(code, null, null);
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
    public static <E> PageWrapper<E> wrap(Exception e) {
        return new PageWrapper<E>(APIWrapper.ERROR_CODE, e.getMessage(), null);
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
    public static <E> E unWrap(PageWrapper<E> wrapper) {
        return wrapper.getResult();
    }

    /**
     * Wrap ERROR. code=100
     * 
     * @param <E>
     *            the element type
     * @return the wrapper< e>
     */
    public static <E> PageWrapper<E> illegalArgument() {
        return wrap(APIWrapper.ILLEGAL_ARGUMENT_CODE_, APIWrapper.ILLEGAL_ARGUMENT_MESSAGE, null);
    }

    /**
     * Wrap ERROR. code=500
     * 
     * @param <E>
     *            the element type
     * @return the wrapper< e>
     */
    public static <E> PageWrapper<E> error() {
        return wrap(APIWrapper.ERROR_CODE, APIWrapper.ERROR_MESSAGE, null);
    }

    /**
     * Wrap SUCCESS. code=200
     * 
     * @param <E>
     *            the element type
     * @return the wrapper< e>
     */
    public static <E> PageWrapper<E> ok() {
        return new PageWrapper<E>();
    }
}
