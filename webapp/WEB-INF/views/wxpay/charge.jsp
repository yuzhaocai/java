<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>会员充值</title>
</head>

<body>
    <div class="main-panel">
        <div class="headline"><h5>微信支付</h5></div>
            <div class="panel pad">
                <div class="panel-body">
	                <div class="btn-group h5 pull-right highlight-blue">
	                  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	                    使用其他支付方式 <span class="caret"></span>
	                  </button>
	                  <ul class="dropdown-menu">
	                    <li><a href="${ctx }/member/account/charge/alipay/${chargeLog.id}">支付宝</a></li>
	                  </ul>
	                </div>
                    <p>充值金额 <strong class="highlight">${chargeLog.amount }</strong> 元</p>                
                    <div class="row">
                        <div class="col-md-6">
                            <div class="qr">
                                <div class="qr-box"><img src="${ctx}/wxpay/qrcode/${chargeLog.id}"></div>
                                <div class="qr-info">
                                    <div class="row">
                                        <div class="col-xs-4 text-right"><img src="${ctx}/static/assets/img/pay/qrcode.png"></div>
                                        <div class="col-xs-8"><b>请使用微信扫一扫<br>扫描二维码支付</b></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6"><img src="${ctx}/static/assets/img/pay/phone-bg.png" width="329" height="421"></div>
                    </div>
                    
                </div><!--// .panel-body-->
            </div><!--// .panel-->
                
        </div>

<script type="text/javascript">
$(function() {
    menu.active('#my-account');
});
	
</script>
        
</body>
</html>
