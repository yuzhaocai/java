<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<form id="form-e-case" action="${ctx}/member/media/case/save"
	method="post" enctype="multipart/form-data">
	<zy:token />
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title">修改案例</h4>
	</div>
	<div class="modal-body">
		<input type="hidden" value="${mediaId }" name="mediaId"> <input
			type="hidden" id="caseId" name="id" value="${case.id }">
		<div class="form-group form-group-sm">
			<label class=""><span class="color-red">*</span>标题:</label>
			<div class="has-feedback">
				<input type="text" name="title" class="form-control"
					placeholder="案例标题" id="title-case" value="${case.title }">
			</div>
			<label class=""><span class="color-red">*</span>亮点:</label>
			<div class="has-feedback">
				<input type="text" name="light" class="form-control"
					placeholder="案例亮点" id="light-case" value="${case.light }">
			</div>
		</div>
		<div class="form-group form-group-sm">
			<label class=""><span class="color-red">*</span>简介:</label>
			<div class="has-feedback">
				<textarea name="content" class="form-control height-100" rows="3"
					placeholder="描述案例简介" id="content-case">${case.content }</textarea>
			</div>
		</div>
		<div class="form-group form-group-sm has-feedback">
			<label class="">展示图片:<small class="color-red">(请上传成功案例的截图或其它效果图，这样更有说服力，以获取更多企业的青睐！)</small></label>
			<div class="row img">
				<c:if test="${fn:length(pics) ge 1}">
					<c:forEach items="${pics }" var="pic">
						<!-- 循环添加图片路径 -->
						<div class="col-xs-6">
							<div class="fileinput fileinput-new " data-provides="fileinput">
								<div class="fileinput-new fileinput-exists thumbnail  "
									style="width: 200px; height: 130px;">
									<img data-src="" alt="..."
										src="${ctx}/static/assets/img/main/img1.jpg">
								</div>
								<div class="fileinput-preview thumbnail"
									style="width: 200px; height: 130px;">
									<img name="images" data-src="" alt="..."
										src="<zy:fileServerUrl value="${pic}"/>">
								</div>
								<div>
									<span class="btn btn-default btn-file"><span
										class="fileinput-new">选择图片</span><span
										class="fileinput-exists">更改图片</span><input type="file"
										name="images"></span><input type="hidden" value="${pic}"
										name="pics"> <a href="#" class="btn btn-default rem"
										data-dismiss="fileinput">删除</a>
								</div>
							</div>
						</div>
					</c:forEach>
				</c:if>
				<c:if test="${fn:length(pics) lt 1}">
					<div class="col-xs-6">
						<div class="fileinput fileinput-new" data-provides="fileinput">
							<div class="fileinput-new thumbnail"
								style="width: 200px; height: 130px;">
								<img data-src="" alt="..."
									src="${ctx}/static/assets/img/main/img1.jpg">
							</div>
							<div class="fileinput-preview fileinput-exists thumbnail"
								style="width: 200px; height: 130px;">
								<img data-src="" alt="..."
									src="<zy:fileServerUrl value="${pic}"/>">
							</div>
							<div>
								<span class="btn btn-default btn-file"><span
									class="fileinput-new">选择图片</span><span class="fileinput-exists">更改图片</span><input
									type="file" name="images"><input type="hidden" value="${pic}"
										name="pics"></span> 
							</div>
						</div>
					</div>
				</c:if>
			</div>
			<div class="row">
				<div class="col-xs-8">
					<a class="btn btn-default" href="#" id="addbtn">添加图片</a> <small
						class="color-red">*图片大小不超过2Mb，格式为JPG,PNG,BMP。</small>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn-u btn-u-red" type="submit">确定修改</button>
	</div>
</form>

<script type="text/javascript">
	$(function() {
		removeButton();
		$('#addbtn')
				.click(
						function(e) {
							var newimg = '<div class="col-xs-6"><div class="fileinput fileinput-new" data-provides="fileinput"><div class="fileinput-new  thumbnail" style="width: 200px; height: 130px;"><img data-src="holder.js/100%x100%" alt="..." src="${ctx}/static/assets/img/main/img1.jpg"></div><div class="fileinput-preview fileinput-exists thumbnail" style="width: 200px; height: 130px;"></div><div><span class="btn btn-default btn-file"><span class="fileinput-new">选择图片</span><span class="fileinput-exists">更改图片</span><input type="file" name="images"><input type="hidden" value="${pic}"	name="pics"></span><a href="#" class="btn btn-default rem">删除</a></div></div></div>';
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
			removeButton();
		});
	}
	/*
		删除按钮
	*/
	function removeButton(){
		var len = $('div.img').children().length;
		if (len < 4) {
			$('#addbtn').show();
		} else {
			$('#addbtn').hide();
		}
	}
	/**
	表单效验
	 */
	$('#form-e-case').validate({
		debug : false,
		submitHandler : function(form) {
			form.submit();
		},
		rules : {
			title : {
				required : true,
				rangelength : [ 4, 30 ]
			},
			light : {
				required : true,
				rangelength : [ 4, 30 ]
			},
			content : {
				required : true,
				rangelength : [ 20, 10000 ]
			}
		},
		messages : {
			title : {
				required : '请填写案例标题',
				rangelength : '请输入4~30个字符'
			},
			light : {
				required : '请填写案例亮点',
				rangelength : '请输入4~30个字符'
			},
			content : {
				required : '请填写案例简介',
				rangelength : '请输入20~10000个字符'
			}
		}
	});
</script>