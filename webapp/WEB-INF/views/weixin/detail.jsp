<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>${media.name}-微信频道-采媒在线</title>
</head>

<body>

<div class="breadcrumbs">
	<div class="container">
    	<div class="row">
        <ul class="breadcrumb">
        	<li>当前位置：</li>
        	<li><a href="${ctx}/">首页</a></li>
        	<li><a href="${ctx}/weixin">微信频道</a></li>
        	<li>${media.name }</li>
        </ul>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/weixin/detail-content.jsp" flush="true" >
	<jsp:param name="prefix" value="weixin"/>
</jsp:include>

</body>
</html>

