<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>会员中心</title>
</head>

<body>
	<div class="main-panel deposit-panel">            
    <!--strat user-info  -->
		<div class="user-info">
			<div class="row">
				<div class="col-xs-6">
					<div class="info-lcol">
						<div class="u-pic">
							<img src="${ctx}/static/assets/img/user.jpg">
							<div class="mask"></div>
						</div>
						<div class="info-m">
							<div class="row">
								<div class="col-lg-12 u-name">
										<c:if test="${empty cust.linkman }">
											${cust.mobPhone }
										</c:if>
										<c:if test="${!empty cust.linkman }">${cust.linkman }</c:if>
								</div>
							</div>
							<div class="row text-muted">
								<div class="col-xs-6">
									会员类型：<span class="color-red"><zy:dic
											value="${cust.custType }" /></span>
								</div>
								<div class="col-xs-6">
									实名认证：
									<c:if test="${cust.certStatus eq 'CERT_S_NULL' }">
										<a href="${ctx}/member/verify?custId=${cust.id}" class="btn-link">申请实名认证</a>
									</c:if>
									<c:if test="${cust.certStatus eq 'CERT_S_AUDIT' }">
										<span class="color-dark"><a href="${ctx}/member/toCertification?custId=${cust.id}" class="btn-link">认证中</a></span>
										<div class="progress progress-xs margin-bottom-5">
											<div class="progress-bar progress-bar-striped active"
												role="progressbar" aria-valuenow="90" aria-valuemin="0"
												aria-valuemax="100" style="width: 60%">
												<span class="sr-only">50% Complete</span>
											</div>
										</div>
									</c:if>
									<c:if test="${cust.certStatus eq 'CERT_S_PASS' }">
										<span class="color-dark"><img
											src="${ctx }/static/assets/img/renzheng.png"><a href="${ctx}/member/toCertification?custId=${cust.id}" class="btn-link">已认证</a></span>
									</c:if>
									<c:if test="${cust.certStatus eq 'CERT_S_UNPASS' }">
										<a href="${ctx}/member/verify?custId=${cust.id}" class="btn-link">未通过，再申请</a>
									</c:if>
								</div>
							</div>
							<div class="row text-muted">
								<div class="col-xs-6">
									级别：<i class="emblem emblem-m emblem-m-1" data-toggle="tooltip"
										title="Lv.1">${cust.custLevel.name }</i>
								</div>
								<div class="col-xs-6">
									信用值：<span class="color-red"><fmt:formatNumber
											value="${cust.credit }" /></span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-xs-6">
					<div class="info-rcol">
						<ul class="tqico-list">
							<li>特权：</li>
							<li
								<c:if test="${cust.custLevel.noAudit == '1' }">class="has"</c:if>><i>审</i>信息免审</li>
							<li
								<c:if test="${cust.custLevel.redName == '1' }">class="has"</c:if>><i>名</i>红名特权</li>
							<li
								<c:if test="${cust.custLevel.priority == '1' }">class="has"</c:if>><i>优</i>信息优先显示</li>
							<li
								<c:if test="${cust.custLevel.discount == '1' }">class="has"</c:if>><i>9<small>折</small></i>佣金9折</li>
						</ul>
						<div class="u-link">
							<a class="btn btn-link" target="_blank" href="${ctx }/helpCenter/memberLevel">什么是信用值？</a>
							<a class="btn btn-link" target="_blank" href="${ctx }/helpCenter/memberLevel#tq">什么是特权？</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- end user-info -->
            <div class="headline"><h5>我的信息</h5></div>
            <div class="panel pad pd15">
            	<div id="div1" class="panel-body">
            		
            		<%-- 显示后台验证错误的标签 --%>
					<tags:fieldErrors commandName="vo" />
            	
                	<form id="form1" class="form-horizontal" action="${ctx }/member/updateUserInfo"
                		method="post" style="width:600px; margin-left:100px;">
                		
                		<input type="hidden" name="id" value="${cust.id }"/>
                		<c:if test="${cust.custType eq 'CUST_T_ADVERTISER' }">
                        <div class="form-group">
                            <label class="col-xs-4 control-label"><span class="color-red">*</span>客户名称</label>
                            <div class="col-xs-6 has-feedback">
                                    <input class="form-control" type="text" name="name" id="name" value="${cust.name }" readonly="readonly">
                                    <input type="hidden" id="oldName" value="${cust.name }" readonly="readonly">
                            </div>
                        </div>
                		</c:if>          
                        
                        <div class="form-group">
                            <label class="col-xs-4 control-label"><span class="color-red">*</span>联系人</label>
                            <div class="col-xs-6 has-feedback">
                                    <input class="form-control" type="text" name="linkman" id="linkman" value="${cust.linkman }" readonly="readonly">
                            </div>
                        </div>
                        <div class="form-group ">
                            <label class="col-xs-4 control-label"><span class="color-red">*</span>手机号</label>
                            <div class="col-xs-6 has-feedback">
                                <input class="form-control disabled" type="text" name="mobPhone" id="mobPhone" readonly="readonly" value="${cust.mobPhone }" >                      
                            </div>
                            <div class="col-xs-2"><button type="button" class="btn btn-link" id="btn-change-phone" style="display:none">修改</button></div>
                        </div>

                        <div class="form-group hide" id="smscode-group">
                            <label class="col-xs-4 control-label"><span class="color-red">*</span>验证码</label>
                            <div class="col-xs-6 has-feedback">
			                    <div class="input-group">
			                        <input id="smscode" name="smscode" class="form-control" placeholder="输入短信验证码" 
			                        	type="text" maxlength="6" >
			                        <span class="input-group-btn">
			                            <button type="button" class="btn btn-default" id="btn-smscode">获取短信验证码</button>
			                        </span>
			                    </div>
			                </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-xs-4 control-label"><span class="color-red">*</span>邮箱</label>
                            <div class="col-xs-6 has-feedback">
                                <input class="form-control" type="email" name="email" id="email" value="${cust.email }" readonly="readonly">                      
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-4 control-label"><span class="color-red">*</span>QQ</label>
                            <div class="col-xs-6 has-feedback">
                                <input class="form-control" type="text" name="qq" id="qq" value="${cust.qq }" readonly="readonly">                      
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-4 control-label"></label>
						<div class="col-xs-6">
								<button type="submit" class="btn-u btn-u-lg btn-u-red w150 pull-right"
									id="btn-submit" style="display: none">保存</button>
								<span class="btn-u btn-u-lg btn-u-red btn-block w150"
									style="text-align: center" id="btn-edit">修改</span>
						</div>
					</div>
                    </form>
                        
                </div>
            </div>
        </div>
