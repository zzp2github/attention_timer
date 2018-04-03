package com.hexun.attention.web.wrapper;

public class APIWrapperExt<T> extends APIWrapper<T> {

	private static final long serialVersionUID = -7758344442574358856L;
	
	 /** 结果明细 **/
    private Object detail;
    
    public APIWrapperExt(int code, String message, T result,Object obj) {
    	super();
    	this.detail(obj).code(code).message(message).result(result);
    }
    
    public Object getDetail() {
		return detail;
	}

	public void setDetail(Object detail) {
		this.detail = detail;
	}
	
	public APIWrapper<T> detail(Object detail) {
    	this.setDetail(detail);
    	return this;
    }

}
