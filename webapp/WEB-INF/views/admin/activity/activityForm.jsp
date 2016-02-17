<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>活动折扣</title>
<style type="text/css">
.left {
	padding-left: 15px !important;
}
</style>
</head>
<body>
	<jsp:include page="select-common-field.jspf">
		<jsp:param value="微博" name="mediaTypeName" />
	</jsp:include>

	<jsp:include page="select-common-script.jspf">
		<jsp:param name="prefix" value="weibo" />
	</jsp:include>

	<script type="text/javascript">
		$(function() {
			menu.active('#audit-activity');
			queryMedias(1);
		});
	</script>
</body>
</html>
