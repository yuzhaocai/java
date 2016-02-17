<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
<%-- 
	<meta name="decorator" content="plain">
--%>
    <title>创建需求</title>
    
</head>

<body>
        
  <jsp:include page="/WEB-INF/views/member/req/advertiser/create/select-common-field.jspf">
  	<jsp:param value="微信" name="mediaTypeName"/>
  </jsp:include>
  
  <jsp:include page="/WEB-INF/views/member/req/advertiser/create/select-common-script.jspf" >
	<jsp:param name="prefix" value="weixin"/>
  </jsp:include>
</body>
</html>
