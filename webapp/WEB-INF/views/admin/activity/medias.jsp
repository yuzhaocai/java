<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>
<style type="text/css">
.media{
max-height: 600px !important;
overflow-y : scroll;
}
</style>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">已选择媒体</h4>
</div>

<div class="modal-body media">
	<table
		class="table table-bordered table-condensed table-hover table-photos">
		<thead>
			<tr class="thead">
				<th>名称</th>
				<th>大类</th>
				<th>认证类型</th>
				<th>粉丝数</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${not empty medias}">
				<c:forEach items="${medias}" var="item">
					<tr>
						<td>${item.name }</td>
						<td><zy:dic value="${item.mediaType }" /></td>
						<td><zy:dic value="${item.category }" /></td>
						<td class="tdw-md">${item.fans }</td>
						<td><a data-id="${item.id }" class="dis-selected">删除</a></td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
</div>

<div class="modal-footer">
	<button class="btn-u btn-u-red" type="button" id="btn-close" data-dismiss="modal">关闭</button>
</div>


<script type="text/javascript">
	/* 绑定选择媒体事件 */
	$('.dis-selected').click(function() {
		var id = $(this).data("id");
			var checkbox = $("#"+id)[0];
			if (checkbox.checked) {
				checkbox.checked = false;
				$(checkbox).parents('.thumbnail').removeClass('selected');
				mediaIds.splice( $.inArray(id, mediaIds), 1 );
				$("input[name='mediaIds']").val(mediaIds.join(","));
			}
			updateTotal();
		$(this).closest("tr").remove();
	});
</script>