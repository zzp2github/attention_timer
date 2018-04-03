package com.hexun.attention.db.mongo;

import java.util.List;

import com.hexun.attention.domain.entity.Teacher;

public interface TeacherDao {

	/**
	 * 获取所有老师信息
	 * 
	 * @return
	 */
	public List<Teacher> getAllTeacher();
	
	/**
	 * 分页获取老师信息
	 * 
	 * @return
	 */
	public List<Teacher> getTeachers(int offset, int limit);
}
