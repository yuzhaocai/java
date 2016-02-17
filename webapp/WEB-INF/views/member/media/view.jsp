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
	margin-right: 20px;
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
	<h4 class="modal-title">媒体详情</h4>
</div>
<div class="modal-body">
	<div class="sys-media">
		<div class="bg-grey border p-xs m-b-xs">
			<a class="pull-left"> <img
				src="<zy:fileServerUrl value="${media.showPic }" />"
				class="img-circle media-avatar" />
			</a>
			<div class="media-body m-b-xs">
				<c:if test="${media.mediaType eq 'MEDIA_T_WEIXIN'}">
					<div class="row">
						<div class="col-md-6">
							<span class="text-muted">微信号：</span>${media.account }
						</div>
					</div>
				</c:if>
				<div class="row">
					<div class="col-md-6">
						<span class="text-muted">媒体名称：</span>${media.name }
					</div>
					<div class="col-md-6">
						<span class="text-muted">认证类别：</span>
						<zy:dic value="${media.category }" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<span class="text-muted">行业类型：</span>
						<c:forEach items="${fn:split(media.industryType, ',')}" var="item">
							<zy:dic value="${item }" />
						</c:forEach>
					</div>
					<div class="col-md-6">
						<span class="text-muted">地区：</span>
						<c:forEach items="${fn:split(media.region, ',')}" var="item">
							<zy:dic value="${item }" />
						</c:forEach>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<span class="text-muted">粉丝数：</span>${media.fans }
					</div>
					<c:if test="${media.fansDir ne null}">
					<div class="col-md-6">
						<span class="text-muted">粉丝方向：</span>
						<c:forEach items="${fn:split(media.fansDir, ',')}" var="item">
							<zy:dic value="${item }" />
						</c:forEach>
					</div>
					</c:if>
				</div>
				<div class="row">
					<div class="col-lg-6">
						<span class="text-muted">标签：</span>
                        ${media.tags}
					</div>
					<div class="col-md-6">
						<span class="text-muted">适用产品：</span>
						<c:forEach items="${fn:split(media.products, ',')}" var="item">
							<zy:dic value="${item }" />
						</c:forEach>
					</div>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-lg-12">
				<div class="border-dotted p-xs m-b-xs">
					<strong>媒体简介</strong>
					<div class="row">
						<div class="col-lg-12"><p class="pre">${media.description }</p></div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<div class="border-dotted p-xs m-b-xs">
					<strong>服务及报价</strong>
					<c:if test="${fn:length(media.mediaQuotes) lt 1 }">
						<div class="row">
							<div class="col-md-6">该媒体暂无报价！</div>
						</div>
					</c:if>
					<c:forEach items="${media.mediaQuotes }" var="item">
						<div class="row">
							<div class="col-lg-6">
								<zy:dic value="${item.type }" />
							</div>
							<div class="col-lg-6">
								<span class="text-muted">价格：</span>${item.price }
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<div class="border-dotted p-xs m-b-xs">
					<strong>案例</strong>
					<c:if test="${fn:length(media.mediaCases) lt 1 }">
						<div class="row">
							<div class="col-md-6">该媒体暂无案例！</div>
						</div>
					</c:if>
					<c:forEach items="${media.mediaCases }" var="item">
						<div class="row">
							<div class="col-md-6">
								<span class="text-muted">案例标题：</span>${item.title }
							</div>
							<div class="col-md-6">
								<span class="text-muted">案例亮点：</span>${item.light }
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<span class="text-muted">案例正文：</span><br><p class="pre">${item.content }</p>
							</div>
						</div>
						<hr class="hr-xs">
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>
