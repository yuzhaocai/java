<%@ tag pageEncoding="UTF-8" description="输出 radio/checkbox 的选中状态" body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="field" type="java.lang.String" required="true" rtexprvalue="true"  %>
<%@ attribute name="value" type="java.lang.String" required="true" rtexprvalue="true"  %>

<c:choose>
	<c:when test="${field eq value }">checked="checked"</c:when>
	<c:otherwise></c:otherwise>
</c:choose>