<script type="text/javascript">
$(function() {
	menu.active('#user-info');
	
	//>> --------------------------------- 表单验证规则
	$('#form1').validate({
		debug: false,
		submitHandler: function(form) {
			common.disabled('#btn-smscode');
			form.submit();
		}, 
		errorPlacement: function(error, element) { 
		   if($(element).attr('name')=='smscode'){
			   error.appendTo(element.parent().parent()); 
		   }else{
			   error.appendTo(element.parent());  
		   }
// 		   if($(element).attr('name')=='mobPhone'&&!whetherInvalid(element)&&$('#btn-smscode').text()=='获取短信验证码'){
// 			   $('#btn-smscode').attr({disabled: false});
// 			   $('#btn-smscode').removeClass("disabled");
// 		   }else{
// 			   common.disabled('#btn-smscode');
// 		   }
		},
		rules: {
			name: {
				required: true, 
				rangelength: [2,30],
				checkCustNameEdit : true
			},
			linkman: {
				required: true, 
				rangelength: [2,12]
			},
			email: {
				required: true, 
				rangelength: [6,40],
				email:true
			},
			qq: {
				required: true, 
				rangelength: [5,13],
				digits:true
			},
			mobPhone: {
				required: true,
				digits:true,
				rangelength: [11,11],
				remote: {
					depends: function() {
						return !('${cust.mobPhone}' == $('#mobPhone').val())
					},
					param: '${ctx}/common/checkMobPhone'
				}
			},
			smscode: {
				required: {
					depends: function() {
						return $('#mobPhone').data('modifyFlag');
					}
				},
				digits:true,
				minlength:6,
				remote: {
					url: '${ctx}/common/checkSmscode',
					type:'post',
					data: {
						mobPhone: function() {
							return $( "#mobPhone" ).val();
						},
						smscode: function() {
							return $( "#smscode" ).val();
						}
					}
				 }
			}
		},
		
		messages: {
			name: {
				rangelength: '请输入2~30个字符'
			},
			linkman: {
				rangelength: '请输入2~12个字符'
			},
			mobPhone: {
				remote: '此手机号码已经被注册！',
				rangelength: '请输入正确的手机号'
			},
			email: {
				rangelength: '请输入正确的邮箱地址',
			},
			qq: {
				rangelength: '请输入正确的QQ号',
			},
			smscode: {
				remote: '短信验证码不正确！'
			}
		}
	});	
	//<< --------------------------------- 表单验证规则
	
	//绑定短信验证码组件
	app.bindSmsCode('btn-smscode', 'mobPhone');
	
	// 点击手机号码后的“修改”
	$('#btn-change-phone').click(function() {
		//common.log('click change mobPhone');
		$('#mobPhone').removeAttr('readonly');
		$('#mobPhone').data('modifyFlag', true);
		$('#smscode-group').removeClass('hide');
		$(this).off('click');
	});
	//编辑按钮
	$('#btn-edit').click(function() {
		$(this).hide();
		$('#btn-change-phone').show();
		$('#btn-submit').show();
		$('#name').removeAttr('readonly');
		$('#linkman').removeAttr('readonly');
		$('#email').removeAttr('readonly');
		$('#qq').removeAttr('readonly');
	});
});

function whetherInvalid(element){
	var validate= $(element).parent().attr('class').split(' ');
	for(var i=0;i<validate.length;i++){
		if(validate[i]=='has-error'){
			return true;
		}
	}
	return false;
}


$.validator.addMethod("checkCustNameEdit", function(value, element) {
	var returnMsg = true;
	jQuery.ajax({
		type : "get",
		url : "${ctx}/member/checkCustNameEdit",
		async : false,
		cache : false,
		data : {
			name : function() {
				return $('#name').val();
			},
			oldName: function(){
				return $('#oldName').val();
			}
		},
		dataType : "json",
		success : function(msg) {
				returnMsg = msg;
		}
	});
	return returnMsg;
}, "该名称已被注册");
</script>

</body>
</html>