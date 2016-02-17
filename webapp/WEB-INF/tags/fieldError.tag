<%@ tag pageEncoding="UTF-8" description="显示字段错误消息" body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="commandName" type="java.lang.String" required="true" description="命令对象名称" %>
<%@ attribute name="field"       type="java.lang.String" required="true" description="字段名称" %>

<spring:hasBindErrors name="${commandName}">
	<c:if test="${errors.fieldErrorCount > 0}">
		<c:forEach items="${errors.fieldErrors}" var="error">
			<c:if test="${error.field eq field }">
				<spring:message var="message" code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}" />
				<%
				System.out.println("==========>> message = " + request.getAttribute("message"));
				%>
				<c:if test="${!empty message}">
					<p class="help-block"><small>${message}</small></p>
				</c:if>
			</c:if>
		</c:forEach>
	</c:if>
</spring:hasBindErrors>