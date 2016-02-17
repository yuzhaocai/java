<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<!doctype html>
<html>
<head>
<%-- 
	<meta name="decorator" content="plain">
--%>
<title>查看需求</title>
</head>

<body>

	<!-- Main -->
	<div class="main-panel">
		<div class="headline">
			<h5>查看需求：${req.id }</h5>
			<button type="button" class="btn-u btn-u-sm btn-u-dark pull-right"
				onclick="location.replace(document.referrer);">
				<i class="fa fa-angle-left"></i> 返回
			</button>
		</div>
		<!-- mTab v2 -->
		<div class="pad pd15">

			<div class="steps form-horizontal">
				<div class="steps-panel">
					<div class="col-sm-12 subtt">
						已选择 <span class="mediaCount highlight" data-req-id="${req.id }">${reqChooseMediaNum }</span>
						家媒体 <a href="#" data-toggle="modal" data-target=".selected-media"
							data-req-id="${req.id }" class="queryMediaCount btn btn-link">查看</a>
					</div>

					<div class="row">
						<div class="col-sm-9 col-xs-offset-1">
							<fieldset>
								<div class="form-group">
									<label class="control-label col-sm-2">项目预算:</label>
									<div class="col-sm-4">
										<p class="form-control-static">${req.budget }元</p>
									</div>
									<label class="control-label col-sm-2">拟邀媒体数:</label>
									<div class="col-sm-4">
										<p class="form-control-static">
											<zy:dic value="${req.inviteNum }" />
										</p>
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-sm-2">媒体类别:</label>
									<div class="col-sm-4">
										<p class="form-control-static">
											<zy:dic value="${req.mediaTypes }" />
										</p>
									</div>

									<label class="control-label col-sm-2">投放地域:</label>
									<div class="col-sm-4">
										<p class="form-control-static">
											<zy:area id="${req.regions }" />
										</p>
									</div>
								</div>

								<div class="form-group">
									<c:if test="${not empty req.serviceTypes }">
										<label class="control-label col-sm-2">服务类别:</label>
										<div class="col-sm-4 has-feedback">
											<p class="form-control-static">
												<zy:dic value="${req.serviceTypes }" />
											</p>
										</div>
									</c:if>

									<label class="control-label col-sm-2">投放行业:</label>
									<div class="col-sm-4 has-feedback">
										<p class="form-control-static">
											<zy:dic value="${req.industryTypes }" />
										</p>
									</div>
								</div>
							</fieldset>

							<fieldset>
								<div class="form-group">
									<label class="control-label col-sm-2">标题:</label>
									<div class="col-sm-4 has-feedback">
										<input type="text" class="form-control" name="name"
											value="${req.name }" readonly="readonly">
									</div>
									<label class="control-label col-sm-2">预算:</label>
									<div class="col-sm-4 has-feedback">
										<input type="text" class="form-control" name="articlePrice"
											value="${req.articlePrice }" readonly="readonly">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-sm-2">需求概述:</label>
									<div class="col-sm-10 has-feedback form-control-static">
										<%-- 				                	<textarea class="form-control" rows="5" name="summary">${req.summary }</textarea> --%>
										<%-- 				                	<zy:out value="${req.summary }" escapeXml="false" ellipsis="false" br="true" /> --%>
										<p class="pre">${req.summary }</p>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-sm-2">素材:</label>
									<div class="col-sm-4 has-feedback radio-inline">
										<c:if test="${!empty req.articleMatter }">
											<a
												href="${ctx}/member/req/download/article/${req.articleMatter}"
												target="_blank" class="btn-u btn-u-xs btn-u-dark">下载稿件</a>
										</c:if>
									</div>
									<c:if test="${!empty req.certImg }">
										<label class="control-label col-sm-2">资质证明:</label>
										<div class="col-sm-4 form-control-static">
											<a href="${ctx}/member/req/download/article/${req.certImg}"
												target="_blank" class="btn-u btn-u-xs btn-u-dark"> 下载</a>
										</div>
									</c:if>
								</div>

								<div class="form-group">
									<label class="control-label col-sm-2">最迟响应时间:</label>
									<div class="col-sm-4 has-feedback">
										<input type="text" class="form-control Wdate "
											placeholder="接单媒体的最迟响应时间" id="deadline" name="deadline"
											value="<fmt:formatDate value="${req.deadline }" pattern="yyyy-MM-dd" />"
											readonly="readonly">
									</div>
								</div>
							</fieldset>
						</div>
						<!-- /col-sm-9 col-xs-offset-1 -->
					</div>
				</div>
			</div>
		</div>
		<!-- End Pad -->
	</div>
	<!-- End Main -->

	<script type="text/javascript">
		$(function() {
			menu.active('#my-req');
		});
	</script>
	<%@include
		file="/WEB-INF/views/member/req/advertiser/queryMediaCount.jspf"%>

</body>
</html>
