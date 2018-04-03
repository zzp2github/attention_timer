package com.hexun.attention.service;

import java.util.Date;
import java.util.List;

import com.hexun.attention.domain.entity.AttentionStatistics;

/**
 * 描述：关注关系操作service
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月19日
 * @version v1.0
 */
public interface AttentionService {

	/**
	 * 递归执行任务
	 * 
	 * @param offset 偏移量
	 * @param date 日期
	 */
	public void execute(int offset, Date date);
	
	/**
	 * 查询老师的关注关系信息从统计表
	 * 
	 * @param tids 老师IDs
	 * @param fromDate 开始时间
	 * @param fromDate 结束时间
	 * @param fixDate 固定日期
	 * @return
	 */
	public List<AttentionStatistics> searchAttentions(List<Long> teacherIds, String fromDate, String toDate, String fixDate, Integer page, Integer count);

	/**
	 * 查询老师的关注关系信息数量从统计表
	 * 
	 * @param tids 老师IDs
	 * @param fromDate 开始时间
	 * @param fromDate 结束时间
	 * @param fixDate 固定日期
	 */
	public Integer searchAttentionsCount(List<Long> teacherIds, String fromate, String toDate, String fixDate);
	
	/**
	 * 获取老师的粉丝数量
	 * 
	 * @param teacherId
	 * @return
	 */
	public int getFansCount(Long teacherId);
	/**
	 * 根据多个或一个用户id、具体日期，查询每个合作者的新增关注数、取消关注数、净增数、累计关注数
	 * @param teacherIds
	 * @param postTime
	 * @return
	 */
	public List<AttentionStatistics> getAttentionByWhere(String teacherIds,String postTime,String code);
}
