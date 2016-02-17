<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<%-- 下属媒体 --%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">${orgName }</h4>
</div>
<div class="modal-body">
	<table width="100%" border="0" class="table table-bordered">
	  <tr>
	    <th scope="col">大类</th>
	    <th scope="col">媒体名称</th>
	    <th scope="col">认证类型</th>
	    <th scope="col">粉丝数</th>
	  </tr>
	  <c:forEach items="${subMedia }" var="item">
	  <tr>
	    <td><zy:dic value="${item.mediaType }"/></td>
	    <td><zy:dic value="${item.name }"/></td>
	    <td><zy:dic value="${item.category }"/></td>
	    <td>${item.fans }</td>
	  </tr>
	  </c:forEach>
	  <c:if test="${empty subMedia }">
	  <tr>
	    <td colspan="9">当前机构无下属媒体</td>
	  </tr>
	  </c:if>
	</table>
</div><!-- /modal-body -->

