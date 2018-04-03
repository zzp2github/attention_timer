package com.hexun.attention.web.wrapper;

/**
 * 支持数据分页的包装类
 * 
 * @author zhc2054
 * 
 */
public class PageWrapper<T> extends APIWrapper<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 666985064788933395L;

    /**
     * 
     */
    public PageWrapper() {
        super();
    }

    /**
     * @param code
     * @param message
     */
    public PageWrapper(int code, String message) {
        super(code, message);
    }

    /**
     * @param code
     * @param message
     * @param result
     * @param pageUtil
     */
    public PageWrapper(int code, String message, T result) {
        super(code, message, result);
    }

    /**
     * Sets the 结果数据 ，返回自身的引用.
     * 
     * @param result
     *            the new 结果数据
     * 
     * @return the wrapper
     */
    @Override
    public PageWrapper<T> result(T result) {
        super.setResult(result);
        return this;
    }
}
