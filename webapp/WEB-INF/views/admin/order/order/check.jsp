<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<form id="yanshou-form" action="${ctx}/member/order/advertiser/acceptance" method="post">
	<input type="hidden" name="id" value="${param.id }">

	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title">订单验收</h4>
	</div>

	<div class="modal-body">
		<c:if test="${!empty deliverable }">
			<div class="row">
				<div class="col-xs-6">
					需求名称：${deliverable.order.requirement.name }
				</div>
				<div class="col-xs-6">
					接单媒体：${deliverable.order.media.name }
				</div>
			</div>
			<br>
			<div class="form-group">
				<label for="url">订单交付链接:</label>
				<div class="input-group">
					<input type="url" id="url" class="form-control"
						value="${deliverable.url }" readonly="readonly"
						style="background: #FFF"> <span class="input-group-btn"><button
							class="btn btn-default" type="button" id="viewUrl">查看</button></span>
				</div>
			</div>
			<div class="form-group">
				<label for="url">订单交付截图:</label>
				<div class="row img-group">
					<c:forEach items="${pics }" var="pic" varStatus="status">
						<div class="col-xs-4">
							<img src="${pic }" class="img-bordered" id="pic${status.index }"
								onclick="view('${pic}');" style="cursor:pointer;">
						</div>
					</c:forEach>
				</div>
			</div>
		</c:if>
	</div>

	<div class="modal-footer">
		<button class="btn-u btn-u-red" type="button" data-dismiss="modal"
			aria-label="Close">关闭</button>
	</div>
</form>


<!--查看案例模态框 -->
<div class="modal fade" id="viewCheckModal" tabindex="-1" role="dialog"
	data-width="700" data-height="600" aria-labelledby="myModalLabel"
	data-replace="true"></div>


<script type="text/javascript">
	$('#viewUrl').click(function() {
		var urlValue = $('#url').val();
		window.open(urlValue, "_blank");
	});

	function view(pic) {
		$yanshouModal = $('#chakan');
		$('#viewCheckModal').loadModalNow(
				'${ctx}/admin/order/order/view?ajax', {
					pic : pic
				},$yanshouModal);
	}
</script>
