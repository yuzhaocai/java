<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<%-- 查看改稿申请 --%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">改稿申请</h4>
</div>

<div class="modal-body">
	<form class="form-horizontal">
		<div class="form-group">
			<label class="col-xs-3 control-label">改稿媒体：</label>
			<div class="col-xs-9">
				<p class="help-block" id="media-name">${reqMedia.media.name }</p>
			</div>
		</div>
		<div class="form-group">
			<label class="col-xs-3 control-label">申请理由：</label>
			<div class="col-xs-9">
				<textarea class="form-control" readonly rows="3">${reqMedia.changedReason }</textarea>
			</div>
		</div>
		<div class="form-group">
			<label class="col-xs-3 control-label">修改后稿件：</label>
			<div class="col-xs-9 form-control-static">
				<p>
					<a target="_blank" href="${ctx}/member/req/download/article/${reqMedia.changedArticle}">下载</a>
				</p>
			</div>
		</div>
	</form>
</div>

<div class="modal-footer text-center">
	<button class="btn-u btn-u-default" data-dismiss="modal">关闭</button>
</div>