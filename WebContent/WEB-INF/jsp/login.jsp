<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>欢迎登录电子科技大学外语学习平台管理系统</title>
<link rel="shortcut icon" href="/FLOP/resources/images/favicon.ico" />
<style type="text/css">
body,div,img,a {
	margin:0 auto;
	padding:0;
	border:none;
	font-family:"微软雅黑", "宋体", arial, sans-serif;
}
#header {
	width:950px;
	height:60px;
}
#banner {
	width:100%;
	height:72px;
	background:url(/FLOP/resources/images/banner_bg.png) repeat-x;
}
.title {
	width:950px;
	height:72px;
	color:#fff;
	font-size:20px;
	line-height:50px;
}
#login {
	margin-left:100px;
	margin-top:90px;
	width:400px;
	height:260px;
	padding-top:80px;
	background:url(/FLOP/resources/images/login_bg.png) no-repeat;
}
#bg {
	width:1090px;
	height:362px;
	padding-top:40px;
	background:url(/FLOP/resources/images/div_bg.png) no-repeat;
}
.item {
	width:360px;
	height:28px;
	float:left;
	font-size:12px;
	line-height:24px;
}
.item2 {
	width:360px;
	height:36px;
	float:left;
}
.itemname {
	width:136px;
	float:left;
	text-align:right;
}
.itemcon {
	width:176px;
	float:left;
}
.submit {
	width:64px;
	margin-top:12px;	
	padding-top:4px;
}
</style>
</head>

<body>
<form name="frm" action="/FLOP/user/login.do" method="post">
    <div id="header">
        <img src="/FLOP/resources/images/admin_logo.jpg" style="height:61px;"/>
    </div>
    <div id="banner">
        <div class="title">欢迎使用电子科技大学外语学习平台管理系统，请先登录！</div>
    </div>
    <div id="bg">
	    <div id="login">
	    	<div class="item"><div class="itemname">用户名：</div><input name="username" id="userid" class="itemcon" type="text" /></div>
	    	<div class="item"><div class="itemname">密码：</div><input name="password" id="userpwd" class="itemcon" type="password" /></div>
	 	    <div class="item"><div class="itemname">验证码：</div><input  id="verifyCode" name="verifyCode" class="itemcon" type="text" /></div>
	    	<div class="item2"><div class="itemname">&nbsp;</div><a href="#"><img id="imgObj" alt="" src="/FLOP/VerifyCodeAction" onclick="reloadCode()"/></a> <a href="#" onclick="reloadCode()">换一张</a> </div>
	    	<div class="item2"><div class="itemname">&nbsp;</div><input class="submit" type="submit" value="登 录" /><input class="submit" type="reset" value="重 填" style="margin-left:27px;"/></div>
    		<div class="item2"><div class="itemname">&nbsp;</div><font color="red">${loginError}</font></div>
	    </div>
    </div>
    </form>
</body>
<script>
//无刷新重载图片
function reloadCode(){
	var image=document.getElementById("imgObj");
	image.src="/FLOP/VerifyCodeAction?a=" + new Date().getTime();
}
</script>
</html>