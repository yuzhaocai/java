<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<!-- Meta -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<!-- Favicon -->
<link type="image/x-icon" href="${ctx}/static/assets/img/favicon.ico" rel="shortcut icon">

<title><decorator:title default="帮助中心"/></title>

<%@include file="/WEB-INF/jspf/jslib.jsp" %>

<decorator:head />

</head>

<body>
	<%@include file="/WEB-INF/decorators/header.jsp"%>

	
	<div class="container mt20 mb20">
		<div class="row">
			<!-- Sidebar -->
			<div class="left-panel">
			<%@include file="/WEB-INF/decorators/helpCenter/left.jsp"%>
			</div>
			
			<!-- Content -->
			<div class="right-panel"> 
			<decorator:body />
			</div>
		</div>
	</div>

	<%@include file="/WEB-INF/decorators/footer.jsp"%>
	
<script>
	$(document).ready(function() {
		//控制首页成功案例高度
		var floorMain_h=$('.floor.floor-main').height(),
			floorSidebar_panelHeading_h=$('.floor-sidebar .panel-heading').outerHeight();	
		$('.floor-sidebar .panel-body').outerHeight(floorMain_h - floorSidebar_panelHeading_h);
		
	});
</script>
</body>

<tags:errors />

</html>