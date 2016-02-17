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
				<li class="active">查看媒体</li>
			</ul>
		</div>
		<div class="panel-body">
			<div class="topwrap">
				活动名称：${activityName }
				<button type="button" class="btn-u btn-u-sm btn-u-dark pull-right"
					onclick="location.replace(document.referrer);">
					<i class="fa fa-angle-left"></i> 返回
				</button>
			</div>
			<table
				class="table table-bordered table-condensed table-hover table-photos">
				<thead>
					<tr class="thead">
						<th>头像</th>
						<th>大类</th>
						<th>认证类别</th>
						<th>昵称</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${data.content }" var="item">
						<tr>
							<td><img width="100" height="80"
								src="<zy:fileServerUrl value="${item.media.showPic }" />"
								class="img-circle media-avatar" /></td>
							<td><zy:dic value="${item.media.mediaType}" /></td>
							<td><zy:dic value="${item.media.category }" /></td>
							<td>${item.media.name}</td>
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
		});
	</script>
</body>
</html>
