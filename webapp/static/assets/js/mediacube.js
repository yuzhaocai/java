var loading = '<img id="img-loading" src="' + ctx +'/static/legacy/img/loading.gif">';
var app = {};

app.showLoading = function (dom) {
	$(dom).attr({disabled: true});
	$(dom).append(loading);
}

app.hideLoading = function () {
	$('#img-loading').parent().attr({disabled: false});
	$('#img-loading').remove();
}

/*
 * 为页面元素增加 disabled 属性.
 */
app.disabled = function(dom) {
	$(dom).attr({disabled: true});
	$(dom).addClass("disabled");
}

/*
 * 取消页面元素的 disabled 属性.
 */
app.enabled = function(dom) {
	$(dom).removeAttr("disabled");
	$(dom).removeClass("disabled");
}

/*
 * 激活菜单项.
 */
var menu = {};
menu.active = function(id) {
	$(id).addClass('list-group-item-info');
}

menu.addActive = function(id) {
	$(id).addClass('active');
}
/**
 * 扩展setTimeout方法,支持传递方法参数
 **/
var __sto = setTimeout;
window.setTimeout = function(callback, timeout, param) {
	var args = Array.prototype.slice.call(arguments,2);
	var _cb = function() {
		callback.apply(null,args);
	}
	__sto(_cb,timeout);
}
//-- 扩展setTimeout方法

/*
 * 更新站内消息的未读数字.
 */
app.loadSiteMessageBadge = function () {
	$.post(ctx + '/member/message/loadBadge', function(data) {
		try {
			var n = parseInt(data);
			if(n==0){
				n="";
			}
			$('.msg-badge').text(n);
		} catch (e) {}
	});
	//20秒内随机刷新
	setTimeout(app.loadSiteMessageBadge,Math.round(Math.random()*20)*1000);
}

$(function() {
	// 修改密码
	if ($('#changePwd').length > 0) {
		$('#changePwd').click(function(){
		    $.get(ctx + '/security/changePassword?ajax', function(data) {
		    	$('#pwd-modal').remove();//从DOM删除已有的对话框
		        $('body').append(data);
		        common.showModal('#pwd-modal', false);
		    });	
		    return false;
		});
	}
	
	// 调整 #content 的最小高度.
	try {
		var h = $(window).height() - $('#header').height() - $('#footer').height();
		$('#content').css('min-height', h);
	} catch (e) {
		common.log(e);
	}
	
});