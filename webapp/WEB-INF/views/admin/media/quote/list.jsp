<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>修改媒体报价</title>
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统管理 </li>
        <li>媒体管理</li>
        <li>修改结算价 </li>
        <li class="active">${media.name }</li>
    </ul>
  </div><!-- / 右侧标题 -->
  
  <div class="panel-body"><!-- 右侧主体内容 -->
	<p class="text-right">
	<c:if test="${not empty quotes}">
		<label class="checkbox-inline">
			<input name="provideInvoice" type="checkbox" <c:if test="${media.provideInvoice}">checked="checked"</c:if> onclick="provideInvoice('${media.id }')"/>
				提供发票
			<c:if test="${auditInvoice }">
					<span style="color:red">(正在审核中，请留意状态变更。)</span>
			</c:if>
		</label>
	</c:if>
		<a class="btn btn-primary createQuote" href="#" data-toggle="modal"
			data-target="#createQuote"><i class="fa fa-plus"></i>创建新报价</a>&nbsp;&nbsp;&nbsp;
		<a class="btn btn-default" href="${ctx }/admin/media/media"><i class="fa fa-angle-left"></i>返回</a>
	</p>
  	<div class="pad">
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th scope="col" width="33%">报价类型</th>
						<th scope="col" width="33%">价格</th>
						<th scope="col" width="34%">操作</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="quote" items="${quotes}">
						<tr>
							<td><zy:dic value="${quote.type }" /></td>
							<td>${quote.priceMedia }</td>
							<td class="manage">
								<button class="btn btn-primary btn-sm org-edit" data-toggle="modal"
								data-target="#editQuote"
								onclick="editQuote('${quote.id}','${quote.type }','${quote.priceMedia}')">
									<i class="fa fa-edit"></i>
									修改
								</button>
								<button class="btn btn-danger btn-sm org-delete" onclick="deleteMediaQuote('${quote.id}','${media.id }')">
									<i class="fa fa-remove"></i>
									删除
								</button>
							</td>
						</tr>
						<input type="hidden" value="${fn:length(quotes) } " class="quotesLength"/>
					</c:forEach>
					<c:if test="${fn:length(quotes) lt 1}">
						<tr>
							<td colspan="3">该媒体暂无报价！</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
	

  </div>
	
</div>

<!--新增报价 -->
	<div class="modal fade" id="createQuote" tabindex="-1" role="dialog"
		data-width="700" aria-labelledby="myModalLabel" data-replace="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title">新增报价</h4>
		</div>
		<div class="modal-body">
			<form id="form-c-quote" action="${ctx}/admin/audit/media/quote/save"
				method="post">
				<zy:token />
				<input type="hidden" value="${media.id }" name="mediaId">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label class="form-control-static control-label col-sm-2"><span
								class="color-red">*</span>报价类型：</label>
							<div class="col-sm-4 has-feedback">
								<c:if test="${media.mediaType eq 'MEDIA_T_WEIXIN' }">
									<select class="form-control select_save" name="type">
										<c:forEach var="weixinService"
											items="${weixinService.dicItems}">
											<option
												<c:forEach var="quote" items="${quotes}">
									<c:if test="${weixinService.itemCode eq quote.type}">disabled='disabled'</c:if>
									</c:forEach>
												value="${weixinService.itemCode}">${weixinService.itemName}</option>
										</c:forEach>
									</select>
								</c:if>

								<c:if test="${media.mediaType eq 'MEDIA_T_WEIBO' }">
									<select class="form-control select_save" name="type">
										<c:forEach var="weiboService" items="${weiboService.dicItems}">
											<option
												<c:forEach var="quote" items="${quotes}">
									<c:if test="${weiboService.itemCode eq quote.type}">disabled='disabled'</c:if>
									</c:forEach>
												value="${weiboService.itemCode}">${weiboService.itemName}</option>
										</c:forEach>
									</select>
								</c:if>
							</div>
							<label class="form-control-static control-label col-sm-2"><span
								class="color-red">*</span>价格：</label>
							<div class="col-sm-4 has-feedback">
								<div class="input-group">
									<input maxlength="8" name="price" class="form-control" id="_quote_price">
									<span class="input-group-addon">元</span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
			<div class="modal-footer">
				<button class="btn btn-primary" type="button" id="btn-c-quote"><span class="glyphicon glyphicon-ok"></span>保存</button>
			</div>
		</div>
	</div>


	<!--修改报价 -->
	<div class="modal fade" id="editQuote" tabindex="-1" role="dialog"
		data-width="700" aria-labelledby="myModalLabel" data-replace="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title">修改报价</h4>
		</div>
		<div class="modal-body">
			<form id="form-e-quote" action="${ctx}/admin/audit/media/quote/save"
				method="post">
				<zy:token />
				<input type="hidden" value="${media.id }" name="mediaId"> <input
					type="hidden" name="id" id="quoteId">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label class="form-control-static control-label col-sm-2"><span
								class="color-red">*</span>报价类型：</label>
							<div class="col-sm-4 has-feedback">
								<c:if test="${media.mediaType eq 'MEDIA_T_WEIXIN' }">
									<select class="form-control" name="type">
										<c:forEach var="weixinService"
											items="${weixinService.dicItems}">
											<option
												<c:forEach var="quote" items="${quotes}">
											<c:if test="${weixinService.itemCode eq quote.type}">disabled='disabled' id='${quote.id}_option'</c:if>
										</c:forEach>
												value="${weixinService.itemCode}">${weixinService.itemName}</option>
										</c:forEach>
									</select>
								</c:if>

								<c:if test="${media.mediaType eq 'MEDIA_T_WEIBO' }">
									<select class="form-control" name="type">
										<c:forEach var="weiboService" items="${weiboService.dicItems}">
											<option
												<c:forEach var="quote" items="${quotes}">
									<c:if test="${weiboService.itemCode eq quote.type}">disabled='disabled' id='${quote.id}_option'</c:if>
									</c:forEach>
												value="${weiboService.itemCode}">${weiboService.itemName}</option>
										</c:forEach>
									</select>
								</c:if>
							</div>
							<label class="form-control-static control-label col-sm-2"><span
								class="color-red">*</span>价格：</label>
							<div class="col-sm-4 has-feedback">
								<div class="input-group">
									<input maxlength="8" name="price" class="form-control" id="quote_price">
									<span class="input-group-addon">元</span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
			<div class="modal-footer">
				<button class="btn btn-primary" type="button" id="btn-e-quote"><span class="glyphicon glyphicon-ok"></span>确定修改</button>
			</div>
		</div>
	</div>


