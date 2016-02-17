<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>媒体导入</title>
</head>

<body>
<div class="modal-header">
	<h4 class="modal-title">导入结果</h4>
</div>
<ul class="list-group">
    <c:forEach items="${result}" var="item">
    <li class="list-group-item">${item }</li>
    </c:forEach>
</ul>
  
<script type="text/javascript">
<!--
$(function() {
    menu.active('#media-import');
    
});
//-->
</script>

</body>
</html>
