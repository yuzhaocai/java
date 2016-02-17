<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>账户充值</title>
</head>

<body>
    <!-- Main -->
    <div class="main-panel deposit-panel">
		<div class="headline"><h4>账户充值</h4></div>
        <div class="panel">
        	<div class="panel-body">
            	<form id="charge-form" class="form-horizontal" action="${ctx }/member/account/charge" method="post">
            		<zy:token />
            		
                    <div class="form-group">
                        <label class="control-label col-xs-2">当前账户余额：</label>
                        <div class="col-xs-5">
                            <p class="price"><strong><fmt:formatNumber value="${account.avBalance }" currencyCode="CNY" type="currency" /></strong><small>元</small></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">充值金额：</label>
                        <div class="col-xs-5 has-feedback">
                            <div class="input-group w200">
                                <span class="input-group-addon">￥</span>
                                <input type="text" id="amount" name="amount" class="form-control">
                                <span class="input-group-addon">.00</span>                     
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">充值平台：</label>
		                <div class="col-xs-3">
		                    <a class="dashed-box active" href="member-deposit.html">
		                        <span class="dashed-circle"><i class="fa fa-check"></i></span>
		                        <img src="${ctx}/static/assets/img/pay/alipay.png">
		                        <input type="radio" class="hide" name="platform" value="PAY_P_ALIPAY" checked />
		                    </a>
		                </div>
		                <div class="col-xs-3">
		                    <a class="dashed-box" href="member-deposit-union.html">
		                        <span class="dashed-circle"><i class="fa fa-check"></i></span>
		                        <img src="${ctx}/static/assets/img/pay/wechatpay.png">
                                <input type="radio" class="hide" name="platform" value="PAY_P_WXPAY" />
		                    </a>
		                </div>
		            </div>                    
                    
                    <div class="form-group">
                    	<div class="col-xs-offset-2 col-xs-5 ">
                            <button id="btn-submit-charge" type="submit" class="btn-u btn-u-green w200">充值</button>
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
	
	$(".dashed-box").click(function() {
		$(".dashed-box").removeClass("active");
        $(this).addClass("active");
        $("input", this).attr("checked", true);
        return false;
	});
	
	$('#charge-form').validate({
		debug: false,
		submitHandler: function(form) {
			//common.log('submit');
			common.disabled('#btn-submit-charge');
			form.submit();
			
		}, 
		rules: {
			amount: {
				required:true,
				digits:true,
				min: 5000,
				max: 100000000
			}
		},
		messages: {
			amount:{
				required: '请输入充值金额',
				digits:'只能输入整数',
				min: '充值金额每次最低5000元',
				max: '充值金额每次最高100000000元'
			}
		}
	});
	
});
	
</script>
        
</body>
</html>
