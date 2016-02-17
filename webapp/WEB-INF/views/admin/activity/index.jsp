<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<!doctype html>
<html>
<head>
<title>媒体查询</title>
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading">
			<ul class="breadcrumb">
				<li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
				<li class="active">活动列表</li>
			</ul>
		</div>
		<div class="panel-body">
			<div class="topwrap">
				<a class="btn btn-primary " href="${ctx }/admin/activity/create">创建活动</a>
			</div>
			<table
				class="table table-bordered table-condensed table-hover table-photos">
				<thead>
					<tr class="thead">
						<th>开始时间</th>
						<th>结束时间</th>
						<th>活动名称</th>
						<th>统一折扣</th>
						<th>参与媒体</th>
						<th>提交时间</th>
						<th>状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${data.content }" var="item">
						<tr>
							<td><fmt:formatDate value="${item.startTime}"
									pattern="yyyy-MM-dd" />
								</td>
							<td>
								<c:if test="${ not empty item.endTime }">
								
							<fmt:formatDate
									value="${item.endTime}" pattern="yyyy-MM-dd" /></td>
								</c:if>
								<c:if test="${empty item.endTime }">
								无
								</c:if>
							<td>${item.name }</td>
							<td><fmt:formatNumber type="number" value="${item.percent * 100}" pattern="0.0" maxFractionDigits="0"/>%</td>
							<td><a href="#" class="view"
								data-id="${item.id }">${item.mediaNum}个</a></td>
							<td><fmt:formatDate value="${item.createTime }"
									pattern="yyyy-MM-dd" /></td>
							<td><zy:dic value="${item.status }" /></td>
							<td><c:if test="${item.status eq 'ACTIVE'}">
									<a href="#" class="disable" data-id="${item.id }">下线</a>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<tags:pagination page="${data}" />
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			menu.active('#audit-activity');

			/*
			 *下线活动
			 */

			$('.disable')
					.click(
							function() {
								var activityId = $(this).attr("data-id");
								bootbox
										.confirm(
												"您确定下线该活动吗？",
												function(result) {
													if (result) {
														window.location.href = "${ctx}/admin/activity/disable/"
																+ activityId;
													}
												});
							});

			/*
			 *查看媒体
			 */
			$('.view').click(
					function() {
						var activityId = $(this).attr("data-id");
						window.location.href = "${ctx}/admin/activity/view/"
								+ activityId;
					});
		});
	</script>
</body>
</html>
