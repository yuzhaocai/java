<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<style type="text/css">
.navbar-brand {
	width: 500px;
	line-height: 70px;
	padding-left: 170px;
	color: #fff;
}

.breadcrumb {
	margin: 0;
	padding: 0;
	background-color: transparent;
}

/*以下是新增样式*/
.sys-media .pull-left {
	margin-right: 10px;
}

.sys-media .media-avatar {
	width: 100px;
	height: 100px;
}

.sys-media .media-body {
	display: block;
	width: auto;
}

.sys-media .media-body .row .col- ^* {
	padding: 5px 0;
}

.border {
	border: 1px solid #ddd;
}

.border-dotted {
	border: 1px dotted #ddd;
}

.p-xs {
	padding: 10px;
}

.m-b-xs {
	margin-bottom: 10px;
}

.bg-grey {
	background-color: #eee;
}
</style>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">需求详情</h4>
</div>
<div class="modal-body">
	<div class="sys-media">
		<div class="bg-grey border p-xs m-b-xs">
			<div class="media-body m-b-xs">
				<div class="row">
					<div class="col-md-6">
						<span class="text-muted">标题：</span>${req.name }
					</div>
					<div class="col-md-6">
						<span class="text-muted">媒体类别：</span>
						<zy:dic value="${req.mediaTypes }" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<span class="text-muted">发布时间：</span>
						<fmt:formatDate value="${req.startTime }" pattern="yyyy/MM/dd" />
					</div>
					<div class="col-md-6">
						<span class="text-muted">至：</span>
						<fmt:formatDate value="${req.endTime}" pattern="yyyy/MM/dd" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<span class="text-muted">最迟响应时间：</span>
						<fmt:formatDate value="${req.deadline}" pattern="yyyy/MM/dd" />
					</div>
					<div class="col-md-6">
						<span class="text-muted">投放地域：</span>
						<c:forEach items="${fn:split(req.regions, ',')}" var="item">
							<zy:dic value="${item }" />
						</c:forEach>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<span class="text-muted">服务类别：</span>
						<c:forEach items="${fn:split(req.serviceTypes , ',')}" var="item">
							<zy:dic value="${item }" />
						</c:forEach>
					</div>
					<div class="col-md-6">
						<span class="text-muted">投放行业：</span>
						<c:forEach items="${fn:split(req.industryTypes , ',')}" var="item">
							<zy:dic value="${item }" />
						</c:forEach>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<span class="text-muted">项目预算：</span>${req.budget } 元
					</div>
					<div class="col-md-6">
						<span class="text-muted">拟邀媒体数量：</span>${fn:substring(req.inviteNum, 7, 12)} 家
					</div>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-lg-12">
				<div class="border-dotted p-xs m-b-xs">
					<strong>需求描述-<a class=""
						href="${ctx }/member/req/download/article/${req.article}">稿件下载</a></strong>
					<div class="row">
						<div class="col-lg-12"><p class="pre">${req.summary }</p></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
