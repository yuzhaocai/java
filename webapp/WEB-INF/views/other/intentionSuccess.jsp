<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>会员注册-采媒在线</title>
</head>

<body style="background-color:#f7f7f7;">

<!--=== Content ===-->
<div class="container">
	<div class="form-page">
		<div class="headline">
        	<h3>提交订单</h3>
        </div>
        <div class="register-wrap">   
            <div class="row">
                <div class="col-xs-4 col-xs-offset-4">
                <h3><i class="fa fa-check-circle color-green" style="font-size:30px; vertical-align:sub"></i>提交订单成功！</h3>
                <br><br><br>
                <button type="button" onClick="location='${ctx }/'" class="btn-u btn-u-lg btn-u-red rounded w200">返回首页</button>
                <br><br>
                <button type="button" onClick="location='${ctx }/other'" class="btn-u btn-u-lg btn-u-red rounded w200">返回更多媒体</button>
                </div>
            </div>
    	</div><!--/ register wrap -->
    </div>
</div>
<!--=== End Content ===-->

</body>
</html>
