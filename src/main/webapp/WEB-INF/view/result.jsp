<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关注关系统计</title>
<script type="text/javascript" src="${ctx }/static/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="${ctx }/static/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body>

	<table>
		<tr>
			<td>
				请选择日期:
				<input type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd', onpicked:pickedFunc})" class="Wdate" value="${date }" />
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2">
				<table cellpadding="10" cellspacing="1" bgcolor="#CCC">
					<tr>
						<td bgcolor="#EEE" align="center">老师ID</td>
						<td bgcolor="#EEE" align="center">老师名称</td>
						<td bgcolor="#EEE" align="center">关注次数</td>
						<td bgcolor="#EEE" align="center">取消关注次数</td>
						<td bgcolor="#EEE" align="center">日期</td>
					</tr>
					<c:forEach items="${result }" var="ats">
					<tr>
						<td bgcolor="#FFF" align="center">${ats.teacherId }</td>
						<td bgcolor="#FFF" align="center">${ats.teacherName }</td>
						<td bgcolor="#FFF" align="center">${ats.addAttentionCount }</td>
						<td bgcolor="#FFF" align="center">${ats.cancelAttentionCount }</td>
						<td bgcolor="#FFF" align="center"><fmt:formatDate pattern="yyyy-MM-dd" value="${ats.date }" /></td>
					</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</table>
	
	
	
	<script>
		function pickedFunc() {
			var date = $(this).val(); 
			window.location.href = "${ctx}/attention/report?date=" + date;
		}
	</script>

</body>
</html>
