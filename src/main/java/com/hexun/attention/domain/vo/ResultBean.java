package com.hexun.attention.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexun.attention.domain.entity.AttentionStatistics;

/**
 * 描述：
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年8月9日
 * @version v1.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResultBean {

	/** 结果集 */
	@ApiModelProperty(notes = "粉丝统计数据集")
	private List<AttentionStatistics> data;
	
	/** 分页信息 */
	@ApiModelProperty(notes = "分页信息")
	private Pager pager;

	public List<AttentionStatistics> getData() {
		return data;
	}

	public void setData(List<AttentionStatistics> data) {
		this.data = data;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}
	
}
