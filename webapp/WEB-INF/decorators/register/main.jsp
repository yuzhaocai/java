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
<meta name="keywords" content="网络广告投放,微信频道,微博频道,广告发布,新媒体营销">
<meta name="description" content="采媒在线是中国大型广告在线交易平台，提供广告交易、新闻素材（版权）交易、舆情监控三大服务，涵盖平面媒体、网络媒体、电视媒体、户外媒体以及传统媒体的新媒体。">
<!-- Favicon -->
<link type="image/x-icon" href="${ctx}/static/assets/img/favicon.ico" rel="shortcut icon">

<title>中国在线广告交易中心<%-- <decorator:title/>--%></title>

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

//
var winh = $(window).height();
doch = $(document).height();
if(doch<=winh){
	$(".footer-v1").css({
		'position':'fixed',
		'bottom':'0',
		'width':'100%'
		
		});
}
else {
	$(".footer-v1").css({
		'position':'relative'			
		});			
}

</script>

</head>
<body>

	<%@include file="/WEB-INF/decorators/register/header.jsp"%>
	<div id="content">
	<!-- Content -->
	<decorator:body />
	</div>

	<%@include file="/WEB-INF/decorators/footer.jsp"%>
	
</body>

</html>