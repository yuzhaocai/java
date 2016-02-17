<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>
	
<form id="deliverTask" action="${ctx}/member/order/provider/deliverable"
	method="post" enctype="multipart/form-data">
	<zy:token />
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title">订单交付</h4>
	</div>
	<div class="modal-body">
		<input id="orderId" type="hidden" name="orderId" value="${order.id }">
		<input id="deliverId" type="hidden" name="id"
			value="${order.deliverable.id }">
		<div class="row">
			<div class="col-xs-6">
				需求名称：<a data-id="${order.requirement.id }"
					class="preview-requirement" href="#">${order.requirement.name }</a>
			</div>
			<div class="col-xs-6">
				合作广告商：<a data-id="${order.media.id }" class="preview-media" href="#">${order.media.name }</a>
			</div>
		</div>
		<br>
		<div class="form-group has-feedback">
			<label for="url"><span class="color-red">*</span>成稿链接:</label> <input type="url" id="url" name="url"
				class="form-control" placeholder="将完成稿链接复制于此...">
		</div>
		<div class="form-group has-feedback">
			<label for="url"><span class="color-red">*</span>图片:<small
				class="color-red">（请上传完成稿的截图或二维码）</small></label>
			<div class="row img-group img" id="draft">
				<!--   	增加成稿上传图片            begin	  	 -->
				<div class="col-xs-6">
					<div class="fileinput fileinput-new" data-provides="fileinput">
						<div class="fileinput-new thumbnail"
							style="width: 200px; height: 130px;">
							<img data-src="" alt="..."
								src="${ctx}/static/assets/img/main/img1.jpg">
						</div>
						<div class="fileinput-preview fileinput-exists thumbnail"
							style="width: 200px; height: 130px;"></div>
						<div>
							<span class="btn btn-default btn-file"><span
								class="fileinput-new">选择图片</span><span class="fileinput-exists">更改图片</span><input
								type="file" name="images"></span>
						</div>
					</div>
				</div>
				<!--   	增加成稿上传图片           end 	  	 -->
			</div>
		</div>
			<a class="btn-u btn-u-sm btn-u-dark" id="addbtn"><i
				class="fa fa-plus"></i>添加</a>
		<div class="modal-footer">
			<button class="btn-u btn-u-red" type="submit" id="btn-deliver"
				data-loading-text="正在交付...">完成</button>
		</div>
	</div>
</form>
<!-- 媒体详情 -->
<div class="modal fade" id="media-view" tabindex="-1" role="dialog"
	data-width="900" data-replace="true"></div>

<!-- 需求详情 -->
<div class="modal fade" id="requirement-view" tabindex="-1"
	role="dialog" data-width="700" data-replace="true"></div>

<script type="text/javascript">
	common.showMediaNow(".preview-media", $('#deliverableModal'));
	common.showRequirementNow(".preview-requirement",$('#deliverableModal'));

	$(function() {
		$('#addbtn')
				.click(
						function(e) {
							var newimg = '<div class="col-xs-6"><div class="fileinput fileinput-new" data-provides="fileinput"><div class="fileinput-new  thumbnail" style="width: 200px; height: 130px;"><img data-src="holder.js/100%x100%" alt="..." src="${ctx}/static/assets/img/main/img1.jpg"></div><div class="fileinput-preview fileinput-exists thumbnail" style="width: 200px; height: 130px;"></div><div><span class="btn btn-default btn-file"><span class="fileinput-new">选择图片</span><span class="fileinput-exists">更改图片</span><input type="file" name="images"></span><a href="#" class="btn btn-default rem">删除</a></div></div></div>';
							$('.img').append(newimg);
							var len = $('div.img').children().length;
							if (len >= 4) {
								$(this).hide();
							}
							removePic();
						});
		removePic();
	});

	/*
	 删除图片上传域	
	 */
	function removePic() {
		$('.rem').click(function() {
			$(this).parent().parent().parent().remove();
			var len = $('div.img').children().length;
			if (len < 4) {
				$('#addbtn').show();
			} else {
				$('#addbtn').hide();
			}
		});
	}

	/**
	交付表单效验
	 */
	$('#deliverTask').validate({
		debug : true,
		submitHandler : function(form) {
			$('#btn-deliver').button('loading');
			form.submit();
		},
		rules : {
			url : {
				required : true,
				url : true
			},
			images : {
				required : true
			}
		},
		messages : {
			url : {
				required : '请您填写完成稿的链接',
				url : '请您输入正确的网址'
			},
			images : {
				required : '请您上传完成稿的截图或二维码'
			}
		}
	});
</script>