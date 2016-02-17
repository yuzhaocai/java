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
				<li class="active">实名认证审核</li>
			</ul>
		</div>
		<div class="panel-body">
			<!-- Table -->
			<table class="table table-bordered table-condensed table-hover"
				id="table1">
				<thead>
					<tr class="thead">
						<th>会员类型</th>
						<th>手机号</th>
						<th>注册时间</th>
						<th>提交认证时间</th>
						<th>实名认证材料</th>
						<th class="text-right">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${data.content }" var="customer">
						<tr>
							<td><zy:dic value="${customer.custType }" /></td>
							<td>${customer.mobPhone }</td>
							<td><fmt:formatDate value="${customer.createTime }"
									pattern="yyyy/MM/dd HH:mm" /></td>
							<td><fmt:formatDate value="${customer.certSubmitTime }"
									pattern="yyyy/MM/dd HH:mm" /></td>
							<td><a href="javascript:void(0)"
								onclick="certificate('${customer.id}');">查看</a></td>
							<td class="text-right"><c:if
									test="${customer.certStatus eq 'CERT_S_AUDIT' }">
									<div class="btn-group btn-group-sm">
										<button class="btn btn-success"
											onclick="pass('${customer.id}');">
											<i class="fa fa-check"></i> 认证通过
										</button>
										<button class="btn btn-danger"
											onclick="unpass('${customer.id}');">
											<i class="fa fa-close"></i> 不通过
										</button>
									</div>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<!--// Table -->

			<tags:pagination page="${data}" />

		</div>
	</div>

	<!--查看实名认证  -->

	<div class="modal fade" id="cert" tabindex="-1" role="dialog"
		data-width="640" data-backdrop="static">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title">实名认证详细</h4>
		</div>
		<div class="modal-body">
			<div class="row">
				<div class="col-xs-3">
					<span id="name"></span>：<a href="#"><span id="certName"></span></a>
				</div>
				<div class="col-xs-9">
					<span id="company"></span>：<a href="#"><span id="certIndentity"></span></a>
				</div>
			</div>
			<br>
			<div class="row">
				<div class="col-xs-3">
					<label>认证材料:</label>
				</div>
				<div class="col-xs-9">
					<img alt="" src="" width="200" height="150" id="certMatter1">
					<img alt="" src="" width="200" height="150" id="certMatter2">
					<img alt="" src="" width="200" height="150" id="certMatter3">
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn-u btn-u-red" type="button" data-dismiss="modal"
					aria-label="Close">关闭</button>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {
			menu.active('#member-cert');
		});

		/*
		 *认证通过
		 */

		function pass(customerId) {
			bootbox
					.confirm(
							"您确定同意该实名认证请求吗？",
							function(result) {
								if (result) {
									window.location.href = "${ctx}/admin/member/cert/pass?customerId="
											+ customerId;
								}
							});
		}

		/*
		 *认证不通过
		 */

		function unpass(customerId) {
			bootbox
					.confirm(
							"您确定拒绝该实名认证请求吗？",
							function(result) {
								if (result) {
									window.location.href = "${ctx}/admin/member/cert/unpass?customerId="
											+ customerId;
								}
							});
		}

		/*
		 *查看实名认证资料
		 */
		function certificate(customerId) {
			$.ajax({
				type : "GET",
				url : "${ctx}/admin/member/cert/certificate?ajax",
				data : {
					customerId : customerId
				},
				dataType : "json",
				success : function(data) {
					$("#certName").html(data.certName);
					$("#certIndentity").html(data.certIndentity);
				   if (data.custType == 'CUST_T_ADVERTISER') {
						$("#name").html("联系人");
						$("#company").html("公司名称");
					} else {
						$("#name").html("姓名");
						$("#company").html("认证身份");
					}

					if (data.certMatter1 != null) {
						$("#certMatter1").show();
						$("#certMatter1").attr("src", data.certMatter1);
					} else {
						$("#certMatter1").hide();
					}
					if (data.certMatter2 != null) {
						$("#certMatter2").show();
						$("#certMatter2").attr("src", data.certMatter2);
					} else {
						$("#certMatter2").hide();
					}
					if (data.certMatter3 != null) {
						$("#certMatter3").show();
						$("#certMatter3").attr("src", data.certMatter3);
					} else {
						$("#certMatter3").hide();
					}
					$("#cert").modal("show");
				}
			});
		}
	</script>
</body>
</html>
