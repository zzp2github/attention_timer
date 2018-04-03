package com.hexun.attention.db.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hexun.attention.domain.entity.AttentionLog;


/**
 * 描述：用户关注关系日志记录mapper
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月18日
 * @version v1.0
 */
public interface AttentionLogMapper {

	/**
	 * 获取够个老师的被关注信息
	 * 
	 * @param teacherId
	 * @param distributorType
	 * @param date
	 * @return
	 */
	public List<AttentionLog> getTeacherAttention(@Param("teacherId") Long teacherId, @Param("date") String date);
	
	/**
	 * 批量查询老师的被关注信息
	 * 
	 * @param teacherIds
	 * @param date
	 * @return
	 */
	public List<AttentionLog> getTeacherAttentions(@Param("teacherIds") List<Long> teacherIds, @Param("date") String date);
}
