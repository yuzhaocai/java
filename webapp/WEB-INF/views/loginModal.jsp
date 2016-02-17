<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<form class="login-wrap" id="login-form" action="${ctx}/login?savedURL=${savedURL}" method="post">
	<div class="modal-header">
	  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	  <h4 class="modal-title" id="myModalLabel">会员登录</h4>
	</div>
	<div class="modal-body">
		<p class="alert alert-danger hide"></p>
		
     	<div class="form-group has-feedback">
         	<div class="input-group">
             	<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
         		<input class="form-control" placeholder="手机号" type="text"  id="username" name="username" value="${param.username}" maxlength="11">
             </div>
         </div>
     	<div class="form-group has-feedback">
         	<div class="input-group">
             	<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
         		<input class="form-control" placeholder="密码" type="password" id="password" name="password" value="${param.password}" maxlength="20">
             </div>
         </div>
     	<div class="form-group has-feedback">
        	  <div class="input-group">
         		<input class="form-control" maxlength="4" placeholder="验证码" type="text" id="captcha" name="captcha">
            	  <span class="input-group-addon captcha"><img id="img_captcha" src="${ctx}/images/kaptcha.jpg" style="cursor: pointer"></span>
             </div>
        </div>
        
	   	<div class="form-group">
	           <a href="${ctx}/findPwd">忘记密码？</a><a href="${ctx }/register" class="pull-right">立即注册</a>
	    </div>
	
	</div>
	<div class="modal-footer">
		<button type="submit" class="btn-u btn-u-red col-sm-12" id="btn-login" >登录</button>
	</div>
</form>

<script type="text/javascript">

$('#img_captcha').click(function() {
	$(this).attr({src:"${ctx}/images/kaptcha.jpg?t=" + Math.random()});
});

//为表单注册validate函数
$("#login-form").validate({
	rules : {
		username: {
			required:true,
		},
		password: {
			required:true,
			rangelength : [6,20]
		},
		captcha:  {
			required:true
		}
	},
	messages: {
		username: {
		  required: "请输入手机号",
		},
		password: {
		 required: "请输入密码",
		 rangelength : "密码应为6~20位字符"
		},
		captcha: {
		 required: "请输入验证码"
		}
	}
});
	
</script>
