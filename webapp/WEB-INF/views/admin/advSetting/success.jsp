<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>添加成功</title>
</head>
<body>
	<div class="panel panel-default" style="width:300px">
		<div class="panel-body">
			<div class="panel-body"> 
				<div style="font-size:20px;text-align:center">添加成功!</div>
			</div>
			<div class="modal-footer">
				<a class="btn-u btn-u-red" href="${ctx }/admin/advSetting/addAdv" >确定</a>
			</div>
		</div>
	</div>
<script type="text/javascript">
	$(function() {
	    menu.active('#add-adv');
	});
</script>
</body>
</html>
