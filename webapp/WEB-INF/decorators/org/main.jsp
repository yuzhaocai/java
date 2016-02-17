<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>


<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="renderer" content="webkit" />
<!-- Favicon -->
<link type="image/x-icon" href="${ctx}/static/assets/img/favicon.ico" rel="shortcut icon">

<title>采媒在线-<decorator:title default="监管机构管理后台"/></title>

<%@include file="/WEB-INF/jspf/jslib.jsp" %>
	
<decorator:head />

</head>

<body class="c-body">

	<c:set var="headerTitle" value="采媒在线--监管机构管理平台" />
	
	<%@include file="/WEB-INF/decorators/common/header.jsp"%>

	<br/>
	
	<div id="content" class="container-fluid">
		<div class="row">
			<!-- Sidebar -->
			<div class="col-md-2">
			<%@include file="/WEB-INF/decorators/org/left.jsp"%>
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