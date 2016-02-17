<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">订单交付详情</h4>
</div>
<div class="modal-body">
	<div class="form-group">
		<img src="<zy:fileServerUrl value="${pic}"/>" width="660px"
			height="450px">
	</div>
	<div class="modal-footer">
		<button class="btn-u btn-u-red" type="button" data-dismiss="modal"
			aria-label="Close">关闭</button>
	</div>
</div>

