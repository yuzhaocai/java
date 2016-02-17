<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="data" type="java.lang.String" required="true"%>
<c:if test="${fn:length(data) > 100 }">${fn:substring(data, 0, 100) }<a tabindex="0" data-toggle="popover" data-placement="bottom" data-trigger="focus" title="" data-content="${data }">[详情]</a></c:if>
<c:if test="${fn:length(data) <= 100 }">${data }</c:if>
