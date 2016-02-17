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
<meta name="keywords" content="<c:if test= "${media.name != null}">${media.name},<zy:dic value="${media.category }"/>,<zy:dic value="${media.category }"/>推广,<zy:dic value="${media.category }"/>营销,</c:if>更多媒体,更多媒体营销,更多媒体成功案例">
<meta name="description" content="采媒在线微信营销是国内大型的微信营销平台.为各个企业微信公众号及个人微信号提供最专业的微信营销方案,并且拥有最权威的微信营销专家以及最广泛的微信营销思路.采媒在线以丰富的媒体资源为各个企业及个人不断提供微信营销成功案例。">
<!-- Favicon -->
<link type="image/x-icon" href="${ctx}/static/assets/img/favicon.ico" rel="shortcut icon">


<c:choose>
	<c:when test="${media.name != null && media.category != null}">
		<title>${media.name}-<zy:dic value="${media.category }"/>-采媒在线</title>
	</c:when>
	<c:otherwise>
		<title>采媒在线更多媒体营销_采媒在线<%-- <decorator:title/>--%></title>
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