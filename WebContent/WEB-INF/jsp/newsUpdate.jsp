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
<script type="text/javascript" src="/FLOP/resources/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/FLOP/resources/ueditor1_4_3-utf8-jsp/ueditor.config.js"></script>
<!-- ueditor源码文件 -->
<script type="text/javascript" charset="utf-8" src="/FLOP/resources/ueditor1_4_3-utf8-jsp/ueditor.all.min.js"></script>
<script type="text/javascript" src="/FLOP/resources/ueditor1_4_3-utf8-jsp/lang/zh-cn/zh-cn.js"></script>
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
					<li class="now"><a href="${pageContext.request.contextPath}/news/list.do">公示公告</a></li>
					<li><a href="${pageContext.request.contextPath}/appoint/list.do">预约管理</a></li>
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
			<h4>公示公告</h4>
			<div class="nTab">
				<div class="TabTitle">
					<ul id="myTab1">
						<li id="li1" class="normal" onclick="window.location.href='list.do'">公告列表</li>
						<li id="li2" class="active">公告修改</li>
					</ul>
				</div>
				<div class="statics">
					<div class="phone_pic" id="newsList0">
						<form action="save.do" method="post" name="newsForm" id="newsForm">
							<div class="newsTitle">
								<div class="underLine">
									<span style="font-size: 15px;">公告主题</span>
								</div>
								<div class="centerInput">
									<textarea style="width: 690px; height: 50px; resize: none;"
										name="title" id="newsTitle">${news.title}</textarea>
								</div>
							</div>							
	
							<div class="newsContent">
								<div class="underLine">
									<span style="font-size: 15px;">详细内容</span>
								</div>										
								<div class="centerInput">
									<%-- <textarea style="width: 690px; height: 800px; resize: none;"
										name="content" id="newsContent">${news.content}</textarea> --%>
									<script type="text/plain" id="newsEditor" style="width:710px;height:800px;" name="content">${news.content}</script>
								</div>
							</div>
							<div style="text-align: center">
								<input type="submit" value="确认添加" />
							</div>
							<input type="hidden" name="id" value="${news.id}">
						</form>
					</div>
				</div>
			</div>					
		</div>
	</div>
	<div id="footer">Copyright &copy; 2017 电子科技大学外国语学院外语学习平台</div>
</body>

<script>
//初始化UEditor
var ue = UE.getEditor("newsEditor", {toolbars: [[
	'undo', 'redo', '|',
	'simpleupload', 'justifyleft', 'justifycenter', 'justifyright', '|',
	'bold', 'italic', 'underline', 'removeformat', 'blockquote', '|',
	'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', '|',
	'fontfamily', 'fontsize', 'horizontal', 'spechars', 'preview', 'searchreplace'
]]});
//用户注销函数
function logOut() {
	var isLogOut = confirm("您要注销登录吗？");
	if (isLogOut == true) {
		document.outFrm.submit();
	}
}
</script>
</html>