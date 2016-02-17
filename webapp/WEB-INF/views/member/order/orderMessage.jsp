<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<%-- 留言板 --%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">订单号:${orderId}</h4>
</div>
<div class="modal-body">
	<div style="padding:0">
		<table width="100%" class="table" style="margin-bottom: 5px">
			<thead>
				<tr>
					<th width="15%">发言人</th>
					<th width="70%">内容</th>
					<th width="15%">时间</th>
				</tr>
			</thead>
		</table>
	</div>
	<div style="max-height:300px; overflow:auto" id="orderMessageCenter">
	<table width="100%" class="table table-striped">
	  <tbody>
	  <c:forEach items="${data }" var="item">
	  <tr>
	    <td width="15%"><zy:dic value="${item.speaker }"/></td>
	    <td width="70%">${item.content }</td>  
	    <td width="15%"><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	  </tr>
	  </c:forEach>
	  <c:if test="${empty data }">
	  <tr class="no-data">
	    <td colspan="3">暂无留言内容</td>
	  </tr>
	  </c:if>
	  </tbody>
	</table>
	</div>
	
</div><!-- /modal-body -->
<div class="modal-footer" id="orderMessageFrame">
	<form id="messageForm" >
      	<input type="hidden" name="orderMessage-action" value="${ctx}/member/order/saveOrderMessage">
      	<input type="hidden" name="orderId" value="${orderId}">
		<div class="form-group has-feedback col-xs-10">
			<textarea class="form-control" placeholder="请输入..."  name="content" id="content"></textarea>
		</div>
		<div class="form-group has-feedback col-xs-2">	
			<button class="btn-u btn-u-red" type="submit" id="btn-send-orderMessage" data-loading-text="正在处理..." style="float:left">发送</button>
		</div>
	</form>	
</div>

<script type="text/javascript">
	$('#messageForm').validate({
		debug: false,
		submitHandler: function(form) {
			messageSubmit();
		}, 
		rules: {
			content: {
				required: true,
				rangelength:[2,200],
				unRegex:/\d{5}/
			}
		},
		messages: {
			content: {
				required: '输入框不允许为空',
				unRegex:'请输入2~200个字符，为保证交易安全，不要留下手机号、QQ号等联系方式。',
				rangelength:'请输入2~200个字符！'
			}
		}
	});	

	var trTemplate = '<tr><td width="15%">{speaker}</td><td width="70%">{content}</td><td width="15%">{createTime}</td></tr>';
	function messageSubmit(){
			var $btn = $('#btn-send-orderMessage');
			$btn.button('loading');
			var url = $('#orderMessageFrame input[name="orderMessage-action"]').val();
			var id = $('#orderMessageFrame input[name="orderId"]').val();
			var content = $('#orderMessageFrame textarea[name="content"]').val();
			var data = {id:id,content:content};
			$.post(url, data, function(rsp) {
				if ( rsp.result ) {
					var $t = $('#orderMessageModal table');
					$t.find('tr.no-data').remove();//删除空行
					$t.find('tbody').append(trTemplate.format(rsp.data));
					$('#orderMessageModal').modal('layout');
				}
				common.showMessage(rsp.content);
				$('#orderMessageFrame textarea[name="content"]').val('');
				$('#orderMessageFrame textarea[name="content"]').focus();
				$btn.button('reset');
			});
	}
</script>
