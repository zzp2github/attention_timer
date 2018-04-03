package com.hexun.attention.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hexun.attention.domain.entity.AttentionStatistics;
import com.hexun.attention.domain.entity.ReportItem;
import com.hexun.attention.domain.vo.Pager;
import com.hexun.attention.domain.vo.ResultBean;
import com.hexun.attention.service.AttentionService;
import com.hexun.attention.util.DateUtil;
import com.hexun.attention.util.Encrypt;
import com.hexun.attention.util.MD5;
import com.hexun.attention.util.ParameterUtils;
import com.hexun.attention.web.wrapper.APIWrapMapper;
import com.hexun.attention.web.wrapper.APIWrapper;
import com.hexun.hwcommon.model.CommonLoginInfo;
import com.hexun.hwcommon.service.UserAuth;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 描述：关注关系Controller
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月19日
 * @version v1.0
 * 			v1.1 2017-07-31 把前端的接口从传递老师ID，改成获取登录用户ID，并添加后台统计接口，需要验签
 * 			v1.1 2017-08-09 zhangyang 接口返回分页信息
 */
@Controller
@RequestMapping("attention")
public class AttentionController {

	private static Logger log = LoggerFactory.getLogger(AttentionController.class);
	
	@Autowired
	private AttentionService attentionService;
	
