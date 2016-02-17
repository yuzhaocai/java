<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<%-- 选定媒体 --%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close" id="sel-media-close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">选择接单媒体</h4>
</div>
<div class="modal-body">
	<table width="100%" border="0" class="table table-bordered">
	  <tr>
	    <th scope="col">报名方式</th>
	    <th scope="col">媒体名称</th>
	    <th scope="col">大类</th>
	    <th scope="col">认证类型</th>
	    <th scope="col">是否改稿</th>
	    <th scope="col">粉丝数</th>
	    <th scope="col">案例</th>
	    <th scope="col">简介</th>
	    <th scope="col">价格</th>
	    <th scope="col">操作</th>
	  </tr>
	  <c:forEach items="${reqMedias }" var="item">
	  <tr>
	    <td><zy:dic value="${item.inviteType }"/></td>
	    <td><a data-id="${item.media.id }"  class="preview-media" href="#">${item.media.name }</a></td>
	    <td><zy:dic value="${item.media.mediaType }"/></td>
	    <td><zy:dic value="${item.media.category }"/></td>
	   	<c:choose>
	   		<c:when test="${item.changed }">
	   		    <td class="hightlight">是&nbsp;
			    	<a href="javascript:void(0)" class="chakan"
			    		data-id="${item.id }" data-media-name="${item.media.name }" ><small>查看</small>
			    	</a>
			    </td>
	   		</c:when>
	   		<c:otherwise><td>否</td></c:otherwise>
	   	</c:choose>
	    <td>${item.media.fans }</td>
	    <td>${fn:length(item.media.mediaCases) } 个</td>
	    <td><zy:out value="${item.media.description }" len="16"/> <a href="#" class="preview-media" data-id="${item.media.id }">[详细]</a></td>
	    <td><span class="color-orange">${item.price }</span></td>
	    <td>
	    <div class="" role="group">
	    	<button type="button" class="btn-u btn-u-xs btn-u-dark accept"
	    		data-id="${item.id }"
	    		data-invite-type="${item.inviteType }">生成订单</button>
	    	<button type="button" class="btn-u btn-u-xs btn-u-dark refuse"
	    		data-id="${item.id }" 
	    		data-invite-type="${item.inviteType }"
	    		data-changed="${item.changed }" >拒绝</button>
	    </div>
	    </td>
	  </tr>
	  </c:forEach>
	  <c:if test="${empty reqMedias }">
	  <tr>
	    <td colspan="9">当前没有可选的媒体</td>
	  </tr>
	  </c:if>
	</table>
</div><!-- /modal-body -->

<div class="modal-footer">
	<div class="row">
		<div class="col-xs-12 text-left">
			<span>编 号：<span id="modal-req-id">${req.id }</span></span>
			<br> 
			需求名：${req.name }
		</div>
	</div>
</div>
<!-- 媒体详情 -->
<div class="modal fade" id="media-view" tabindex="-1" role="dialog" data-width="900" data-replace="true">
   </div>
<script type="text/javascript">
	common.showMediaNow(".preview-media", $('#select-media'));
	$('#sel-media-close').on('click', function () {
		window.location.reload();
		});
</script>
