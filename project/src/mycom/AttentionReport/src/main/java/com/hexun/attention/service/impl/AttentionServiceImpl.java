package com.hexun.attention.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hexun.attention.db.mongo.TeacherDao;
import com.hexun.attention.db.mysql.AttentionLogMapper;
import com.hexun.attention.db.mysql.AttentionStatisticsMapper;
import com.hexun.attention.domain.entity.AttentionLog;
import com.hexun.attention.domain.entity.AttentionStatistics;
import com.hexun.attention.domain.entity.Teacher;
import com.hexun.attention.service.AttentionService;
import com.hexun.attention.util.DateUtil;
import com.hexun.attention.util.HttpUtil;
import com.hexun.attention.util.MD5;
import com.hexun.attention.util.SystemConfig;

/**
 * 描述：关注关系操作service实现类
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月19日
 * @version v1.0
 * 			v1.0 2017-08-09 zhangyang 添加获取符合条件记录数的数量
 */
@Service
public class AttentionServiceImpl implements AttentionService {
	
	private static Logger log = LoggerFactory.getLogger(AttentionServiceImpl.class);

	@Autowired
	private TeacherDao teacherDao;
	
	@Autowired
	private AttentionLogMapper attentionLogMapper;
	
	@Autowired
	private AttentionStatisticsMapper attentionStatisticsMapper;
	
	/**
	 * 查询老师的关注关系信息从统计表
	 * 
	 * @param tids 老师IDs
	 * @param fromDate 开始时间
	 * @param fromDate 结束时间
	 * @param fixDate 固定日期
	 * @param page 第几页
	 * @param count 数量
	 * @return
	 */
	public List<AttentionStatistics> searchAttentions(List<Long> teacherIds, String fromate, String toDate, String fixDate, Integer page, Integer count) {
		return attentionStatisticsMapper.getAttentions(teacherIds, fromate, toDate, fixDate, (page - 1) * count, count);
	}
	
	/**
	 * 查询老师的关注关系信息数量从统计表
	 * 
	 * @param tids 老师IDs
	 * @param fromDate 开始时间
	 * @param fromDate 结束时间
	 * @param fixDate 固定日期
	 */
	public Integer searchAttentionsCount(List<Long> teacherIds, String fromate, String toDate, String fixDate) {
		return attentionStatisticsMapper.searchAttentionsCount(teacherIds, fromate, toDate, fixDate);
	}
	
	/**
	 * 递归执行任务
	 * 
	 * @param offset 偏移量
	 * @param date 日期
	 */
//	@Override
	public void execute(int offset, Date date) {
		final int limit = 200;
		// 1. 获取老师信息(分页)
		List<Teacher> teachers = teacherDao.getTeachers(offset, limit);
		if (log.isDebugEnabled()) {
			log.debug("** offset is {}, load teacher count is {} **", offset, teachers == null ? 0 : teachers.size());
		}
		if (teachers != null && !teachers.isEmpty()) {
			List<Long> tids = new ArrayList<Long>();
			for (Teacher teacher : teachers) {
				tids.add(teacher.getTeacherId());
			}
			// 2. 去日志表查询数据
			List<AttentionLog> attentions = attentionLogMapper.getTeacherAttentions(tids, DateUtil.DateToString(date, "yyyy-MM-dd"));
			// 去除重复元素
			remove(attentions);
			if (log.isDebugEnabled()) {
				log.debug("** teacher attention total item count is {} **", attentions == null ? 0 : attentions.size());
			}
			// <b>本批次记录有部分(或者全部)发生了关注关系</b>
			if (attentions != null && !attentions.isEmpty()) {
				// 老师关注统计map
				Map<Long, AttentionStatistics> map = new HashMap<Long, AttentionStatistics>();
				for (AttentionLog av : attentions) {
					Long teacherId = av.getAttentionId();	// 老师ID
					// 为空则添加到map
					if (map.get(teacherId) == null) {
						AttentionStatistics as = new AttentionStatistics();
						as.setTeacherId(teacherId);
						if (av.getType() == 1) {			
							// 初始化添加关注数1
							as.setFocusCount(1);
						} else {							
							// 初始化取消关注数1
							as.setCancelCount(1);
						}
						// 操作的日期
						as.setDate(date);
						map.put(teacherId, as);
					// 不为空从map中获取	
					} else {
						AttentionStatistics as = map.get(teacherId);
						if (av.getType() == 1) {			
							// 追加关注数1
							as.setFocusCount(as.getFocusCount() == null ? 1 : as.getFocusCount() + 1);
						} else {							
							// 追加取消关注数1
							as.setCancelCount(as.getCancelCount() == null ? 1 : as.getCancelCount() + 1);
						}
					}
				}
				
				if (log.isDebugEnabled()) {
					log.debug("** {} teachers attentions **", map.size());
				}
				
				// 3-1. 批量插入有关注/取消记录统计信息
				if (map.size() > 0) {
					List<AttentionStatistics> ass = new ArrayList<AttentionStatistics>();
					for (Long teacherId : map.keySet()) {
						ass.add(map.get(teacherId));
					}
					// 设置当前粉丝数
					StringBuffer ids = new StringBuffer();
					for (Long teacherId : map.keySet()) {
						ids.append(teacherId).append(",");
					}
					setCurrentFans(ids.toString(), ass);
					// 保存到数据库
					attentionStatisticsMapper.batchInsertAttentionStatistics(ass);
				}
				
				// 3-2. 插入没有关注/取消记录的统计信息
				// 取没有关注记录的老师ID(总数-有关注记录的老师ID)
				Iterator<Long> iterator = tids.iterator();
				while (iterator.hasNext()) {
					Long tid = iterator.next();
					for (Long teacherId : map.keySet()) {
						if (tid.longValue() == teacherId.longValue()) {
							iterator.remove();
							break;
						}
					}
				}
				// 批量设置空实体
				List<AttentionStatistics> ass = new ArrayList<AttentionStatistics>();
				AttentionStatistics as = null;
				for (Long tid : tids) {
					as = new AttentionStatistics();
					as.setTeacherId(tid);
					as.setDate(date);
					ass.add(as);
				}
				// 设置当前粉丝数
				StringBuffer ids = new StringBuffer();
				for (Long teacherId : tids) {
					ids.append(teacherId).append(",");
				}
				setCurrentFans(ids.toString(), ass);
				// 保存到数据库
				attentionStatisticsMapper.batchInsertAttentionStatistics(ass);
			
			// <b>本批次记录没有老师发生关注记录</b>	
			} else {
				// 3-3. 插入没有关注/取消记录的统计信息
				// 批量设置空实体
				List<AttentionStatistics> ass = new ArrayList<AttentionStatistics>();
				AttentionStatistics as = null;
				for (Long tid : tids) {
					as = new AttentionStatistics();
					as.setTeacherId(tid);
					as.setDate(date);
					ass.add(as);
				}
				// 设置当前粉丝数
				StringBuffer ids = new StringBuffer();
				for (Long teacherId : tids) {
					ids.append(teacherId).append(",");
				}
				setCurrentFans(ids.toString(), ass);
				// 保存到数据库
				attentionStatisticsMapper.batchInsertAttentionStatistics(ass);
			}
			
			offset += teachers.size();
			execute(offset, date);
		}
		return;
	}
	
