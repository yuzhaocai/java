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
<meta name="keywords" content="网络广告营销,网络广告投放,新媒体,广告投放平台">
<meta name="description" content="采媒在线广告投放平台为各行各业(包括时事新闻,财经理财,IT科技,旅游休闲等)广告主提供大量需求,主要利用新媒体的网络广告营销方式为广告主提供一系列新媒体营销方案,根据企业需求制定互联网广告投放策略。">
<!-- Favicon -->
<link type="image/x-icon" href="${ctx}/static/assets/img/favicon.ico" rel="shortcut icon">

<title>采媒在线广告投放平台,网络广告营销,新媒体营销_采媒在线<%-- <decorator:title/>--%></title>

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