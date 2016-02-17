<%@ page contentType="text/html;charset=UTF-8" session="false" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="printable" />

  <title>禁止访问（403） - 用户权限不足</title>
  <style>
    body {
      padding: 15px;
    }
  </style>	
  <%@ include file="/WEB-INF/views/error/error-common.jsp" %>
</head>

<body>
	<div style="min-height:400px;">
		<h2>禁止访问 - 用户权限不足.</h2>
		<p><a href="${ctx}/">返回上一页</a></p>
	</div>
</body>
</html>
