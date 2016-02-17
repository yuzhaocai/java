<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>


<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="title_yingyao">选择媒体类别</h4>
</div>
<div class="modal-body">
	<div class="form-group">
		<label class="radio-inline"><input type="radio" value="1"
			name="userChoose"> <c:forEach var="media"
				items="${mediaType.dicItems}" varStatus="status">
										 ${media.itemName}
										<c:if
					test="${status.index < fn:length(mediaType.dicItems)-1 }">、</c:if>
			</c:forEach> </label>
	</div>
	<div class="form-group">
		<label class="radio-inline"> <input type="radio" value="0"
			name="userChoose"> 更多媒体( <c:forEach var="categorie"
				items="${categories}" varStatus="status">
										 ${categorie.itemName}
			<c:if test="${status.index < fn:length(categories)-1 }">、</c:if>
			</c:forEach> )
		</label>
	</div>
</div>
<div class="modal-footer">
		<p class="text-center">
					<button id="accept-choose" type="button" class="btn-u btn-u-red w100" >确认</button>
				</p>
</div>

<script type="text/javascript">
	$(function() {
		$("input[name=userChoose]:eq(0)").attr('checked', 'checked');
		$("#accept-choose")
				.click(
						function() {
							var choose = $("input[name='userChoose']:checked")
									.val();
							if (choose == 1) {
								window.location.href = "${ctx}/member/req/advertiser/create/start";
							} else {
								window.location.href = "${ctx}/other";
							}
						});
	});
</script>