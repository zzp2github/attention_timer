package com.hexun.attention.domain.entity;

import java.util.Date;

import com.hexun.attention.util.DateUtil;

/**
 * 描述：关注关系记录实体
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月19日
 * @version v1.0
 */
public class AttentionLog {

	/** 被关注老师ID */
	private Long attentionId;
	
	/** 用户ID */
	private Long userId;
	
	/** 1添加关注 2取消关注 */
	private Integer type;
	
	/** 操作日期 */
	private Date date;
	
	public Long getAttentionId() {
		return attentionId;
	}

	public void setAttentionId(Long attentionId) {
		this.attentionId = attentionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof AttentionLog) {
			AttentionLog log = (AttentionLog) obj;
			if (log.attentionId.longValue() == this.attentionId.longValue()
					&& log.userId.longValue() == this.userId.longValue() 
					&& log.type.intValue() == this.type.intValue()
					&& DateUtil.DateToString(this.date, "yyyy-MM-dd").equalsIgnoreCase(DateUtil.DateToString(log.date, "yyyy-MM-dd"))) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
