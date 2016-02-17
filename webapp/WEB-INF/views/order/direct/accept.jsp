<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>
<style type="text/css">
.text-muted {
	font-size:16px;
	font-weight: bold;
}
</style>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="title_yingyao">选择下单类别</h4>
</div>
<div class="modal-body">
	<div class="form-group">
		<label class="radio-inline">
			<div class="col-md-12">
		<input type="radio" value="1"
			name="userChoose">
				<span class="text-muted">接单</span>
<p class="pre">
将向您的“会员中心-待处理需求”跳转，点击“应邀”就可接单成功。
快去处理广告主邀请您参加的需求吧！</p>
			</div> </label>
	</div>
	<div class="form-group">
		<label class="radio-inline">
			<div class="col-md-12">
		 <input type="radio" value="0"
			name="userChoose">
				<span class="text-muted">抢单</span>
<p class="pre">
没有广告投放需求邀请您参加？不要紧。
选择抢单将向“广告悬赏”频道跳转，在这里有海量广告投放需求等您抢！</p>
			</div>

		</label>
	</div>
</div>
<div class="modal-footer">
		<p class="text-center">
					<button id="send-choose" type="button" class="btn-u btn-u-red w100" >确认</button>
				</p>
</div>

<script type="text/javascript">
	$(function() {
		$("input[name=userChoose]:eq(0)").attr('checked', 'checked');

		$("#send-choose").click(function() {
			var choose = $("input[name='userChoose']:checked").val();
			if (choose == 1) {
				window.location.href = "${ctx}/member/req/deal";
			} else {
				window.location.href = "${ctx}/plaza";
			}
		});
	});
</script>