	/**
	 * 获取指定老师昨天的关注信息
	 * 
	 * @param request
	 */
	@RequestMapping(value = "yesterday", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取昨日粉丝统计信息", notes = "根据登录老师ID获取昨日老师的粉丝关注信息", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "GET")  
	@ApiResponses({@ApiResponse(code = 500, message = "内部异常")})
	public APIWrapper<AttentionStatistics> yesterday(HttpServletRequest request) {
		try {
			// 登录老师ID
			Long teacherId = null;
			CommonLoginInfo cli = UserAuth.GetUserInfoByRequest(request);
			if (cli != null && "True".equals(cli.getIslogin())) {
				teacherId = Long.parseLong(cli.getUserid());
			} else {
				return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, "用户未登录");
			}
			AttentionStatistics as = new AttentionStatistics();
			
			// 老师IDs
			List<Long> tids = new ArrayList<Long>();
			tids.add(teacherId);
			
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DATE, -1);
			
			// 查询数据库
			List<AttentionStatistics> result = attentionService.searchAttentions(tids, null, null, DateUtil.DateToString(now.getTime(), "yyyy-MM-dd"), 1, Integer.MAX_VALUE);
			if (result != null && !result.isEmpty()) {
				as = result.get(0);
			}
			return APIWrapMapper.wrap(APIWrapper.SUCCESS_CODE, APIWrapper.SUCCESS_MESSAGE, as);
		} catch (Exception e) {
			log.error("** get yesterday attention report error {} **", e);
			return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, APIWrapper.ERROR_MESSAGE);
		}
	}
	
	/**
	 * 获取指定时间段的统计数据
	 * 
	 * @param request
	 * @param fromDate 开始时间
	 * @param toDate 结束时间
	 * @param page 第一页 从1开始
	 * @param count 数量 默认10
	 */
	@RequestMapping(value = "report", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取某个时间段内老师粉丝统计信息", notes = "根据登录老师输入的开始、结束时间分页获取指定老师的粉丝关注信息", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "GET")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "from_date", value = "开始时间yyyy-MM-dd", required = true, dataType = "String", paramType = "query"),  
        @ApiImplicitParam(name = "to_date", value = "结束时间yyyy-MM-dd", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "page", value = "当前页", required = false, dataType = "Long", paramType = "query"),
        @ApiImplicitParam(name = "count", value = "每页数量", required = false, dataType = "Long", paramType = "query")})  
	@ApiResponses({@ApiResponse(code = 500, message = "内部异常")})
	public APIWrapper<ResultBean> report(HttpServletRequest request,
			@RequestParam(value = "from_date", required = true) String fromDate,
			@RequestParam(value = "to_date", required = true) String toDate,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
		try {
			// 参数校验
			String errorMsg = validate(fromDate, toDate);
			if (StringUtil.isNotBlank(errorMsg)) {
				return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, errorMsg);
			}
			// 每页数量校验
			if (count != null && count.intValue() > 50) {
				return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, "每页数量不允许大于50条");
			}
			// 登录老师ID
			Long teacherId = null;
			CommonLoginInfo cli = UserAuth.GetUserInfoByRequest(request);
			if (cli != null && "True".equals(cli.getIslogin())) {
				teacherId = Long.parseLong(cli.getUserid());
			} else {
				return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, "用户未登录");
			}
			// 老师IDs
			List<Long> tids = new ArrayList<Long>();
			tids.add(teacherId);
			
			// 查询数据库
			// 1. 分页结果集
			List<AttentionStatistics> result = attentionService.searchAttentions(tids, fromDate, toDate, null, page, count);
			// 2. 符合条件记录数
			Integer totalRows = attentionService.searchAttentionsCount(tids, fromDate, toDate, null);
			
			// 响应结果集
			ResultBean rb = new ResultBean();
			Pager pager = new Pager(page, count, totalRows);
			rb.setPager(pager);
			rb.setData(result);
			
			return APIWrapMapper.wrap(APIWrapper.SUCCESS_CODE, APIWrapper.SUCCESS_MESSAGE, rb);
		} catch (Exception e) {
			log.error("** get attention report error {} **", e);
			return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, APIWrapper.ERROR_MESSAGE);
		}
	}
	
	/**
	 * 获取指定时间段的统计数据
	 * 
	 * @param fromDate 开始时间
	 * @param toDate 结束时间
	 * @param teacherId 老师ID
	 * @param code 验证码
	 * @param page 第一页 从1开始
	 * @param count 数量 默认10
	 */
	@RequestMapping(value = "report-for-backend", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取某个时间段内老师粉丝统计信息-后台用,需要验签", notes = "根据师输入的开始、结束时间分页获取指定老师的粉丝关注信息", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "GET")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "from_date", value = "开始时间yyyy-MM-dd", required = true, dataType = "String", paramType = "query"),  
        @ApiImplicitParam(name = "to_date", value = "结束时间yyyy-MM-dd", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "teacherId", value = "老师ID", required = true, dataType = "Long", paramType = "query"),
        @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "Long", paramType = "query"),
        @ApiImplicitParam(name = "count", value = "每页数量", required = true, dataType = "Long", paramType = "query")})  
	@ApiResponses({@ApiResponse(code = 500, message = "内部异常")})
	public APIWrapper<ResultBean> reportForBackend(@RequestParam(value = "from_date", required = true) String fromDate,
			@RequestParam(value = "to_date", required = true) String toDate,
			@RequestParam(value = "teacherId", required = true) Long teacherId,
			@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "count", required = true) Integer count) {
		try {
			// 参数校验
			String errorMsg = validate(fromDate, toDate);
			if (StringUtil.isNotBlank(errorMsg)) {
				return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, errorMsg);
			}
			// 每页数量校验
			if (count != null && count.intValue() > 50) {
				return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, "每页数量不允许大于50条");
			}
			// 验签
			if (StringUtils.isBlank(code)) {
				return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, "验证码不允许为空");
			} else {
				Map<String, String> params = new HashMap<String, String>();
				params.put("fromDate", fromDate);
				params.put("toDate", toDate);
				params.put("teacherId", teacherId + "");
				params.put("page", page + "");
				params.put("count", count + "");
				String _code = Encrypt.getMD5(ParameterUtils.createSignText(params) + "HeXun8565");
				if (!_code.equalsIgnoreCase(code)) {
					return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, "验签错误");
				}
			}
			// 老师IDs
			List<Long> tids = new ArrayList<Long>();
			tids.add(teacherId);
			
			// 查询数据库
			// 1. 分页结果集
			List<AttentionStatistics> result = attentionService.searchAttentions(tids, fromDate, toDate, null, page, count);
			// 2. 符合条件记录数
			Integer totalRows = attentionService.searchAttentionsCount(tids, fromDate, toDate, null);
			
			// 响应结果集
			ResultBean rb = new ResultBean();
			Pager pager = new Pager(page, count, totalRows);
			rb.setPager(pager);
			rb.setData(result);
			
			return APIWrapMapper.wrap(APIWrapper.SUCCESS_CODE, APIWrapper.SUCCESS_MESSAGE, rb);
		} catch (Exception e) {
			log.error("** get attention report error {} **", e);
			return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, APIWrapper.ERROR_MESSAGE);
		}
	}
	
	/**
	 * 按照类型获取指定时间段的统计数据
	 * 
	 * @param response
	 * @param request
	 * @param fromDate 开始时间
	 * @param toDate 结束时间
	 * @param type 1=新关注人数 2=取消关注人数 3=净增关注人数 4=累计关注人数
	 * @return
	 */
	@RequestMapping(value = "statistics", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取某个时间段内指定类型的老师粉丝统计信息", notes = "根据输入的开始、结束时间分页获取指定老师的粉丝关注信息", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "GET")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "from_date", value = "开始时间yyyy-MM-dd", required = true, dataType = "String", paramType = "query"),  
        @ApiImplicitParam(name = "to_date", value = "结束时间yyyy-MM-dd", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "type", value = "类型1=新关注人数 2=取消关注人数 3=净增关注人数 4=累计关注人数", required = true, dataType = "Long", paramType = "query")})  
	@ApiResponses({@ApiResponse(code = 500, message = "内部异常")})
	public APIWrapper<List<ReportItem>> statistics(HttpServletRequest request,
			@RequestParam(value = "from_date", required = true) String fromDate,
			@RequestParam(value = "to_date", required = true) String toDate,
			@RequestParam(value = "type", required = true) Integer type) {
		List<ReportItem> res = new ArrayList<ReportItem>();
		try {
			// 参数校验
			String errorMsg = validate(fromDate, toDate);
			if (StringUtil.isNotBlank(errorMsg)) {
				return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, errorMsg);
			}
			// 登录老师ID
			Long teacherId = null;
			CommonLoginInfo cli = UserAuth.GetUserInfoByRequest(request);
			if (cli != null && "True".equals(cli.getIslogin())) {
				teacherId = Long.parseLong(cli.getUserid());
			} else {
				return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, "用户未登录");
			}
			// 老师IDs
			List<Long> tids = new ArrayList<Long>();
			tids.add(teacherId);
			// 查询数据库
			List<AttentionStatistics> result = attentionService.searchAttentions(tids, fromDate, toDate, null, 1, Integer.MAX_VALUE);
			if (result != null && !result.isEmpty()) {
				ReportItem ri = null;
				for (AttentionStatistics as : result) {
					ri = new ReportItem();
					ri.setDate(as.getDate());
					switch(type) {
						case 1:
							ri.setCount(as.getFocusCount());
							break;
						case 2:
							ri.setCount(as.getCancelCount());
							break;
						case 3:
							ri.setCount(as.getNetFocus());
							break;
						case 4:
							ri.setCount(as.getCurrentFans());
							break;
					}
					res.add(ri);
				}
			}
			return APIWrapMapper.wrap(APIWrapper.SUCCESS_CODE, APIWrapper.SUCCESS_MESSAGE, res);
		} catch (Exception e) {
			log.error("** get attention report error {} **", e);
			return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, APIWrapper.ERROR_MESSAGE);
		}
	}
	
	/**
	 * 在定时任务出现问题的时候，可以调用这个接口执行某天的统计数据
	 * 
	 * @param date 日期
	 * @param pwd 验证码
	 * @return
	 */
	@RequestMapping(value = "execute", method = RequestMethod.GET)
	@ResponseBody
	@ApiIgnore
	public APIWrapper<?> execute(@RequestParam(value = "date", required = true) String date,
			@RequestParam(value = "pwd", required = false, defaultValue = "") String pwd) {
		// 参数校验
		if (StringUtils.isBlank(pwd) || !MD5.getMD5Code("hexun8565").equalsIgnoreCase(pwd)) {
			return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, "无权限访问");
		}
		try {
			attentionService.execute(0, DateUtil.StringToDate(date, "yyyy-MM-dd"));
			return APIWrapMapper.wrap(APIWrapper.SUCCESS_CODE, APIWrapper.SUCCESS_MESSAGE);
		} catch (Exception e) {
			log.error("** execute manually error {} **", e);
			return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, e.getMessage());
		}
	}
	
	/**
	 * 日期校验
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	private String validate(String fromDate, String toDate) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			Date now = cal.getTime();
			Date fDate = DateUtil.StringToDate(fromDate, "yyyy-MM-dd");
			Date tDate = DateUtil.StringToDate(toDate, "yyyy-MM-dd");
			if (fDate.after(now)) {
				return "开始时间不允许大于当前时间";
			}
			if (tDate.after(now)) {
				return "结束时间不允许大于当前时间";
			}
			if (fDate.after(tDate)) {
				return "开始时间不允许大于结束时间";
			}
			if (DateUtil.daysBetween(fDate, tDate, "yyyy-MM-dd") > 365) {
				return "开始时间和结束时间只差不能大于一年";
			}
		} catch (ParseException e) {
			return "系统异常";
		}
		return "";
	}
	@RequestMapping(value = "static-attentions", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取某个日期一个或多个老师新增关注数、取消关注数、净增数、累计关注数", notes = "根据多个或一个用户id、具体日期，查询每个合作者的新增关注数、取消关注数、净增数、累计关注数", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "postTime", value = "查询日期yyyy-MM-dd", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "teacherIds", value = "批量老师id", required = false, dataType = "String", paramType = "query"), 
	@ApiImplicitParam(name = "code", value = "加密串", required = true, dataType = "String", paramType = "query")})  
	@ApiResponses({@ApiResponse(code = 500, message = "内部异常")})
	public APIWrapper<?> staticAttentions(@RequestParam(value="teacherIds",required=false) String teacherIds,
			@RequestParam(value="postTime",required=false) String postTime,
			@RequestParam(value="code",required=true) String code){
		List<AttentionStatistics> statistics = attentionService.getAttentionByWhere(teacherIds, postTime, code);
		if(statistics == null){
			return APIWrapMapper.wrap(APIWrapper.ERROR_CODE, "验签失败或用户id过长！");
		}
		return APIWrapMapper.wrap(APIWrapper.SUCCESS_CODE, APIWrapper.SUCCESS_MESSAGE, statistics);
	}
}
