<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>会员中心</title>
    
</head>

<body>
        
    <!-- Main -->
    <div class="main-panel deposit-panel">

		<div class="headline"><h4>提现</h4></div>
        
        <div class="panel">
        	<div class="panel-body">
            	<form id="withdraw-form" class="form-horizontal" action="${ctx }/member/account/withdraw" method="post">
            		<zy:token />
                    <input type="hidden" name="platform" value="PAY_P_ALIPAY" class="form-control" >
                    <input type="hidden" id="mobPhone" value="${account.customer.mobPhone }" class="form-control" >
            		
                    <div class="form-group">
                        <label class="control-label col-xs-2">当前账户余额：</label>
                        <div class="col-xs-5">
                            <p class="price"><strong><fmt:formatNumber value="${account.avBalance }" currencyCode="CNY" type="currency" /></strong><small>元</small></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">提现金额：</label>
                        <div class="col-xs-5 has-feedback">
                            <div class="input-group w300">
                                <span class="input-group-addon">￥</span>
                                <input type="text" id="amount" name="amount" class="form-control" >
                                <span class="input-group-addon">.00</span>                     
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">提现账号：</label>
                        <div class="col-xs-5 has-feedback">
                            <div class="input-group w300">
                                <span class="input-group-addon">支付宝</span>
                                <input type="text" id="alipayAccount" name="alipayAccount" class="form-control" placeholder="支付宝账号" maxlength="100">               
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
		                <label class="col-xs-2 control-label"><span class="color-red">*</span>验证码:</label>
		                <div class="col-xs-3 has-feedback">
		                    <div class="input-group w300">
		                        <input id="smscode" name="smscode" class="form-control" placeholder="输入短信验证码" 
		                            type="text" maxlength="6" >
		                        <span class="input-group-btn">
		                            <button type="button" class="btn btn-default" id="btn-smscode" disabled="disabled" style="disabled">获取短信验证码</button>
		                        </span>
		                    </div>
		                </div>
		            </div>                    
                    
                    <div class="form-group">
                    	<div class="col-xs-offset-2 col-xs-5 ">
                            <button id="btn-submit-ok" type="submit" class="btn-u btn-u-green w200">确　定</button>
                    	</div>
                    </div>
                </form>
                    
            </div>
        </div>
            
    </div>
    <!-- End Main -->

<script type="text/javascript">
$(function() {
	menu.active('#my-account');
	
	app.bindSmsCode('btn-smscode', 'mobPhone');
	
	$('#withdraw-form').validate({
		debug: false,
		submitHandler: function(form) {
			//common.log('submit');
			common.disabled('#btn-submit-ok');
			form.submit();
			
		},
		errorPlacement: function(error, element) {  
			   error.appendTo(element.parent().parent());  
			   if($(element).attr('name')=='alipayAccount'&&!whetherInvalid(element)&&$('#btn-smscode').text()=='获取短信验证码'){
				   $('#btn-smscode').attr({disabled: false});
				   $('#btn-smscode').removeClass("disabled");
			   }else{
				   common.disabled('#btn-smscode');
			   }
		},
		rules: {
			amount: {
				required:true,
				digits:true,
				min: 100,
				max: '${account.avBalance}'
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
			alipayAccount: {
				required:true,
                minlength:11,
                maxlength:30
			}
		},
		messages: {
			amount:{
				required: '请输入提现金额',
                min: '最少提100元',
				max: '您的余额只有 ${account.avBalance}元'
			},
			alipayAccount: {
				required: '请输入支付宝账号',
				minlength:'请输入11位手机号或邮箱',
                maxlength:'请输入11位手机号或邮箱'
            },
            smscode: {
                required: '请您输入验证码',
                remote: '短信验证码不正确',
                minlength:'请您输入完整的验证码'
			}
			
			
		}
	});
	
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
