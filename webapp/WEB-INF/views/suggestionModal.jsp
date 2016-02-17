<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>
<script type="text/javascript" src="${ctx }/static/bower_components/jquery-form/jquery.form.js"></script>

<!--投诉建议模态框 -->
<div class="modal fade in complaintsAll" id="modal-complaint" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" 
	data-backdrop="static" style="display: block;" data-width="650" aria-hidden="true" >
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close btnColor" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">投诉建议</h4>
			</div>
			<div class="complaintMain">
				<div class="modal-body">
					<form class=" form-horizontal" id="suggestion-form"
			action="${ctx}/suggestion/suggest" method="post"
			enctype="multipart/form-data" onsubmit="return saveReport();">
			<zy:token />
						<div class="tab">
							<ul class="list-inline tab_menu ">
								<c:forEach var="suggestion" items="${suggestions.dicItems}">
						<li class="noPaleft"><label class="radio-inline"><input type="radio"
								name="feedbackType" value="${suggestion.itemCode}"
								id="${suggestion.itemCode}">${suggestion.itemName}</label></li>
					</c:forEach>
							</ul>
							<div class="tab_box">
								<div class="alert alert-info open">咨询关于网站使用时不懂的操作，以及关于网站资源、功能、服务方面的投诉与建议等。</div>
								<div class="alert alert-info">关于对本网站和服务改善的意见或建议。</div>
								<div class="alert alert-info">关于网站报错，功能不正常，功能没反应，无法操作等问题</div>
							</div>
						</div>
						<h2 class="numList">
							<span>1</span>请留下您对采媒在线的意见或建议
						</h2>
						<div class="form-group">
							<div class="col-sm-12 has-feedback">
								<textarea class="form-control" name="feedbackContent"></textarea>
							</div>
						</div>
						<div class="form-group hide" id="attachment">
							<label class="control-label col-sm-2 txtLeft">附件</label>
							<div class="col-sm-10 noPaleft">
								<div class="fileinput fileinput-new input-group"
									data-provides="fileinput">
									<div class="form-control" data-trigger="fileinput">
										<i class="glyphicon glyphicon-file fileinput-exists"></i><span
											class="fileinput-filename"></span>
									</div>
									<span class="input-group-addon btn btn-default btn-file"><span
										class="fileinput-new">选择文件</span><span
										class="fileinput-exists">重选</span><input type="file" name="feedbackAttachment"></span><a href="#"
										class="input-group-addon btn btn-default fileinput-exists"
										data-dismiss="fileinput">删除</a>
								</div>
							</div>
						</div>
						<h2 class="numList">
							<span>2</span>请填写以下信息，方便我们与您联系
						</h2>
						<div class="form-group">
							<label class="control-label col-sm-2 txtLeft">手机号</label>
							<div class="col-sm-10 has-feedback noPaleft">
								<input type="text" class="form-control" placeholder="请输入手机号"
						name="phone" maxlength="11">
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2 txtLeft">验证码</label>
							<div class="col-sm-5 has-feedback noPaleft">
								<div class="input-group">
										<input type="text" class="form-control captcha1" maxlength="4"
							placeholder="验证码" name="captcha" > <span
							class="input-group-addon captcha"><img id="img_captcha"
							src="${ctx}/images/kaptcha.jpg" style="cursor: pointer"></span>
								</div>
							</div>
						</div>
						<input type="hidden" name="url">
					</form>
				</div>
				<div class="modal-footer complaintFooter">
					<button class="btn-u btn-u-red" id="complaint-submit">提交</button>
					<button type="button" class="btn-u btn-u-red" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
</div>




<script type="text/javascript">
	$(function(){
		//每次弹出页面更换验证码
		$('#img_captcha').attr({src : "${ctx}/images/kaptcha.jpg?t=" + Math.random()});
	})

	$("input[name=feedbackType]:eq(0)").attr('checked', 'checked');

	$('#img_captcha').click(function() {
		$(this).attr({
			src : "${ctx}/images/kaptcha.jpg?t=" + Math.random()
		});
	});

	var $tab_li = $('.tab > .tab_menu > li');
	$tab_li.click(function(e) {
		var index = $tab_li.index(this);
		$('.tab_box > div').eq(index).show().siblings().hide();
	});

	$('#modal-complaint .tab_menu > li > label').click(function(e) {
		if ($('#OPINION_TECHNOLOGY').is(':checked')) {
			$('#attachment').removeClass('hide');
		} else {
			$('#attachment input[type=file]').val(null);
			$('#attachment .fileinput-filename').html(null);
			$('#attachment').addClass('hide');
		}
	});

	$('#complaint-submit').click(function() {
		$("#modal-complaint input[name='url']").val(window.location.href);
		$('#suggestion-form').submit();
	});
	
	function saveReport() {
		//验证表单
		if($("#suggestion-form .has-error").length>0){
			return false;
		}
		// jquery 表单提交
		$("#suggestion-form").ajaxSubmit(function(message) {
			$('#modal-complaint').modal('hide');
			common.showMessage(message.content);
		});
		return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转
	} 

	//为表单注册validate函数
	$("#suggestion-form").validate({
		rules : {
			phone : {
				required : true,
				digits : true,
				minlength : 11
			},
			feedbackContent : {
				required : true,
				rangelength : [ 20, 500 ]
			},
			captcha : {
				required : true,
				checkCaptcha : true
			}
		},
		messages : {
			phone : {
				required : "请输入手机号",
				minlength : "请输入正确的手机号"
			},
			feedbackContent : {
				required : "请输入详细内容",
				rangelength : "详细内容应为20~500位字符"
			},
			captcha : {
				required : "请输入验证码"
			}
		}
	});

	$.validator.addMethod("checkCaptcha", function(value, element) {
		var returnMsg = true;
		jQuery.ajax({
			type : "get",
			url : "${ctx}/suggestion/checkCaptcha",
			async : false,
			cache : false,
			data : {
				captcha : function() {
					return $('.captcha1').val();
				}
			},
			dataType : "json",
			success : function(msg) {
				returnMsg = msg;
			}
		});
		return returnMsg;
	}, "验证码错误");
</script>
