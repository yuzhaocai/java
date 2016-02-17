<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<!doctype html>
<html>
<head>
<title>会员中心</title>
</head>

<body>
	<div class="main-panel deposit-panel">
		<div class="headline">
			<h5>实名认证信息</h5>
			<a class="btn-u btn-u-sm btn-u-dark pull-right"
				href="${ctx }/member/userInfo"><i class="fa fa-angle-left"></i>返回</a>
		</div>
		<div class="panel pad pd15">
			<!-- 实名认证提交 -->
			<div id="div2" class="panel-body">
				<tags:fieldErrors commandName="vo" />
				<form class="form-horizontal"
					style="width: 833px; margin-left: 100px;">
					<input type="hidden" name="id" value="${cust.id }" id="customer_id" />
					<div class="form-group  text-center">
						<h2>完成实名认证可提高成单率哦~</h2>
						<br>
						<c:if test="${cust.custType eq 'CUST_T_ADVERTISER' }">
							<label class="col-xs-3 control-label"><span
								class="color-red">*</span> 联系人 </label>
							<div class="col-xs-6 has-feedback cert">
								<input class="form-control" type="text" name="certName"
									id="certName" value="${cust.certName }" maxlength="7">
							</div>
						</c:if>
						<c:if test="${cust.custType eq 'CUST_T_PROVIDER' }">
							<label class="col-xs-3 control-label"><span
								class="color-red">*</span> 姓名 </label>
							<div class="col-xs-6 has-feedback cert">
								<input class="form-control" type="text" name="certName"
									id="certName" value="${cust.certName }" maxlength="7">
							</div>
						</c:if>
					</div>

					<c:if test="${cust.custType eq 'CUST_T_ADVERTISER' }">
						<div class="form-group ">
							<label class="col-xs-3 control-label"><span
								class="color-red">*</span>公司名称</label>
							<div class="col-xs-6 has-feedback">
								<input class="form-control" type="text" name="certIdentity"
									id="certIdentity" value="${cust.certIdentity }" maxlength="20">
							</div>
						</div>
					</c:if>
					<c:if test="${cust.custType eq 'CUST_T_PROVIDER' }">
						<div class="form-group ">
							<label class="col-xs-3 control-label"><span
								class="color-red">*</span>认证身份</label>
							<div class="col-xs-6 has-feedback">
								<input class="form-control" type="text" name="certIdentity"
									id="certIdentity" value="${cust.certIdentity }" maxlength="20">
							</div>
						</div>
					</c:if>
					<input type="hidden" value="${cust.certStatus}" id="cert_status">
					<div class="form-group">
						<label class="col-xs-3 control-label"><span
							class="color-red">*</span>认证材料</label>
						<div class="img">
							<c:if test="${fn:length(pics) ge 1}">
								<c:forEach items="${pics }" var="pic">
									<div class="col-xs-3">
										<img name="images" width="200" height="286" alt="..."
											src="<zy:fileServerUrl value="${pic}"/>">
									</div>
								</c:forEach>
							</c:if>
						</div>
					</div>
				</form>
			</div>
			<!-- 实名认证提交 -->
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			menu.active('#user-info');
			var status = $("#cert_status").val();
			if (status == 'CERT_S_AUDIT') {
				$("#certName").attr("disabled", "disabled");
				$("#certIdentity").attr("disabled", "disabled");
			}
		});
	</script>

</body>
</html>