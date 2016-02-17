<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<form class="form-horizontal" id="changedArticle"
	class="form-horizontal" action="${ctx}/member/req/deal/change"
	method="post" enctype="multipart/form-data">
	<zy:token />
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="title_yingyao">
			改稿申请-<span style='color: red'>${reqmedia.requirement.name }</span>
		</h4>
	</div>
	<div class="modal-body">
		<input id="changedAndInvReqMediaId" type="hidden" name="reqMediaId"
			value="${reqmedia.id }">
		<div class="form-group">
			<div class="col-xs-12 has-feedback">
				<textarea name="changedReason" class="form-control" rows="3"
					placeholder="请输入理由..." id="changedReason"></textarea>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-4"><span class=""
				style="color: red">*上传您修改后的稿件：</span></label>
			<div class="col-xs-8 has-feedback">
				<input type="file" name="image">
				<p class="hightlight">
					<small>仅支持图片和word/pdf文档！</small>
				</p>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn-u btn-u-red" type="submit" id="changedAndInv"
			data-loading-text="正在提交...">改稿应邀</button>
	</div>
</form>

<script type="text/javascript">
	/**
	改稿应邀表单效验
	 */
	$('#changedArticle').validate({
		debug : false,
		submitHandler : function(form) {
			$('#changedAndInv').button('loading');
			common.disabled('#changedAndInv');
			form.submit();
		},
		rules : {
			changedReason : {
				required : true,
				rangelength : [ 2, 1000 ]
			},
			image : {
				required : true
			}
		},
		messages : {
			changedReason : {
				required : '请您填写改稿理由',
				rangelength : '请输入2~1000个字符'
			},
			image : {
				required : '请上传您修改后的稿件'
			}
		}
	});
</script>