package com.hexun.attention.domain.entity;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 描述：对应mongo中的TeacherInfo表
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月19日
 * @version v1.0
 */
@Document(collection = "TeacherInfo")
public class Teacher implements Serializable  {

	@Field(value = "_id")
	private Long teacherId;
	
	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

}
