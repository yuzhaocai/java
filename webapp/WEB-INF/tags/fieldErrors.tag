<%@ tag pageEncoding="UTF-8" description="显示字段错误消息" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="commandName" type="java.lang.String" required="true" description="命令对象名称" %>

<spring:hasBindErrors name="${commandName}">
	<c:if test="${errors.fieldErrorCount > 0}">
	<div class="alert alert-danger" role="alert">
	<strong>错误消息</strong><br>
		<c:forEach items="${errors.fieldErrors}" var="error">
			<spring:message var="message" code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}" />	
			<c:if test="${not empty message}">* ${message} <br></c:if>
		</c:forEach>
	</div>
	</c:if>
</spring:hasBindErrors>

