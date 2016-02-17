<%@page import="com.lczy.media.util.UserContext"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!-- Static navbar -->
<div class="row header">
	<div class="nav"><!-- nav -->
	  <div class="navbar-top"><!-- nav -->
	    <div class="container-fluid">
	      <img src="${ctx}/static/assets/img/headers/c-logo.png" height="50" /> &nbsp;&nbsp;<span class="tel"> 系统管理后台</span>
	      
	      <div class="navbar-top-right">
					<shiro:guest>
					   <a href="${ctx}/login">登录</a>
					</shiro:guest>
					
					<shiro:user>
					   <span id="greeting">您好，<shiro:principal />！</span> 
					   <span class="cutline"></span> 
					   <a href="#" id="changePwd">修改密码</a>
					   <span class="cutline"></span> 
					   <a href="${ctx}/logout">退出登录</a>
					</shiro:user>
	      </div>
	    </div>
	  </div>
	  
	</div><!-- / nav -->
</div><!-- / Header -->
