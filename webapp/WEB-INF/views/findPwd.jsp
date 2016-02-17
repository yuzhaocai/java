<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>忘记密码-采媒在线</title>
</head>

<body>

<!--=== Content ===-->
<div class="container">
	<div class=" tag-box tag-box-v3 form-page mt20">
    	<div class="headline"><h3>忘记密码</h3></div>
        <div class="register-wrap">
	        <%-- 显示后台验证错误的标签 --%>
			<tags:fieldErrors commandName="vo" />
            <form id="findPwd-form" class="form-horizontal" action="${ctx }/findPwd"  method="post" style="width: 520px; margin-left: 200px;">
                
                <div class="form-group">
	                <label class="col-xs-4 control-label"><span class="color-red">*</span>手机号码:</label>
	                <div class="col-xs-8 has-feedback">
	                    <div class="input-group">
	                        <input type="text" class="form-control" id="mobPhone" name="mobPhone" maxlength="11" >
	                        <span class="input-group-addon"><i class="glyphicon glyphicon-phone"></i></span>
	                    </div>
	                </div>
	            </div>
                <div class="form-group">
                    <label class="col-xs-4 control-label"><span class="color-red">*</span>短信验证码:</label>
                    <div class="col-xs-8 has-feedback">
                        <div class="input-group">
                            <input type="text" maxlength="6" class="form-control" placeholder="输入短信验证码"  id="smscode" name="smscode" >
                            <span class="input-group-btn">
                                <button type="button" class="btn btn-default" id="btn-smscode">获取短信验证码</button>
                            </span>
                        </div>
                    </div>
                </div>                
                
                <div class="form-group">
                    <label class="col-xs-4 control-label"><span class="color-red">*</span>设置新密码:</label>
                    <div class="col-xs-8 has-feedback">
                        <div class="input-group">
                            <input type="password" class="form-control" placeholder="6-20位字符" id="password" name="password" maxlength="20" >
                            <span class="input-group-addon"><i class="fa fa-keyboard-o"></i></span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-4 control-label"><span class="color-red">*</span>重复新密码:</label>
                    <div class="col-xs-8 has-feedback">
                        <div class="input-group">
                            <input type="password" class="form-control" id="checkPassword" name="checkPassword" placeholder="请再次输入上面的密码" maxlength="20">
                            <span class="input-group-addon"><i class="fa fa-keyboard-o"></i></span>
                        </div>
                    </div>
                </div>
                
                <div class="form-group">
                    <div class="col-xs-8 col-xs-offset-4">
                   		<button id="btn-submit" type="submit" class="btn-u btn-u-red">确定</button>
                    </div>
                </div>
        	</form>
    	</div><!--/ register wrap -->
    </div>
</div>
<!--=== End Content ===-->
<script type="text/javascript">
jQuery(document).ready(function() {
	
	$('#findPwd-form').validate({
		debug: false,
		submitHandler: function(form) {
			common.disabled('#btn-submit');
			form.submit();
		}, 
		errorPlacement: function(error, element) {  
			   error.appendTo(element.parent().parent());  
			   if($(element).attr('name')=='mobPhone'&&!whetherInvalid(element)&&$('#btn-smscode').text()=='获取短信验证码'){
				   $('#btn-smscode').attr({disabled: false});
				   $('#btn-smscode').removeClass("disabled");
			   }else{
				   common.disabled('#btn-smscode');
			   }
		},
		rules: {
			password: {
				required: true, 
				rangelength:[6,20]
				}, 
			checkPassword: {
				required: true, 
				equalTo: '#password'
			}, 
			mobPhone: {
				required: true,
				digits:true,
				minlength:11,
				remote: '${ctx}/common/checkMobPhoneRegist'
			},
			smscode: {
				required: true,
				digits:true,
				minlength:6,
				remote: {
					url: '${ctx}/common/checkSmscode',
					type:'post',
					data: {
						mobPhone: function() {
							return $( "#mobPhone" ).val();
						},
						smscode: function() {
							return $( "#smscode" ).val();
						}
					}
				 }
			},
		},
		messages: {
			password :{
				required: '请您输入密码！',
				rangelength : '密码应为6~20位字符！'
			},
			checkPassword: {
				required: '请您输入此前填写的密码！', 
				equalTo: '与此前填写的密码不一致！'
			},
			smscode: {
				remote: '短信验证码不正确！'
			},
			mobPhone: {
				remote: '此手机号码未注册！'
			},
		}
	});	
	//绑定短信验证码组件
	app.bindSmsCode('btn-smscode', 'mobPhone');
});

function whetherInvalid(element){
	var validate= $(element).parent().parent().attr('class').split(' ');
	for(var i=0;i<validate.length;i++){
		if(validate[i]=='has-error'){
			return true;
		}
	}
	return false;
}
</script>
</body>
</html>