	/**
	 * 设置当前老师的粉丝数量
	 * 
	 * @param map
	 * @param ass
	 */
	private void setCurrentFans(String ids, List<AttentionStatistics> ass) {
		/** 批量获取老师粉丝 */
		String TEACHER_RELATION_URL = String.format(SystemConfig.TEACHER_RELATION_URL, ids);
		String result = HttpUtil.sendGet(TEACHER_RELATION_URL);
		if (StringUtils.isNoneBlank(result)) {
			JSONObject jo = JSONObject.parseObject(result);
			if (!jo.isEmpty() && jo.containsKey("statecode") && jo.containsKey("result")) {
				String code = jo.getString("statecode");
				if (StringUtils.isNotBlank(code) && "1".equals(code)) {
					String resultStr = jo.getString("result");
					if (StringUtils.isNoneBlank(resultStr)) {
						JSONObject resultJO = JSONObject.parseObject(resultStr);
						if (resultJO != null && resultJO.containsKey("status") && resultJO.containsKey("revdata")) {
							if (1 == resultJO.getIntValue("status")) {
								JSONArray dataJA = resultJO.getJSONArray("revdata");
								for (int i=0; i< dataJA.size(); i++) {
									JSONObject djo = (JSONObject) dataJA.get(i);
									if (dataJA != null && dataJA.size() > 0) {
										for (AttentionStatistics as : ass) {
											if (as.getTeacherId().longValue() == djo.getLongValue("userId")) {
												as.setCurrentFans(djo.getInteger("fansCount"));
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 去除重复元素
	 * 
	 * @param attentions
	 */
	private void remove(List<AttentionLog> attentions) {
		for (int i=0; i<attentions.size(); i++) {
			for (int j=attentions.size() - 1; j>i; j--) {
				if (attentions.get(i).equals(attentions.get(j))) {
					attentions.remove(j);
				}
			}
		}
	}
	
	/**
	 * 获取老师的粉丝数量
	 * 
	 * @param teacherId
	 * @return
	 */
	public int getFansCount(Long teacherId) {
		/** 获取单个老师的粉丝数 */
		String ONE_TEACHER_RELATION_URL = "http://follow.zq.hexun.com/relation/getrelationinfo.do?uid=%s&source=2";
		ONE_TEACHER_RELATION_URL = String.format(ONE_TEACHER_RELATION_URL, teacherId);
		String followResStr = HttpUtil.sendGet(ONE_TEACHER_RELATION_URL);
		if(!StringUtils.isBlank(followResStr)){
			JSONObject jo = JSONObject.parseObject(followResStr);
			if (jo != null && jo.containsKey("statecode") && "1".equals(jo.getString("statecode"))) {
				JSONObject jos = jo.getJSONObject("result");
				if(jos.containsKey("fansCount")){
					return jos.getInteger("fansCount");
				}
			}
		}
		return 0;
	}
	/**
	 * 根据多个或一个用户id、具体日期，查询每个合作者的新增关注数、取消关注数、净增数、累计关注数
	 * @param teacherIds
	 * @param postTime
	 * @return
	 */
	public List<AttentionStatistics> getAttentionByWhere(String teacherIds,String postTime,String code){
		// 参数校验
		StringBuffer params = new StringBuffer();
		if (StringUtils.isNotBlank(teacherIds)) {
			params.append(teacherIds);
		}
		if (StringUtils.isNotBlank(postTime)) {
			params.append(postTime);
		}
		String result = MD5.getMD5Code(params.toString());
		if(!result.equalsIgnoreCase(code)){
			return null;
		}
		List<Long> teacherIdList = new ArrayList<Long>();
		if (StringUtils.isNotBlank(teacherIds)) {
			String[] teacherIdArry = teacherIds.split(",");
			for(String tId:teacherIdArry){
				teacherIdList.add(Long.valueOf(tId));
			}
		}
		return attentionStatisticsMapper.getAttentionSumByWhere(teacherIdList,postTime);
	}
}
