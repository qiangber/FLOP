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
<link rel="stylesheet" type="text/css"
	href="/FLOP/resources/css/common.css" />
<link rel="stylesheet" type="text/css"
	href="/FLOP/resources/css/adminUser.css" />
<link rel="shortcut icon" href="/FLOP/resources/images/favicon.ico" />
<script type="text/javascript"
	src="/FLOP/resources/js/jquery-1.6.1.min.js"></script>
</head>

<body>
	<div id="header"></div>
	<div id="banner">
		<div class="info">
			<span class="l">${adminuser.name}，你好，欢迎使用电子科技大学外语学习平台管理系统！</span> <span
				class="r"> <jsp:useBean id="now" class="java.util.Date" /> <fmt:formatDate
					value="${now}" type="both" dateStyle="long"
					pattern="yyyy-MM-dd HH:mm" />
			</span>
		</div>
	</div>
	<div id="wrap">
		<div id="left">
			<div class="funcbox">
				<ul>
					<li><a href="${pageContext.request.contextPath}/news/list.do">公示公告</a></li>
					<li><a
						href="${pageContext.request.contextPath}/appoint/list.do">实验室预约</a></li>
					<li><a href="${pageContext.request.contextPath}/order/list.do">预约情况</a></li>
					<li><a
						href="${pageContext.request.contextPath}/category/list.do">预约类型</a></li>
					<li><a href="${pageContext.request.contextPath}/about/list.do">相关介绍</a></li>
					<li><a href="${pageContext.request.contextPath}/resource/list.do">资源导航</a></li>
					<li><a
						href="${pageContext.request.contextPath}/user/list.do?type=teacher">老师用户管理</a></li>
					<li><a
						href="${pageContext.request.contextPath}/user/list.do?type=student">学生用户管理</a></li>
					<li class="now"><a
						href="${pageContext.request.contextPath}/user/pwd.do">密码管理</a></li>
					<li>
						<form name="outFrm"
							action="${pageContext.request.contextPath}/user/loginout.do"
							method="post">
							<a href="#" onclick="logOut();">注销登录</a>
						</form>
					</li>
				</ul>
			</div>
		</div>
		<div id="right">
			<h4>密码修改</h4>
			<form action="pwdUpdate.do" method="post"
				onsubmit="return submitCheck();">
				<div class="pass" style="margin-top: 12px;">
					<div class="item">
						<div class="title">用户名：</div>
						<input id="username" name="username" type="text"
							value="${username}" />
						<input name="oldusername" value="${username}" type="hidden"/>
					</div>
					<div class="item">
						<div class="title">原密码：</div>
						<input id="oldpassword" name="oldpassword" type="password"
							onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" />
					</div>
					<div class="item">
						<div class="title">新密码：</div>
						<input id="newpassword" name="newpassword" type="password"
							onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" />
					</div>
					<div class="item">
						<div class="title">确认密码：</div>
						<input id="repassword" name="repassword" type="password"
							onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" />
					</div>
					<div class="btncontain">
						<button class="adduserbtn" type="submit"
							style="margin-left: 100px">确认修改</button>
					</div>
				</div>
				<input type="hidden" name="id" value="${id}" />
			</form>
		</div>
	</div>
	<div id="footer">Copyright &copy; 2016 电子科技大学外国语学院外语学习平台</div>
</body>

<script>
function submitCheck() {
    if (document.getElementById("username").value.match(/[\W]/g)) {
        alert("用户名只能输入英文和数字！");
        document.getElementById("username").value = "";
        return false;
    }
    if ((document.getElementById("oldpassword").value == "")) {
        alert("请正确填写原密码！");
        document.getElementById("oldpassword").value = "";
        document.getElementById("newpassword").value = "";
        document.getElementById("repassword").value = "";
        return false;
    }
    if (document.getElementById("newpassword").value.match(/[\W]/g)) {
        alert("密码只能输入英文和数字！");
        document.getElementById("newpassword").value = "";
        document.getElementById("repassword").value = "";
        return false;
    }
    if (document.getElementById("newpassword").value != document.getElementById("repassword").value) {
        alert("新密码两次输入不一致！");
        document.getElementById("oldpassword").value = "";
        document.getElementById("newpassword").value = "";
        document.getElementById("repassword").value = "";
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