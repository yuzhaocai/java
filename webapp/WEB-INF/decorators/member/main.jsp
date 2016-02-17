<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="renderer" content="webkit" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<!-- Favicon -->
<link type="image/x-icon" href="${ctx}/static/assets/img/favicon.ico" rel="shortcut icon">

<title>采媒在线-<decorator:title default="会员中心"/></title>

<%@include file="/WEB-INF/jspf/jslib.jsp" %>

<style type="text/css" >
.popover-cart {
	display: block;
	top: 41px;
	right: 0;
	max-width: 375px;
}

.popover-cart.bottom .arrow {
	left: 90%
}

.badge.info {
    background-color: #EC971F;
    color: #ff0000;
}
.user-info .info-lcol {
    width: 500px;
}
</style>

<decorator:head />

</head>

<body class="c-body">
	
	<c:set var="headerTitle" value="采媒在线 - 广告投放平台" />
	
	<%@include file="/WEB-INF/decorators/common/header.jsp"%>

	
	<div id="content" class="container-fluid">
		<div class="row">
			<!-- Sidebar -->
			<div class="col-md-2">
			<%@include file="/WEB-INF/decorators/member/left.jsp"%>
			</div>
			
			<!-- Content -->
			<div class="col-md-10">
			<decorator:body />
			</div>
		</div>
	</div>

	<%@include file="/WEB-INF/decorators/common/footer.jsp"%>
	
 
</body>

<tags:errors />

</html>