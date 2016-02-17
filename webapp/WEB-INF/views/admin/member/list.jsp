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
				<li class="active">注册会员查询</li>
			</ul>
		</div>
		<div class="panel-body">
			<div class="topwrap">
				<form class="form-inline">
					<label class="control-label">注册时间</label>
					<div class="form-group form-group-sm">
						<input class="form-control"
							id="search_GTE_createTime" name="search_GTE_createTime"
							value="${param.search_GTE_createTime }" placeholder=" 从"
							onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_createTime\')}'})"
							type="text">
					</div>
					<div class="form-group form-group-sm">
						<input class="form-control"
							id="search_LTE_createTime" name="search_LTE_createTime"
							value="${param.search_LTE_createTime }" placeholder="至"
							onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})"
							type="text">
					</div>
					<div class="form-group form-group-sm">
						<label>手机</label> <input type="text" class="form-control"
							name="search_LIKE_customer.mobPhone"
							value="${param['search_LIKE_customer.mobPhone'] }"
							placeholder="请输入手机号" maxlength="11">
					</div>
					<div class="form-group form-group-sm">
						<label>类型</label> <select class="form-control"
							name="search_EQ_customer.custType">
							<option value="">--请选择--</option>
							<c:forEach items="${types.dicItems }" var="type">
								<c:if
									test="${type.itemCode eq 'CUST_T_PROVIDER' ||  type.itemCode eq 'CUST_T_ADVERTISER'}">
									<option value="${type.itemCode}"
										<c:if test="${type.itemCode eq param.search_EQ_customer.custType }">selected</c:if>>${type.itemName}</option>
								</c:if>
							</c:forEach>
						</select>
					</div>
					<div class="form-group form-group-sm">
						<label>名称</label> <input type="text" class="form-control"
							name="search_LIKE_nickname"
							value="${param.search_LIKE_nickname }"
							placeholder="请输入名称">
					</div>
                    <label class="control-label">登陆时间</label>
					<div class="form-group form-group-sm">
						<input class="form-control" 
							id="search_GTE_latestTime" name="search_GTE_latestTime" 
							value="${param.search_GTE_latestTime }" placeholder=" 从"
							onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_latestTime\')}'})">
					</div>
					<div class="form-group form-group-sm">
						<input class="form-control"
							id="search_LTE_latestTime" name="search_LTE_latestTime"
							value="${param.search_LTE_latestTime }" placeholder="至"
							onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_latestTime\')}'})">
					</div>
					<div class="form-group form-group-sm"> 
                           <label>实名认证</label>
                           <select class="form-control" name="search_EQ_customer.certified"> 
                               <option value="">不限</option>
                               <option value="true" <c:if test="${param['search_EQ_customer.certified'] eq 'true' }">selected</c:if>>已认证</option>
                               <option value="false" <c:if test="${param['search_EQ_customer.certified'] eq 'false' }">selected</c:if>>未认证</option>
                           </select>
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
						<th>会员类型</th>
						<th>手机号</th>
						<th>名称</th>
						<th>注册时间</th>
						<th>最近登录时间</th>
						<th>最近登录IP</th>
						<th>实名认证</th>
						<th class="text-center">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${data.content }" var="user">
						<tr>
							<td><zy:dic value="${user.customer.custType }" /></td>
							<td>${user.customer.mobPhone }</td>
							<td>${user.nickname }</td>
							<td><fmt:formatDate value="${user.createTime }"
									pattern="yyyy-MM-dd" /></td>
							<td><fmt:formatDate value="${user.latestTime }"
									pattern="yyyy-MM-dd" /></td>
							<td>${user.latestIp }</td>
							<td><c:if test="${user.customer.certified eq true }">
									<a href="javascript:void(0)"
										onclick="certificate('${user.customer.id}');">查看</a>
								</c:if> <c:if test="${user.customer.certified eq false }">
									未认证
								</c:if></td>
							<td class="text-center">
								<div class="btn-group btn-group-sm">
									<button data-id="${user.id }" class="btn btn-primary preview-btn"><i class="fa fa-eye"></i>查看</button>									
                                    <button class="btn btn-primary updatePhone"
                                        data-id=${user.id }
                                        data-name=${user.nickname }
                                        data-phone=${user.customer.mobPhone }>
                                        <i class="fa fa-edit"></i> 修改手机号
                                    </button>
									<button class="btn btn-primary"
										onclick="resetPassword('${user.id}','${user.loginName }');">
										<i class="fa fa-refresh"></i> 密码初始化
									</button>
									<c:if test="${user.status eq 'USER_ENABLED' }">
										<button class="btn btn-danger"
											onclick="freeze('${user.id}','${user.loginName }');">
											<i class="fa fa-lock"></i> 冻结
										</button>
									</c:if>
									<c:if test="${user.status eq 'USER_DISABLED' }">
										<button class="btn btn-success"
											onclick="unfreeze('${user.id}','${user.loginName }');">
											<i class="fa fa-key"></i> 解冻
										</button>
									</c:if>
									<c:if test="${user.shutup eq false }">
										<button class="btn btn-danger"
											onclick="banned('${user.id}','${user.loginName }');">
											<i class="fa fa-ban"></i> 禁言
										</button>
									</c:if>
									<c:if test="${user.shutup eq true }">
										<button class="btn btn-success"
											onclick="unbanned('${user.id}','${user.loginName }');">
											<i class="fa fa-comment-o"></i> 解禁
										</button>
									</c:if>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<!--// Table -->
			<tags:pagination page="${data }" />
		</div>
	</div>
	<!--// Right -->



	<!--查看实名认证  -->
	<div class="modal fade" id="cert" tabindex="-1" role="dialog"
		data-width="640" aria-labelledby="myModalLabel" data-replace="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title">申请人详细</h4>
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
	
	
		<!--修改手机号  -->
	<div class="modal fade" id="updatePhone" tabindex="-1" role="dialog" data-replace="false" data-backdrop="static">
	
	    <form id="updatePhone-form" action="${ctx}/admin/member/updatePhone" method="post">
	      <input type="hidden" name="id">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">修改手机号</h4>
	      </div>
	      <div class="">
              <label class="col-xs-4 control-label"><span class="color-red"></span>用户名称:</label>
	              <div class="col-xs-8 has-feedback">
	                  <div class="input-group">
	                      <input type="text" class="form-control" name="userName" disabled="disabled"/>
	                  </div>
	              </div>
              <label class="col-xs-4 control-label"><span class="color-red">*</span>手机号码:</label>
              <div class="col-xs-8 has-feedback">
                  <div class="input-group">
                      <input type="text" class="form-control" id="mobPhone" name="mobPhone"  maxlength="11"/>
                  </div>
              </div>
	      </div>
	      <div class="modal-footer">
	      	<button class="btn-u btn-u-red" type="submit" 
	      		id="btn-refuse-ok"
	      		data-loading-text="正在处理...">确定</button>
	      </div>
	    </form>
	
	</div>
	
        <!--修改手机号  -->
    <div class="modal fade" id="preview" tabindex="-1" role="dialog" data-replace="false" data-backdrop="static">
    </div>
	<script type="text/javascript">
		$(function() {
			menu.active('#member-register');
			
		    $(".preview-btn").click(function() {
		        $('#preview').loadModal('${ctx}/admin/member/preview?ajax', {id: $(this).attr("data-id")}, function() {
		            $('#btn-close').click(function() {
		                $('#preview').modal('hide');
		            });
		        });
		        return false;
		    });  
			
			$('.updatePhone').click(function(){
				var $this = $(this);
				$("#updatePhone input[name='id']").val($this.data('id'));
				$("#updatePhone input[name='userName']").val($this.data('name'));
				$("#updatePhone input[name='mobPhone']").val($this.data('phone'));
				$('#updatePhone').showModal();
			});
			
			$('#updatePhone-form').validate({
				debug: false,
				submitHandler: function(form) {
					common.disabled('#btn-updatePhone');
					form.submit();
				}, 
				rules: {
					mobPhone: {
						required: true,
						digits:true,
						minlength:11,
						remote: '${ctx}/common/checkMobPhone'
					}
				},
				messages: {
					mobPhone: {
						required: '请您输入手机号！',
						remote: '此手机号码已经被注册！',
						minlength : '请输入正确的手机号!'
					}
				}
			});	
		});
		
		


		/*
		 *冻结
		 */

		function resetPassword(userId, name) {
			bootbox
					.confirm(
							"您确定要初始化用户： <span style='color:red'>" + name
									+ "</span> 的密码？",
							function(result) {
								if (result) {
									window.location.href = "${ctx}/admin/member/resetPwd?userId="
											+ userId;
								}
							})
		}

		/*
		 *冻结
		 */

		function freeze(userId, name) {
			bootbox
					.confirm(
							"您确定要冻结用户：<span style='color:red'>" + name
									+ "</span>？",
							function(result) {
								if (result) {
									window.location.href = "${ctx}/admin/member/freeze?userId="
											+ userId;
								}
							})
		}

		/*
		 *解冻
		 */

		function unfreeze(userId, name) {
			bootbox
					.confirm(
							"您确定要解冻用户： <span style='color:green'>" + name
									+ "</span>？",
							function(result) {
								if (result) {
									window.location.href = "${ctx}/admin/member/unfreeze?userId="
											+ userId;
								}
							})
		}
		
		/*
		 *禁言
		 */

		function banned(userId, name) {
			bootbox
					.confirm(
							"您确定要禁言用户： <span style='color:red'>" + name
									+ "</span>？",
							function(result) {
								if (result) {
									window.location.href = "${ctx}/admin/member/banned?userId="
											+ userId;
								}
							})
		}
		/*
		 *解禁
		 */

		function unbanned(userId, name) {
			bootbox
					.confirm(
							"您确定要解禁用户： <span style='color:green'>" + name
									+ "</span>？",
							function(result) {
								if (result) {
									window.location.href = "${ctx}/admin/member/unbanned?userId="
											+ userId;
								}
							})
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
