package com.hexun.attention.web.json;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * 描述：fastjson jsonp转换类
 * 
 * @author zhangyang@staff.hexun.com
 * @date 2017年3月30日 下午1:52:07
 * @version v1.0
 */
public class FastJsonJsonpHttpMessageConverter extends FastJsonHttpMessageConverter {

	protected String[] jsonpParameterNames = new String[] { "callback", "jsonp" };

	@SuppressWarnings("deprecation")
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String text = JSON.toJSONString(obj, getFeatures());
		String callback = null;
		for (int i = 0; i < jsonpParameterNames.length; i++) {
			callback = request.getParameter(jsonpParameterNames[i]);
			if (callback != null) {
				break;
			}
		}
		if (!StringUtils.isBlank(callback)) {
			text = new StringBuilder(callback).append("(").append(text).append(")").toString();
		}
		outputMessage.getBody().write(text.getBytes(getCharset()));
	}

	public void setJsonpParameterNames(String[] jsonpParameterNames) {
		this.jsonpParameterNames = jsonpParameterNames;
	}
}
