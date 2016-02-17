<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>


<form id="refuse" class="form-horizontal"
	action="${ctx}/member/req/deal/refuse" method="post">
	<zy:token />
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title">
			拒绝需求-<span style='color: red'>${reqmedia.requirement.name }</span>
		</h4>
	</div>
	<div class="modal-body">

		<input id="reqMediaId" type="hidden" name="reqMediaId"
			value="${reqmedia.id }">
		<div class="form-group">
			<label class="control-label col-xs-2"><span class="color-red">*</span>拒绝理由:</label>
			<div class="col-xs-8 has-feedback" id="div">
				<select class="form-control" name="refuseReason" id="refuseReason">
					<option value="">请选择...</option>
					<zy:options items="${rejectReasons }" itemLabel="itemName" itemValue="itemName"/>
				</select>
			</div>
		</div>
		<div class="form-group">
			<div class="modal-footer">
				<button class="btn-u btn-u-red" type="submit" id="refuseInvitation"
					data-loading-text="正在提交...">拒绝应邀</button>
			</div>
		</div>
	</div>
</form>

<script type="text/javascript">
	/**
	 拒绝应邀表单效验
	 */
	var $form = $('#refuse');

	$form.validate({
		debug : false,
		submitHandler : function(form) {
			$('#refuseInvitation').button('loading');
			common.disabled('#refuseInvitation');
			form.submit();
		},
		rules : {
			refuseReason : {
				required : true,
			}
		},
		messages : {
			refuseReason : {
				required : '请您输入拒绝理由',
			}
		}
	});
</script>