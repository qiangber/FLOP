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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/adminUser.css" />
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
			<h4>添加用户</h4>
	        <div class="pass" style="margin-top:12px;">
	        <form name="editFrm" action="add.do" enctype="multipart/form-data" method="post" onsubmit="return checkSubmit();">
				<div class="item"><a href="download.do"><span style="font-family: Arial; font-size: 10px;color: red;">下载模板Excel表格</span></a></div>
				<div class="item">
					<span style="font-size:15px">选择文件(</span><span style="font-family: Arial; font-size: 10px;color: red;">必须上传按模板格式填写的Excel文件</span>)：<input type="file" name="excel" id="excel"/>
				</div>
	            <div class="btncontain">
	            	<input class="adduserbtn" type="button" value="返回列表" onclick="window.location.href='list.do?type=${type}';"/>
	            	<input class="adduserbtn" type="submit" value="确认添加"/>
	            </div>
	            <div class="item" style="color:red;font-size:15px">${result}</div>
	        </form> 
	        </div>					
		</div>
	</div>
	<div id="footer">Copyright &copy; 2016 电子科技大学外国语学院外语学习平台</div>
</body>

<script>
function checkSubmit() {
	if (document.getElementById("excel").value == "") {
		alert("请选择待上传的用户名单！");
		return false;
	} else {
		var arr = document.getElementById("excel").value.split(".");
		var last = arr.length - 1;
		if ((arr[last] != "xls" && arr[last] != "XLS") && (arr[last] != "xlsx" && arr[last] != "XLSX")) {
			alert("上传的Excel文件必须为xls或xlsx格式！");
			return false;
		}
	}
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