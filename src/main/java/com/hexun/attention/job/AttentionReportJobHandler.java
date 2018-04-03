package com.hexun.attention.job;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexun.attention.service.AttentionService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 用户关注报告定时任务
 * 
 * 开发步骤：
 * 1、继承 “IJobHandler” ；
 * 2、装配到Spring，例如加 “@Service” 注解；
 * 3、加 “@JobHander” 注解，注解value值为新增任务生成的JobKey的值;多个JobKey用逗号分割;
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 * 
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月18日
 * @version v1.0
 */
@JobHander(value="attentionReportJobHandler")
@Service
public class AttentionReportJobHandler extends IJobHandler {

	@Autowired
	private AttentionService attentionService;
	/**
	 * Title: execute 
	 * Description:关注关系定时统计
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		try {
			XxlJobLogger.log("开始执行'关注关系统计'定时任务" + new Date());
			// 取前一天时间
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DAY_OF_MONTH, -1);
	    	attentionService.execute(0, now.getTime());
	    	XxlJobLogger.log("结束执行'关注关系统计'定时任务" + new Date());
	    	return ReturnT.SUCCESS;
		} catch (Exception e) {
			XxlJobLogger.log("执行'关注关系统计'定时任务失败" + e.getMessage());
			return ReturnT.FAIL;
		}
	}

}
