<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<!doctype html>
<html>
<head>
<title>媒体查询</title>
</head>
<body>
	<!-- Right -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<ul class="breadcrumb">
				<li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
				<li class="active">线下服务记录</li>
			</ul>
		</div>
		<div class="panel-body">
			<div class="topwrap">
				<form class="form-inline">
					<div class="form-group form-group-sm">
						<label>订单号</label> <input type="text" class="form-control"
							name="search_LIKE_id" value="${param['search_LIKE_id'] }"
							placeholder="请输入订单号">
					</div>
					<div class="form-group form-group-sm">
						<label>用户名</label> <input type="text" class="form-control"
							name="search_LIKE_customer.name"
							value="${param['search_LIKE_customer.name'] }"
							placeholder="请输入用户名">
					</div>
					<div class="form-group form-group-sm">
						<label>创建时间</label>
						<div class="input-group">
							<input type="text" class="form-control"
								id="search_GTE_createTime" name="search_GTE_createTime"
								value="${param.search_GTE_createTime }"
								onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_createTime\')}'})">
							<div class="input-group-addon">至</div>
							<input type="text" class="form-control"
								id="search_LTE_createTime" name="search_LTE_createTime"
								value="${param.search_LTE_createTime }"
								onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})">
						</div>
					</div>
					<div class="form-group form-group-sm">
						<button type="submit" class="btn btn-primary">
							<i class="fa fa-search"></i> 查询
						</button>
					</div>
				</form>
			</div>
			<table
				class="table table-bordered table-condensed table-hover table-photos"
				id="table1">
				<thead>
					<tr class="thead">
						<th width="25%">订单号</th>
						<th width="25%">用户名</th>
						<th width="25%">金额</th>
						<th width="25%">购买时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${data.content }" var="item">
						<tr>
							<td><zy:dic value="${item.id}" /></td>
							<td>${item.customer.name }</td>
							<td>${item.money }</td>
							<td><fmt:formatDate value="${item.createTime }"
									pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<!--// Table -->
			<tags:pagination page="${data }" />
		</div>
	</div>
	<!--// Right -->
	<script type="text/javascript">
		$(function() {
			menu.active('#offerline-man');
		});
	</script>
</body>
</html>
