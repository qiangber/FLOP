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
<link rel="stylesheet" type="text/css" href="/FLOP/resources/css/adminUser.css" />
<link rel="shortcut icon" href="/FLOP/resources/images/favicon.ico" />
<script type="text/javascript" src="/FLOP/resources/js/jquery-1.6.1.min.js"></script>
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
					<li><a href="${pageContext.request.contextPath}/appoint/list.do">实验室预约</a></li>
					<li><a href="${pageContext.request.contextPath}/order/list.do">预约情况</a></li>
					<li><a href="${pageContext.request.contextPath}/category/list.do">预约类型</a></li>
					<li><a href="${pageContext.request.contextPath}/about/list.do">相关介绍</a></li>
					<li><a href="${pageContext.request.contextPath}/resource/list.do">资源导航</a></li>
					<li><a href="${pageContext.request.contextPath}/user/list.do?type=teacher">老师用户管理</a></li>
					<li class="now"><a href="${pageContext.request.contextPath}/user/list.do?type=student">学生用户管理</a></li>
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
			<h4>学生用户管理</h4>
			<div class="nTab">
				<div class="TabTitle">
					<ul id="myTab1">
						<li id="li1" class="normal" onclick="window.location.href='list.do?type=student'">用户列表</li>
						<li id="li2" class="active">用户修改</li>
					</ul>
				</div>
				<div class="statics">
					<div class="phone_pic" id="newsList0">
						<form action="save.do" method="POST" name="formUser" onsubmit="return onsubmitCheck();">
					        <div class="edituser">
					        	<div class="item"><div class="title">姓名：</div><input name="name" id="name" type="text" value="${user.userInfo.name}"/></div>
					        	<div class="item"><div class="title">性别：</div><input name="sex" id="sex" type="text" value="${user.userInfo.sex}" /></div>
					        	<div class="item"><div class="title">学院：</div><input name="college" id="college" type="text" value="${user.userInfo.college}" /></div>
					        	<div class="item"><div class="title">年级：</div><input name="grade" id="grade" type="text" value="${user.userInfo.grade}" /></div>
					        	<div class="item"><div class="title">班级：</div><input name="team" id="team" type="text" value="${user.userInfo.team}" /></div>
					            <div class="item"><div class="title">账号：</div><input name="username" id="username" type="text" value="${user.username}" autocomplete="off"/></div>
					            <div class="item"><div class="title">密码：</div><input name="password" id="password" type="password" value="${user.password}" autocomplete="off"/></div>
					            <div class="item">
					            	<div class="title">权限：</div>
					            	<c:choose>
					            	<c:when test="${user.userInfo.writing == true}">
					            	<label><input name="writing" type="checkbox" value="true" checked="checked"/>写作预约</label>	        
					            	</c:when>
					            	<c:otherwise>
					            	<label><input name="writing" type="checkbox" value="true"/>写作预约</label>
					            	</c:otherwise>
					            	</c:choose>
					            	<c:choose>
					            	<c:when test="${user.userInfo.speaking == true}">
					            	<label><input name="speaking" type="checkbox" value="true" checked="checked"/>口语预约</label>					        
					            	</c:when>
					            	<c:otherwise>
					            	<label><input name="speaking" type="checkbox" value="true"/>口语预约</label>
					            	</c:otherwise>
					            	</c:choose>
								</div>
					            
					            <div class="btncontain">
					            	<input class="adduserbtn" type="submit" value="确认添加"/>
					            </div>
					        </div>
						    <input type="hidden" name="type" value="student" />
						    <input type="hidden" name="id" value="${user.id}" /> 
				        </form>
					</div>
				</div>
			</div>					
		</div>
	</div>
	<div id="footer">Copyright &copy; 2016 电子科技大学外国语学院外语学习平台</div>
</body>

<script>
function onsubmitCheck()
{
	if((document.getElementById("name").value=="")
			||(document.getElementById("sex").value=="")
			||(document.getElementById("college").value=="")
			||(document.getElementById("grade").value=="")
			||(document.getElementById("team").value=="")
			||(document.getElementById("username").value=="")
			||(document.getElementById("password").value==""))
	{
		alert("请填写完整！");
		return false;
	}
	return true;
}
//用户注销函数
function logOut() {
	var isLogOut = confirm("您要注销登录吗？");
	if (isLogOut == true) {
		document.outFrm.submit();
	}
}
</script>
</html>