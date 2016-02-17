<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<!doctype html>
<html>
<head>
<title>已处理的需求</title>
</head>

<body>

	<!-- Main -->
	<div class="main-panel">
		<div class="headline">
			<h5>我的订单</h5>
		</div>
		<!-- mTab v2 -->
		<div class="pad mtab-v2">
			<ul class="nav nav-tabs">
				<li><a href="${ctx}/member/order/provider">进行中订单</a></li>
				<li class="active"><a
					href="${ctx}/member/order/provider/finished">已完成订单</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="transactions">
					<div class="topwrap">
						<form class="form-inline">
							<div class="form-group form-group-sm">
								<input class="form-control input-sm Wdate "
									id="search_GTE_createTime" name="search_GTE_createTime"
									value="" placeholder="开始日期"
									onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'#F{$dp.$D(\'search_LTE_createTime\')||\'%y-%M-%d\'}'})"
									type="text">
							</div>
							<div class="form-group form-group-sm">
								<input class="form-control input-sm Wdate "
									id="search_LTE_createTime" name="search_LTE_createTime"
									value="" placeholder="结束日期"
									onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})"
									type="text">
							</div>
							<button class="btn-u btn-u-sm btn-u-dark">
								<i class="fa fa-search"></i> 查询
							</button>
						</form>
					</div>
					<table width="100%" class="table table-bordered table-striped">
						<thead>
							<tr>
                                <th scope="col" width="10%">订单号</th>
                                <th scope="col" width="10%">支付方式</th>
                                <th scope="col" width="15%">需求名称</th>
                                <th scope="col" width="8%">开始时间</th>
                                <th scope="col" width="8%">结束时间</th>
                                <th scope="col" width="15%">接单媒体</th>
                                <th scope="col" width="8%">订单报价</th>
                                <th scope="col" width="8%">状态</th>
                                <th scope="col" width="14%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${data.content }" var="order">
								<tr>
									<td>${order.id }</td>
                                    <td><zy:dic value="${order.paymentType }"/></td>
									<td><a data-id="${order.requirement.id }"
										class="preview-requirement" href="#"><zy:out
												value="${order.requirement.name }" len="15" /></a></td>
									<td><fmt:formatDate
											value="${order.requirement.startTime }"
											pattern="yyyy/MM/dd" /></td>
									<td><fmt:formatDate value="${order.requirement.endTime }"
											pattern="yyyy/MM/dd" /></td>
									<td><a data-id="${order.media.id }" class="preview-media"
										href="#">${order.media.name }</a></td>
									<td><span class="color-orange">${order.amountMedia }</span></td>
									<td><zy:dic value="${order.status }" /></td>
									<td><a href="#" data-toggle="modal" class='jubao' data-target="#jubao" data-id="${order.id }">举报</a> </td>
								</tr>
							</c:forEach>
							<c:if test="${empty data.content }">
								<tr>
									<td colspan="8">没有搜索到符合条件的记录！</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!-- End Tab v2 -->
		<tags:pagination page="${data}" />
	</div>
	<!-- End Main -->
	
	
	
	<!-- 举报 -->
	<div id="jubao" class="modal fade" tabindex="-1" role="dialog" data-width="640" aria-labelledby="myModalLabel" data-replace="true">
	    <form id="jubao-form">
	      <input type="hidden" name="id">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">举报理由</h4>
	      </div>
	      <div class="modal-body has-feedback">
	      		<textarea class="form-control" placeholder="请输入举报理由..." name="reason" id="reason"></textarea>
	      </div>
	      <div class="modal-footer">
	      	<button class="btn-u btn-u-red" type="submit" id="btn-jubao-ok">确定</button>
	      </div>
	    </form>
	</div>

	<!-- 需求详情 -->
	<div class="modal fade" id="requirement-view" tabindex="-1"
		role="dialog" data-width="700" data-replace="true"></div>

	<!-- 媒体详情 -->
	<div class="modal fade" id="media-view" tabindex="-1" role="dialog"
		data-width="900" data-replace="true"></div>
		
		
	<script type="text/javascript">
$(function() {
	menu.active('#provider-order');
	
	common.showRequirement(".preview-requirement");
	common.showMedia(".preview-media");
	
	//举报
	var $jubaoModal = $('#jubao');
	var $jubaoForm = $('#jubao-form');
	
	function resetJubaoForm() {
		$jubaoForm.find('.has-error').each(function() {
			$(this).removeClass('has-error');
			$(this).find('.help-block').remove();
		});
		$jubaoForm.find('[name=reason]').val('');
	}
	
	$('.jubao').click(function(){
		resetJubaoForm();
		var id = $(this).data('id');
		$('#jubao-form input[name="id"]').val(id);
		$jubaoModal.showModal();
	});
	
	$('#jubao-form').validate({
		submitHandler: function(form) {
			common.disabled('#btn-jubao-ok');
			var params = $(form).serialize();
			$.post('${ctx}/member/order/provider/complaint?ajax', params, function(data) {
				$jubaoModal.hideModal();
				common.enabled('#btn-jubao-ok');
				
				data = $.parseJSON(data);
				if( data.result == 0 ) {
					bootbox.alert("您已成功提交举报信息，请匆重复操作！");
				} else {
					bootbox.alert("您已成功提交举报信息！");
				}
			});
		}, 
		rules: {
			reason: {
				required: true,
				rangelength:[10, 300] 
			}
		},
		messages: {
			reason: {
				required: '请输入举报理由',
				rangelength: '请认真填写举报理由，以方便客户快速处理'
			}
		}
	});
	///举报
	
});
</script>

</body>
</html>
