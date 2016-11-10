<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html;charset=utf-8"
	import="java.util.*,java.sql.Timestamp" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>电子科技大学外语学习平台管理系统</title>
<link rel="stylesheet" type="text/css" href="/FLOP/resources/css/common.css" />
<link rel="stylesheet" type="text/css" href="/FLOP/resources/css/adminNews.css" />
<link rel="shortcut icon" href="/FLOP/resources/images/favicon.ico" />
<script type="text/javascript" src="/FLOP/resources/jedate/jquery-1.7.2.js"></script>
<script type="text/javascript" src="/FLOP/resources/jedate/jquery.jedate.min.js"></script>
<link type="text/css" rel="stylesheet" href="/FLOP/resources/jedate/skin/jedate.css">
</head>

<body>
	<div id="header"></div>
	<div id="banner">
		<div class="info">
			<span class="l">${adminuser.name}，你好，欢迎使用电子科技大学外语学习平台管理系统！</span>
			<span class="r">
				<jsp:useBean id="now" class="java.util.Date" />
				<fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd HH:mm" />
			</span>
		</div>
	</div>
	<div id="wrap">
		<div id="left">
			<div class="funcbox">
				<ul>
					<li><a href="${pageContext.request.contextPath}/news/list.do">公示公告</a></li>
					<li class="now"><a href="${pageContext.request.contextPath}/appoint/list.do">实验室预约</a></li>
					<li><a href="${pageContext.request.contextPath}/order/list.do">预约情况</a></li>
					<li><a href="${pageContext.request.contextPath}/category/list.do">预约类型</a></li>
					<li><a href="${pageContext.request.contextPath}/about/list.do">相关介绍</a></li>
					<li><a href="${pageContext.request.contextPath}/resource/list.do">资源导航</a></li>
					<li><a href="${pageContext.request.contextPath}/user/list.do?type=teacher">老师用户管理</a></li>
					<li><a href="${pageContext.request.contextPath}/user/list.do?type=student">学生用户管理</a></li>
					<li><a href="${pageContext.request.contextPath}/user/pwd.do">密码管理</a></li>
					<li>
						<form name="outFrm" action="${pageContext.request.contextPath}/user/loginout.do" method="post">
							<a href="#" onclick="logOut();">注销登录</a> 
						</form>
					</li>
				</ul>
			</div>
		</div>
		<div id="right">
			<h4>实验室预约</h4>
			<div class="nTab">
				<div class="TabTitle">
					<ul id="myTab1">
						<li id="li1" class="normal" onclick="window.location.href='/FLOP/appoint/list.do'">已有预约</li>
						<li id="li2" class="active" onclick="window.location.href='/FLOP/appoint/preSave.do'">可预约添加</li>
						<li id="li2" class="normal" onclick="window.location.href='/FLOP/order/listToDeal.do'">预约处理</li>
					</ul>
				</div>
				<div class="statics">
					<div class="phone_pic" id="newsList0">
						<form action="save.do" method="post" name="newsForm" id="newsForm" onsubmit="return onsubmitCheck();">
							<div class="newsTitle">
								<!-- <div class="underLine">
									<span style="font-size: 15px;">日期</span>
								</div> -->
								<div class="leftInput">
									<input type="text" id="datepicker" name="date" placeholder="请选择日期" readOnly="true" />
									<%-- <select name="date" style="width:100px;">
										<c:forEach var="date" items="${dateList}">
											<option value="${date}">${date}</option>
										</c:forEach>
									</select> --%>
								</div>
							</div>
							
							<div class="newsTitle">
								<div class="underLine">
									<!-- <span style="font-size: 15px;">时段</span> -->
								</div>
								<div class="leftInput">
									<c:forEach var="i" begin="1" end="5">
										<label><input name="lessons" type="checkbox" value="${i}"/>第${i}节</label>									
									</c:forEach>
									<br/><br/>
									<c:forEach var="i" begin="6" end="11">
										<label><input name="lessons" type="checkbox" value="${i}"/>第${i}节</label>									
									</c:forEach>
								</div>
							</div>								
	
							<div class="newsContent">
								<div class="underLine">
									<!-- <span style="font-size: 15px;">实验室</span> -->
								</div>										
								<div class="leftInput">
									<c:forEach var="category" items="${categoryList}">
										<label><input name="categoryId" type="radio" value="${category.id}"/>${category.name}</label> 
									</c:forEach>
								</div>
							</div>
							<div style="text-align: center">
								<input type="submit" value="确认添加" />
							</div>
						</form>
					</div>
				</div>
			</div>					
		</div>
	</div>
	<div id="footer">Copyright &copy; 2016 电子科技大学外国语学院外语学习平台</div>
</body>

<script>
function onsubmitCheck() {
	var flag = false;
	$("input[name='lessons']:checkbox").each(function() {
		if($(this).attr('checked')) {
			flag = true;
		}
	})
	if (flag) {
		if ($('input:radio[name="categoryId"]:checked').val() == null) {
			alert("请选择实验室！");
			return false;
		} else {
			return true;
		}
	} else {
		alert("请选择时段！");
		return false;
	}
}
$("#datepicker").jeDate({
    isinitVal:false,
    festival:true,
    ishmsVal:false,
    minDate: $.nowDate(2),
    maxDate: '2099-06-16',
    format:"YYYY/MM/DD",
    zIndex:3000,
})
//用户注销函数
function logOut() {
	var isLogOut = confirm("您要注销登录吗？");
	if (isLogOut == true) {
		outFrm.MSG_HEADER.value = "c2s_UserLogOut";
		document.outFrm.submit();
	}
}
</script>
</html>