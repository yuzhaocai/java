//>> --------------------------------- 获取短信验证码

/*
 * 参数说明：
 * btn - 获取验证码按钮的id
 * field - 手机号码输入域的id
 */
app.bindSmsCode = function(btn, field) {
	btn = '#' + btn;
	$(btn).click(function() {
		var mobPhone = $('#'+field).val().trim();
		if( mobPhone.length == 11 ) {
			sendSMS(mobPhone);
		} else {
			showError(field, '请输入正确的手机号码!');
			return;
		}
		
		common.disabled(this);
		var seconds = 60;
		countdown(seconds, btn);
	});
}

/*重新发送短信倒计时*/
function countdown(seconds, btn) {
	if (seconds <= 0) {
		$(btn).text('获取短信验证码');
		common.enabled(btn);
	} else {
		var txt = "(" + seconds + ")秒后重新获取";
		$(btn).text(txt);
		setTimeout(countdown, 1000, --seconds, btn);
	}
}

/*发送验证码*/
function sendSMS(phone) {
	$.get(ctx + '/common/sendSmsCode?mobPhone=' + phone);
}

/*在字段下显示错误消息*/
function showError(id, msg) {
	msg = '<p class="help-block" id="'+id+'-error">' + msg + '</p>'
	var p = $('#'+id).parents('.has-feedback');
	p.find('.help-block').remove();
	p.addClass('has-error').append(msg);
}

//<< --------------------------------- 获取短信验证码