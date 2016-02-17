<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<!doctype html>
<html>
<head>
<script src="${ctx }/static/assets/js/changeImg.js" type="text/javascript"></script>
<link href="${ctx }/static/assets/css/changeImg.css" rel="stylesheet">
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
				<form id="form2" class="form-horizontal"
					action="${ctx }/member/certification" method="post"
					style="width: 833px; margin-left: 100px;"
					enctype="multipart/form-data">
					<input type="hidden" name="id" value="${cust.id }" id="customer_id" />
					<div class="form-group  ">
						<div class="text-center">
						<c:if test="${cust.certStatus eq 'CERT_S_PASS' }">
							<h2>您已通过实名认证，重新认证请上传认证材料</h2>
						</c:if>
						<c:if test="${cust.certStatus ne 'CERT_S_PASS' }">
							<h2>完成实名认证可提高成单率哦~</h2>
						</c:if>
						</div>
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
					<div class="form-group ">
						<label class="col-xs-3 control-label"><span
							class="color-red">*</span>认证材料</label>
						<div class="col-xs-9 uploadImg has-feedback">
						<c:if test="${cust.custType eq 'CUST_T_PROVIDER' }">
							<small id="small" class="color-red">请上传记者证，身份证等证件扫描件，高清照片或其他公司证明材料</small>
						</c:if>
						<c:if test="${cust.custType eq 'CUST_T_ADVERTISER' }">
							<small id="small" class="color-red">请上传公司营业执照高清照片或扫描件</small>
						</c:if>
							<!-- Unnamed (形状) -->
							<c:if
								test="${cust.custType eq 'CUST_T_PROVIDER' || cust.custType eq 'CUST_T_ADVERTISER'}">
							<div class="fileImg">
								<div class="axImg">
									<img id="imgChange0" class="img"
										src="${ctx }/static/assets/img/uploadImg.png" />
									<!-- Unnamed () -->
									<div class="imgchangeDiv">
										<p style="font-size: 36px;">
											<span style="font-size: 36px;">+</span>
										</p>
										<p style="font-size: 28px;">
										<c:if test="${cust.custType eq 'CUST_T_PROVIDER'}">
											<span style="font-size: 28px;">身份证正面</span>
										</c:if>
										<c:if test="${cust.custType eq 'CUST_T_ADVERTISER'}">
											<span style="font-size: 28px;">营业执照</span>
										</c:if>
										</p>
									</div>
								</div>
								<input type="file" class="fileCss" name="images">
							</div>
							<!-- Unnamed (形状) -->
							<c:if	test="${cust.custType eq 'CUST_T_PROVIDER'}">
							<div class="fileImg fileImg1">
								<div class="axImg">
									<img id="imgChange1" class="img"
										src="${ctx }/static/assets/img/uploadImg.png" />
									<!-- Unnamed () -->
									<div class="imgchangeDiv">
										<p style="font-size: 36px;">
											<span style="font-size: 36px;">+</span>
										</p>
										<p style="font-size: 28px;">
											<span style="font-size: 28px;">记者证</span>
										</p>
									</div>
								</div>
								<input type="file" class="fileCss" name="images">
							</div>
							<!-- Unnamed (形状) -->
							<div class="fileImg fileImg2">
								<div class="axImg">
									<img id="imgChange2" class="img"
										src="${ctx }/static/assets/img/uploadImg.png" />
									<!-- Unnamed () -->
									<div class="imgchangeDiv">
										<p style="font-size: 36px;">
											<span style="font-size: 36px;">+</span>
										</p>
										<p style="font-size: 28px;">
											<span style="font-size: 28px;">其他</span>
										</p>
									</div>
								</div>
								<input type="file" class="fileCss" name="images">
							</div>
							</c:if>
							</c:if>
						</div>
					</div>
					<div class="form-group" id="button_sub">
						<label class="col-xs-4 control-label"></label>
						<div class="col-xs-6">
							<button class="btn-u btn-u-lg btn-u-green w200"
								id="btn-submit-certification">提交</button>
						</div>
					</div>
				</form>
			</div>
			<!-- 实名认证提交 -->
		</div>
	</div>
	<input type="hidden" id="hasFileImg"/>
	<script type="text/javascript">
		$(function() {
			
			/*清除火狐浏览器缓存*/
			$('#hasFileImg').val('');
			$('input[name="images"]').val('');
			
			menu.active('#user-info');
		});

		$(document).ready(function() {
			/*
			 *显示实名认证资料
			 */
			var status = $("#cert_status").val();
			var customerId = $("#customer_id").val();
			if (status == 'CERT_S_NULL') {
				$('#certMatter').hide();
			} else {
				$.ajax({
					type : "GET",
					url : "${ctx }/member/certificate?ajax",
					data : {
						customerId : customerId
					},
					dataType : "json",
					success : function(data) {
						$("#certMatter").attr("src", data.certMatter);
					}
				});
			}

			if (status == 'CERT_S_AUDIT') {
				$("#certName").attr("disabled", "disabled");
				$("#certIdentity").attr("disabled", "disabled");
				$("#button_sub").hide();
				$("#saveFile").hide();
			}
		});

		$('#form2')
				.validate(
						{
							debug : false,
							submitHandler : function(form) {
								common.disabled('#btn-submit-certification');
								form.submit();
							},
							rules : {
								certName : {
									required : true,
									rangelength : [ 2, 7 ]
								},
								certIdentity : {
									required : true,
									rangelength : [ 2, 30 ]
								},
								images : {
									required : {
										depends : hasFileImg
									}
								}
							},

							messages : {
								certName : {
									required : '请填写真实姓名！',
									rangelength : '请输入2~7个字符'
								},
								certIdentity : {
									required : '请认真填写该字段！',
									rangelength : '请输入2~40个字符'
								},
								images : {
									required : '<c:if test="${cust.custType eq 'CUST_T_ADVERTISER' }">请上传公司营业执照高清照片或扫描件</c:if><c:if test="${cust.custType eq 'CUST_T_PROVIDER' }">请上传记者证，身份证等证件扫描件，高清照片或其他公司证明材料</c:if>'
								}
							}
						});
		
		
		/*判断是否已经上传了图片*/
		function hasFileImg() {
			var hasFileImg = $('#hasFileImg').val();
			if (hasFileImg) {
				return false;
			} else {
				return true;
			}
		}
		
	</script>
</body>
</html>