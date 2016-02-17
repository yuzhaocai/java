<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>创建需求</title>
    <style type="text/css">
    	.multiple.multiple-P45 .list-inline li {
    		width:45%;
    	}
    	.main-panel .table th.text-left {
    		text-align: left;
    	}
    	.media-group h5 {
    		color: #E74C3C;
    	}
    </style>    
</head>

<body>

<jsp:include page="/WEB-INF/views/member/req/advertiser/create/recommend-common-field.jspf" flush="true">
	<jsp:param name="mediaTypeName" value="微信"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/member/req/advertiser/create/recommend-common-script.jspf" flush="true">
	<jsp:param name="mediaTypeCode"     value="Weixin"/>
</jsp:include>

</body>
</html>