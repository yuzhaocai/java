<%@page import="com.lczy.media.util.UserContext"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html>
<head>

<!-- Meta -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="keywords" content="<c:if test="${media.name != null}">${media.name},</c:if>微博频道,微博推广,微博营销的推广技巧,微博营销技巧">
<meta name="description" content="采媒在线微博营销是国内大型的微博营销平台.拥有大量机构认证（蓝V）,个人认证（黄V）及个人未认证的丰富微博资源.以专业且独特的微博营销的推广技巧为各大企业提供完善的微博营销方案。">
<!-- Favicon -->
<link type="image/x-icon" href="${ctx}/static/assets/img/favicon.ico" rel="shortcut icon">


<c:choose>
	<c:when test="${media.name != null}">
		<title>${media.name}-微博频道-采媒在线</title>
	</c:when>
	<c:otherwise>
		<title>采媒在线微博营销,微博推广,微博营销的推广技巧_采媒在线<%-- <decorator:title/>--%></title>
	</c:otherwise>
</c:choose>


<%@include file="/WEB-INF/jspf/jslib.jsp" %>

<decorator:head />

<!-- 百度统计 -->
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?7a49f701d11a4b4488fea2cf9d2e9656";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>

</head>
<body>

	<%@include file="/WEB-INF/decorators/header.jsp"%>
	
	<!-- Content -->
	<decorator:body />

	<%@include file="/WEB-INF/decorators/footer.jsp"%>
	
</body>

</html>