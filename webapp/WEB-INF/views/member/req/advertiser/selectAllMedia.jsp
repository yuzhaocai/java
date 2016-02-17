<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<%-- 选定媒体 --%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">接单媒体</h4>
</div>
<div class="modal-body">
	<table width="100%" border="0" class="table table-bordered">
	  <tr>
	    <th scope="col">邀请方式</th>
	    <th scope="col">大类</th>
	    <th scope="col">媒体名称</th>
	    <th scope="col">认证类型</th>
	    <th scope="col">是否改稿</th>
	    <th scope="col">粉丝数</th>
	    <th scope="col">价格</th>
	    <th scope="col">应邀状态</th>
	    <th scope="col">操作</th>
	  </tr>
	  <c:forEach items="${reqMedias }" var="item">
	  <tr>
	    <td><zy:dic value="${item.inviteType }"/></td>
	    <td><zy:dic value="${item.media.mediaType }"/></td>
	    <td><zy:dic value="${item.media.name }"/></td>
	    <td><zy:dic value="${item.media.category }"/></td>
	   	<c:choose>
	   		<c:when test="${item.changed }">
						<td class="hightlight">是&nbsp; <small> <a class=""
								href="${ctx }/member/req/download/article/${item.requirement.article}">下载</a>
						</small>
						</td>
					</c:when>
	   		<c:otherwise><td>否</td></c:otherwise>
	   	</c:choose>
	    <td>${item.media.fans }</td>
	    <td><span class="color-orange">${item.price }</span></td>
	    <!-- 应邀状态(未反馈、拒绝、接受) -->
   	    <c:choose>
	    	<c:when test="${item.fbStatus eq 'MEDIA_FB_NULL'}">
			   <td><span class="color-orange">未反馈</span></td>
		    </c:when>
   	    	<c:when test="${item.fbStatus eq 'MEDIA_FB_ACCEPT'}">
			   <td><span class="color-green">同意</span></td>
		    </c:when>
		    <c:when test="${item.fbStatus eq 'MEDIA_FB_REFUSE'}">
			   <td><span class="color-red">拒绝</span></td>
		    </c:when>
	    </c:choose>
	    <c:choose>
	        <c:when test="${item.fbStatus eq 'MEDIA_FB_NULL'}">
			    <td>
				    <div class="" role="group">
				    			<button type="button" class="btn-u btn-u-xs btn-u-dark deleteMedia" data-id="${item.id }">删除媒体</button>
				    </div>
			    </td>
		    </c:when>
		    <c:otherwise>
	   		    <td>
				    <div class="" role="group"></div>
			    </td>
		    </c:otherwise>
	    </c:choose>
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
			<span>需求名：${req.name }</span>
		</div>
	</div>
</div>
