<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" session="false" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta name="decorator" content="printable" />

	<title>500 - 上传文件失败</title>
	<style>
		body {
			padding: 15px;
		}
	</style>
	
	<%@ include file="/WEB-INF/views/error/error-common.jsp" %>
	
</head>

<body>

<h2>上传文件失败. <small><a href="javascript:back()">返回上一页</a></small></h2>
	
<p class="alert alert-error">错误消息：<%=exception.getMessage() %></p>

</body>
</html>
