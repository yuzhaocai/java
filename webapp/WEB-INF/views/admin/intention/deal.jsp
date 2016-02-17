<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<form id="deal-form" action="${ctx}/admin/intention/dealintention" method="post">
	<zy:token />
	
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title">订单处理</h4>
	</div>

	<div class="modal-body">
		<input type="hidden" name="id" value="${param.id }">
		<div class="form-group ">
			<label class="control-label col-xs-2">对接人：</label>
			<div class="input-group col-xs-10 has-feedback">
				<input type="text" class="form-control" id="custManager" name="custManager" maxlength="32">
			</div>
		</div>
		<div class="form-group ">
			<label class="control-label col-xs-2">处理结果：</label>
			<div class="input-group col-xs-10 has-feedback">
				<textarea class="form-control" rows="5" name="handleResult"></textarea>
			</div>
		</div>
	</div>

	<div class="modal-footer">
		<button class="btn-u btn-u-red" type="submit" id="intention-ok">确定</button>
	</div>
</form>

<script type="text/javascript">
	/**
	处理表单效验
	 */
	$('#deal-form').validate({
		debug : true,
		submitHandler : function(form) {
			$('#bintention-ok').button('loading');
			form.submit();
		},
		rules : {
			custManager : {
				required : true,
				rangelength:[2,30]
			},
			handleResult : {
				required : true,
				rangelength:[2,1000]
			}
		},
		messages : {
			custManager : {
				required : '请您输入对接人！',
				rangelength : '请输入2~30个字符！'
			},
			handleResult : {
				required : '请您输入处理结果！',
				rangelength : '请输入2~1000个字符！'
			}
		}
	});

</script>
