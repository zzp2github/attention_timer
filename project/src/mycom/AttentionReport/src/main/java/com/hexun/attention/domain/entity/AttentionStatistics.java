package com.hexun.attention.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 描述：老师被关注/取消信息统计实体
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月18日
 * @version v1.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AttentionStatistics {

	/** 老师ID */
	@JSONField(name = "teacher_id")
	@ApiModelProperty(name = "teacher_id", dataType = "java.lang.Long", notes = "老师ID")
	private Long teacherId;
	
	/** 老师名称 */
	@JSONField(serialize = false)
	@ApiModelProperty(hidden = true)
	private String teacherName;
	
	/** 添加关注数量 */
	@JSONField(name = "focus_count")
	@ApiModelProperty(name = "focus_count", notes = "新增粉丝数")
	private Integer focusCount;
	
	/** 取消关注数量 */
	@JSONField(name = "cancel_count")
	@ApiModelProperty(name = "cancel_count", notes = "取消关注数")
	private Integer cancelCount;
	
	/** 当前粉丝数 */
	@JSONField(name = "current_fans")
	@ApiModelProperty(name = "current_fans", notes = "当前粉丝数")
	private Integer currentFans;
	
	/** 净增长 */
	@JSONField(name = "net_fans")
	@ApiModelProperty(name = "net_fans", notes = "净增长粉丝数")
	private Integer netFocus;
	
	/** 日期 */
	@JSONField (format="yyyy-MM-dd")  
	@ApiModelProperty(name = "date", example = "1970-01-01", notes = "日期")
	private Date date;

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Integer getFocusCount() {
		return focusCount;
	}

	public void setFocusCount(Integer addAttentionCount) {
		this.focusCount = addAttentionCount;
	}

	public Integer getCancelCount() {
		return cancelCount;
	}

	public void setCancelCount(Integer cancelAttentionCount) {
		this.cancelCount = cancelAttentionCount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getCurrentFans() {
		return currentFans;
	}

	public void setCurrentFans(Integer currentFans) {
		this.currentFans = currentFans;
	}

	public Integer getNetFocus() {
		if (focusCount == null) {
			focusCount = 0;
		}
		if (cancelCount == null) {
			cancelCount = 0;
		}
		return focusCount - cancelCount;
	}

	public void setNetFocus(Integer netFocus) {
		this.netFocus = netFocus;
	}
	
}
