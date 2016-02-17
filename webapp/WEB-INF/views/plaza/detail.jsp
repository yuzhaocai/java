<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<c:choose>
<c:when  test="${!empty req }">

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">${req.name }</h4>
</div>
<div class="modal-body">
	<div class="tag-box tag-box-v6 service-block-default">
		<div class="row line-height-sm">
			<div class="col-xs-6">
				<span class="text-muted">标题：</span>${req.name }
			</div>
			<div class="col-xs-6">
				<span class="text-muted">客户名称：</span>${req.customer.name }
			</div>
			<div class="col-xs-6">
				<span class="text-muted">大类：</span>
				<zy:dic value="${req.mediaTypes }" />
			</div>
			<div class="col-xs-6">
				<span class="text-muted">预算：</span>
				<span class="color-orange"><fmt:formatNumber value="${req.budget }" currencyCode="CNY" type="currency" /></span> 元
			</div>
			<div class="col-xs-6">
				<span class="text-muted">发布时间：</span>
				<fmt:formatDate value="${req.startTime }" pattern="yyyy-MM-dd" />
			</div>
			<div class="col-xs-6">
				<span class="text-muted">至：</span>
				<fmt:formatDate value="${req.endTime }" pattern="yyyy-MM-dd" />
			</div>
			<div class="col-xs-6">
				<span class="text-muted">服务类型：</span><zy:dic value="${req.serviceTypes}"/> </div>
			<div class="col-xs-6">
				<span class="text-muted">行业：</span><zy:dic value="${req.industryTypes }"/></div>
			<div class="col-xs-6">
				<span class="text-muted">改稿：</span>
				<c:choose>
					<c:when test="${req.allowChange }">允许</c:when>
					<c:otherwise>不允许</c:otherwise>
				</c:choose>
			</div>
			<div class="col-xs-6">
				<span class="text-muted">素材：</span><a href="${ctx}/member/req/download/article/${req.article }" target="_blank">下载</a>
			</div>
		</div>

	</div>
	<div class="tag-box tag-box-v4">
		<div class="row">
			<div class="col-lg-12">
				<h4 class="text-center">需求概述</h4>
				<hr class="hr-xs">
				<div class="row">
					<div class="col-lg-12"><p class="pre">${req.summary }</p></div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal-footer">
<shiro:hasRole name="provider">
	<span style="display:none"><fmt:formatDate value="${req.deadline }" pattern="yyyy/MM/dd" /></span>
	<button type="button" class="btn-u btn-u-red btn-enlist" data-id="${req.id }" data-dismiss="modal">抢单</button>
</shiro:hasRole>
</div>

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

