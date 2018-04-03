package com.hexun.attention.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 描述：老师被关注/取消信息统计实体简化(时间+人数)
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月18日
 * @version v1.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReportItem {

	/** 数量 */
	@JSONField(name = "count")
	@ApiModelProperty(name = "count", notes = "数量")
	private Integer count;
	
	/** 日期 */
	@JSONField (format="yyyy-MM-dd")  
	@ApiModelProperty(name = "date", example = "1970-01-01", notes = "日期")
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
