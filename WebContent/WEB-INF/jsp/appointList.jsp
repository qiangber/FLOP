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
						<li id="li1" class="active" onclick="window.location.href='/FLOP/appoint/list.do'">已有预约</li>
						<li id="li2" class="normal" onclick="window.location.href='/FLOP/appoint/preSave.do'">可预约添加</li>
						<li id="li2" class="normal" onclick="window.location.href='/FLOP/order/listToDeal.do'">预约处理</li>
					</ul>
				</div>
				<div class="statics">
					<div class="phone_pic" id="newsList1">
						<div id="newsList">
							<table id="newsTable">
								<tr>
									<th>id</th>
									<th>时间</th>
									<th>时段</th>
									<th>实验室</th>
								</tr>
								<c:forEach var="appoint" items="${appointList}">
								<tr>
									<td>${appoint.id}</td>
									<td>${appoint.date}</td>
									<td>${appoint.lesson}</td>
									<td>${appoint.category.name}</td>
								</tr>			
								</c:forEach>
							</table>
							<div class="pageCommon" style="cursor:hand">
								<div class="scottPage">
								    <c:if test="${currentPage != 1}">
								    	<span style="cursor:pointer"><a href="list.do?page=${1}">首页 </a></span>
						            	<a href="list.do?page=${currentPage-1}">上一页 </a>
						            </c:if>
								    <span class="currentPage" >${currentPage}/${pageCount}</span>
						            <c:if test="${currentPage != pageCount}">
						            	<a href="list.do?page=${currentPage+1}">下一页 </a>
									    <span style="cursor:pointer"><a href="list.do?page=${pageCount}">尾页 </a></span>
						            </c:if>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>					
		</div>
	</div>
	<div id="footer">Copyright &copy; 2016 电子科技大学外国语学院外语学习平台</div>
</body>

<script>
//用户注销函数
function logOut() {
	var isLogOut = confirm("您要注销登录吗？");
	if (isLogOut == true) {
		document.outFrm.submit();
	}
}
function isDelete() {
	var isDelete = confirm("您确定要删除该条信息吗？");
	return isDelete
}
function submitData() {
	var searchTitle = document.getElementById("searchTitle").value;
	var searchDate = document.getElementById("searchDate").value;
	$.ajax({ url: "search.do",
        type:"get",           
        dataType:"application/x-www-form-urlencoded; charset=utf-8",
        data: "title="+searchTitle,
        success:function(data){ 
            alert();
      },
      error: function (msg) {
          $("#allNews").html(msg.responseText);
      }
        })
}
</script>
</html>