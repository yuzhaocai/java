<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title">订单处理</h4>
	</div>

	<div class="modal-body">
		<div class="form-group has-feedback">
			<label class="control-label col-xs-2">对接人：</label>
				<div class="input-group col-xs-10">
					<input type="text" class="form-control" id="custManager" name="custManager" maxlength="32" readOnly="readonly" value="${intention.custManager}">
				</div>
		</div>
		<div class="form-group has-feedback">
			<label class="control-label col-xs-2">处理结果：</label>
				<div class="input-group col-xs-10">
					<textarea class="form-control" rows="5" name="handleResult" readOnly="readonly" >${intention.handleResult}</textarea>
				</div>
		</div>
	</div>