<script type="text/javascript">
$(function() {
	menu.active('#media-media');
	$('#_quote_price').val('');
	var quoteLength = $('.quotesLength').val();
	if (quoteLength >= 7){
		common.disabled('.createQuote');
	}
});


$('.createQuote').click(function() {
	$('#_quote_price').val('');
	$('#_quote_price-error').parent().removeClass('has-error');
	$('#_quote_price').parent().parent().removeClass('has-success');
	$('#_quote_price-error').remove();
});

/**
新增媒体报价
 */
$('#btn-c-quote').click(function() {
	$('#form-c-quote').submit();
});

/**
修改媒体报价
 */
$('#btn-e-quote').click(function() {
	$('#form-e-quote').submit();
});

/**
	删除媒体报价
 */
function deleteMediaQuote(quoteId, mediaId) {
	bootbox
			.confirm(
					"您确定删除该媒体报价吗？",
					function(result) {
						if (result) {
							window.location.href = "${ctx}/admin/audit/media/quote/delete?quoteId="
									+ quoteId + "&mediaId=" + mediaId;
						}
					});
}

/**
新增报价效验
 */
$('#form-c-quote').validate({
	debug : false,
	submitHandler : function(form) {
		common.disabled('#btn-c-quote');
		form.submit();
	},
	rules : {
		price : {
			required : true,
			digits : true,
			rangelength:[2, 8],
			checkQuote : true
		}
	},
	messages : {
		price : {
			required : '请填写媒体报价',
			digits : '请输入合法的整数',
			rangelength:'请输入2~8个数字'
		}
	}
});

/**
修改报价效验
 */
$('#form-e-quote').validate({
	debug : false,
	submitHandler : function(form) {
		common.disabled('#btn-e-quote');
		form.submit();
	},
	rules : {
		price : {
			required : true,
			digits : true,
			rangelength:[2, 8],
			checkQuote : true
		}
	},
	messages : {
		price : {
			required : '请填写媒体报价',
			digits : '请输入合法的整数',
			rangelength:'请输入2~8个数字'
		}
	}
});

/**
修改报价
 */
function editQuote(quoteId, quoteType, quotePrice) {
	$('#quoteId').val(quoteId);
	$('#' + quoteId + '_option').removeAttr('disabled');
	$('#' + quoteId + '_option').attr('selected', 'selected');
	$('input[name=price]').val(quotePrice);
}




$.validator.addMethod("checkQuote", function(value, element) {
	var returnMsg = false;
	var re = /^[1-9]\d*$/; 
	if(re.test(value)){
		returnMsg = true;				
	}
	return returnMsg;
}, "请输入合法的正整数");


/**
修改发票
 */
function provideInvoice(mediaId){
	var Invoice = '${auditInvoice }';
	if(Invoice){
		$('input[name=provideInvoice]')[0].checked = !$('input[name=provideInvoice]')[0].checked;
		opts = {life: 3000,theme:'warning'};
		common.showMessage("审核中不能更改", {life: 2000,theme:'warning'});
	}else{
	bootbox.confirm("您确认修改发票状态吗？",function(result) {
		if (result) {
			var provideInvoice='false';
			if($('input[name=provideInvoice]')[0].checked){
				provideInvoice='true';
			}
			$.post("${ctx}/admin/mediaQuote/adminSetInvoice",{provideInvoice:provideInvoice,mediaId:mediaId},function(rsp){
				var opts;
				if(!rsp.result){
					$('input[name=provideInvoice]')[0].checked = !$('input[name=provideInvoice]')[0].checked;
					opts = {life: 3000,theme:'warning'};
				}else{
					opts = {life: 3000};
					location.reload();
				}
				common.showMessage(rsp.content, opts);
			});
		}else{
			//点击取消按钮，将checkbox选中状态返回
			$('input[name=provideInvoice]')[0].checked = !$('input[name=provideInvoice]')[0].checked;
		}
	});
	}
}
</script>
</body>
</html>
