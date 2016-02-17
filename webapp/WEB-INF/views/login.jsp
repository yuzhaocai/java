<%@page import="org.apache.shiro.SecurityUtils"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<%@ include file="/WEB-INF/jspf/taglib.jsp" %>
<%
if (SecurityUtils.getSubject().isAuthenticated()) {
    response.sendRedirect(request.getContextPath());
}
%>

<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <title>登录</title>
    
    <%@include file="/WEB-INF/jspf/jslib.jsp" %>
    
    <style>
	    html,body{
		    height:100%;
			background-color:#333
	    }
    </style>
</head>

<body>

<!--=== Login Wrap ===-->  
<div class="login-wrap">
	<!-- login banner -->
	<div class="login-banner">
    <div class="carousel slide carousel-v2" id="portfolio-carousel">
    <div class="loginNewyearRight">
      <div class="container loginIMGForm">
        <div class="loginBannerImg"></div>
        <!-- login form -->
        <div class="login-form">
        	<h4>会员登录 <small><a href="${ctx }/register" class="hightlight pull-right"><i class="fa fa-arrow-circle-o-right"></i>立即注册</a></small></h4>
        	
        	<c:if test="${!empty shiroLoginFailure }">
        	<p class="alert alert-danger">
			    <c:choose>
			       <c:when test="${fn:containsIgnoreCase(shiroLoginFailure, 'DisabledAccountException') }">
			         <i class="fa fa-lightbulb-o"></i>&nbsp;<c:out value="该账号已被禁用！"/>
			       </c:when>
			       <c:when test="${fn:containsIgnoreCase(shiroLoginFailure, 'IncorrectCaptchaException') }">
			         <i class="fa fa-lightbulb-o"></i>&nbsp;<c:out value="验证码输入不正确！"/>
			       </c:when>
			       <c:otherwise>
			         <i class="fa fa-lightbulb-o"></i>&nbsp;<c:out value="登录失败，用户名或密码不正确！"/>
			       </c:otherwise>
			     </c:choose>              
        	</p>
        	</c:if>
        	
        	<form id="loginForm" action="${ctx}/login" method="post">
            	<div class="form-group has-feedback">
                	<div class="input-group ">
                    	<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                		<input type="text" class="form-control" placeholder="手机号" id="username" name="username" value="${param.username}" maxlength="11">
                    </div>
                </div>
            	<div class="form-group has-feedback">
                	<div class="input-group">
                    	<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                		<input type="password" class="form-control" placeholder="密码" id="password" name="password" value="${param.password}" maxlength="20">
                    </div>
                </div>
            	<div class="form-group has-feedback">
               	  <div class="input-group">
                	<input type="text" class="form-control" maxlength="4" placeholder="请输入验证码" id="captcha" name="captcha">
                   	<span class="input-group-addon captcha"><img id="img_captcha" src="${ctx}/images/kaptcha.jpg" style="cursor: pointer" title="验证码看不清？点击换一张"></span>
                  </div>
                </div>
            	<div class="form-group">
<!-- 	                	<label class="checkbox-inline"><input type="checkbox">自动登录</label> -->
<!--                 		<label class="checkbox-inline"><input type="checkbox">安全控件登录</label> -->
                    <a href="${ctx}/findPwd" class="pull-right">忘记密码？</a>
                </div>
                <input type="hidden" name="savedURL" value="${param.savedURL }" />
                <button type="submit" class="btn-u btn-u-red">登录</button>
            </form>
        </div>
      </div>
    </div>
    </div>
	</div>
    <!-- end login banner -->
</div>
<!--=== End Login Wrap ===-->





<script type="text/javascript">
  
$(function() {
	$('#img_captcha').click(function() {
		$(this).attr({src:"${ctx}/images/kaptcha.jpg?t=" + Math.random()});
	});
	
	//为表单注册validate函数
	$("#loginForm").validate({
		rules : {
				username : {
					required : true,
				},
				password : {
					required : true,
					rangelength : [6,20]
				},
				captcha : {
					required : true
				}
			},
			messages : {
				username : {
					required: "请输入手机号",
				},
				password : {
					required : "请输入密码",
					rangelength : '密码应为6~20位字符'
				},
				captcha : {
					required : "请输入验证码"
				}
			}
		});

		$('#loginForm input:first').focus();
	});
</script>

</body>
</html>

