package com.hexun.attention.db.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hexun.attention.domain.entity.AttentionStatistics;

/**
 * 描述：老师关注数统计mapper
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月27日
 * @version v1.0
 * 			v1.0 2017-08-09 zhangyang 添加获取符合条件记录数的数量
 */
public interface AttentionStatisticsMapper {

	/**
	 * 获取老师的关注/取消关注统计
	 * 
	 * @param tids 老师IDs
	 * @param fromDate 开始时间
	 * @param fromDate 结束时间
	 * @param fixDate 固定日期
	 * @param offset 偏移量
	 * @param limit 数量
	 * @return
	 */
	public List<AttentionStatistics> getAttentions(@Param("tids") List<Long> tids,
			@Param("fromDate") String fromDate, 
			@Param("toDate") String toDate,
			@Param("fixDate") String fixDate,
			@Param("offset") Integer offset,
			@Param("limit") Integer limit);
	
	/**
	 * 查询老师的关注关系信息数量从统计表
	 * 
	 * @param tids 老师IDs
	 * @param fromDate 开始时间
	 * @param fromDate 结束时间
	 * @param fixDate 固定日期
	 */
	public Integer searchAttentionsCount(@Param("tids") List<Long> tids,
			@Param("fromDate") String fromDate, 
			@Param("toDate") String toDate,
			@Param("fixDate") String fixDate);
	
	/**
	 * 插入某个老师的关注统计
	 * 
	 * @param as
	 * @return
	 */
	public boolean insertAttentionStatistics(AttentionStatistics as);
	
	/**
	 * 批量插入关注关系统计信息
	 * 
	 * @param ass 统计信息集合
	 * @return
	 */
	public boolean batchInsertAttentionStatistics(@Param("list") List<AttentionStatistics> ass);
	/**
	 * 通过老师ids与日期查询新增关注数、取消关注数、净增数、累计关注数
	 * @param teacherIdList
	 * @param postTime
	 * @return
	 */
	public List<AttentionStatistics> getAttentionSumByWhere(@Param("teacherIdList") List<Long> teacherIdList, @Param("postTime") String postTime);
	
}
