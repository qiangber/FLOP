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
<script type="text/javascript" src="/FLOP/resources/js/reconnecting-websocket.min.js"></script>
<script type="text/javascript" src="/FLOP/resources/js/channel.js"></script>
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
					<li class="now"><a href="${pageContext.request.contextPath}/order/list.do">预约情况</a></li>
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
			<h4>预约情况</h4>
			<div class="nTab">
				<div class="TabTitle">
					<ul id="myTab1">
					<c:choose>
					<c:when test="${type == 'writing'}">
						<li id="li1" class="active" onclick="window.location.href='/FLOP/order/list.do?type=writing'">写作预约</li>					
						<li id="li2" class="normal" onclick="window.location.href='/FLOP/order/list.do?type=speaking'">口语预约</li>
						<li id="li2" class="normal" onclick="window.location.href='/FLOP/order/list.do?type=lab'">实验室预约</li>
						<form action="downloadAll.do" enctype="multipart/form-data" method="post">
							<input type="submit" value="导出所有作文" style="float:right;">
						</form>
					</c:when>
					<c:when test="${type == 'speaking'}">
						<li id="li1" class="normal" onclick="window.location.href='/FLOP/order/list.do?type=writing'">写作预约</li>					
						<li id="li2" class="active" onclick="window.location.href='/FLOP/order/list.do?type=speaking'">口语预约</li>
						<li id="li2" class="normal" onclick="window.location.href='/FLOP/order/list.do?type=lab'">实验室预约</li>
					</c:when>
					<c:when test="${type == 'lab'}">
						<li id="li1" class="normal" onclick="window.location.href='/FLOP/order/list.do?type=writing'">写作预约</li>					
						<li id="li2" class="normal" onclick="window.location.href='/FLOP/order/list.do?type=speaking'">口语预约</li>
						<li id="li2" class="active" onclick="window.location.href='/FLOP/order/list.do?type=lab'">实验室预约</li>
					</c:when>
					</c:choose>
					</ul>
				</div>
				<div class="statics">
					<div class="phone_pic" id="newsList1">
						<div id="newsList">
							<table id="newsTable">
								<tr>
									<th>时间</th>
									<th>预约人</th>
									<th>类型</th>
									<th>预约时间</th>
									<th>预约对象</th>
									<th>状态</th>
									<c:if test="${type == 'writing'}">
									<th>操作</th>
									</c:if>
								</tr>
								<c:forEach var="order" items="${orderList}">
								<tr>
									<td>${order.time}</td>
									<td>${order.userInfo.name}(${order.userInfo.username})</td>
									<td>${order.appoint.category.name}</td>
									<td>${order.appoint.date},第${order.appoint.lesson}节</td>
									<td>${order.appoint.userInfo.name}(${order.appoint.userInfo.username})</td>
									<c:choose>
										<c:when test="${order.status == 'accept'}">
											<td>成功</td>
										</c:when>
										<c:when test="${order.status == 'cancel'}">
											<td>被取消</td>
										</c:when>
										<c:when test="${order.status == 'close'}">
											<td>已关闭</td>
										</c:when>
										<c:when test="${order.status == 'open'}">
											<td>预约中</td>
										</c:when>
										<c:otherwise>
											<td>失败</td>
										</c:otherwise>
									</c:choose>
									<c:if test="${type == 'writing'}">
									<td>
										<a href="preSave.do?id=${order.id}">查看</a>|<a href="download.do?id=${order.id}">下载</a>
									</td>
									</c:if>
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
NotificationChannel.init("ws://112.74.196.248:80/FLOP/notification-channel?userId=${adminuser.id}");
</script>
</html>