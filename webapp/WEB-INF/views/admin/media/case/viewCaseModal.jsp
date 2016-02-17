<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

	<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title">图片详情</h4>
		</div>
		<div class="modal-body">
			<div class="form-group">
			<c:forEach items="${pics }" var="pic">
				<img  src="<zy:fileServerUrl value="${pic}"/>" width="330px" height="250px">
			</c:forEach>
			</div>
			<div class="modal-footer">
				<button class="btn btn-default" type="button" data-dismiss="modal"
					aria-label="Close">关闭</button>
			</div>
		</div>
	
