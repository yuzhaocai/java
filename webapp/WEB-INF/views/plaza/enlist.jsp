<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<c:choose>
<c:when  test="${!empty req }">
<form id="enlist-form" class="form-inline">
<input type="hidden" name="rid" value="${req.id }">

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">抢单 - ${req.name }</h4>
</div>
<div class="modal-body">
	<table width="100%" class="table">
	  <tr>
	    <th scope="col">类型</th>
	    <th scope="col">媒体</th>
	    <th scope="col">服务名称</th>
	    <th scope="col" class="text-right">报价</th>
	    <th scope="col"></th>
	  </tr>
	  <c:forEach items="${quotes }" var="item">
	  <tr>
	    <td><zy:dic value="${item.media.mediaType }"/></td>
	    <td>${item.media.name }</td>
	    <td><zy:dic value="${item.type }"/></td>
	    <td class="text-right">
	    	<c:choose>
	    		<c:when test="${item.media.enlisted }">
	    			<c:if test="${item.enlisted }">已抢单</c:if>
	    		</c:when>
	    		<c:otherwise>
	    			<div class="form-group">
		    			<input type="hidden" class="quote-field" name="type" value="${item.type }" disabled>
		    			<input type="hidden" class="quote-field" name="price" value="${item.price }"  disabled>
		    			<input type="hidden" class="quote-field " name="priceMedia" value="${item.priceMedia }" disabled />
		    			${item.priceMedia }
	    			</div>
			    	<div class="checkbox">
			    		<label>
			    			<input type="checkbox" class="chk-enlist ${item.media.id }" name="mid" value="${item.media.id }" > 抢单
			    		</label>
			    	</div>
	    		</c:otherwise>
	    	</c:choose>
	    </td>
	  </tr>
	  </c:forEach>
	  
	  <c:if test="${empty quotes }">
	  <tr>
	    <td colspan="5">您目前不能抢单，请先到会员中心“创建媒体”并创建“服务报价”。</td>
	  </tr>
	  </c:if>
	  
	</table>
</div>
<div class="modal-footer">
<c:choose>
	<c:when test="${!empty quotes && unAllCheck}"><button type="submit" class="btn-u btn-u-red" data-id="${req.id }" >确定抢单</button></c:when>
	<c:otherwise><button type="button" class="btn-u btn-u-default" data-dismiss="modal">关 闭</button></c:otherwise>
</c:choose>
</div>
</form>
</c:when>

<c:otherwise>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">非法访问！</h4>
</div>
<div class="modal-body">
</div>
<div class="modal-footer">
	<button type="button" class="btn-u btn-u-default" data-dismiss="modal">关 闭</button>
</div>
</c:otherwise>

</c:choose>

