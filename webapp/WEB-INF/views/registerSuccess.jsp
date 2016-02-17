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
        	<h3>会员注册</h3>
        </div>
        <div class="register-wrap">   
	        <img src="${ctx}/static/assets/img/form/register-title-end.jpg" width="1040" height="40" class="margin-bottom-60">
            <div class="row">
                <div class="col-xs-4 col-xs-offset-4">
                <h3><i class="fa fa-check-circle color-green" style="font-size:30px; vertical-align:sub"></i>会员注册成功！</h3>
                <br><br><br>
                <button type="button" onClick="location='${ctx }/'" class="btn-u btn-u-lg btn-u-red rounded w200">返回首页</button>
                <br><br>
                <button type="button" onClick="location='${ctx }/member'" class="btn-u btn-u-lg btn-u-red rounded w200">进入用户中心</button>
                </div>
            </div>
    	</div><!--/ register wrap -->
    </div>
</div>
<!--=== End Content ===-->

</body>
</html>
