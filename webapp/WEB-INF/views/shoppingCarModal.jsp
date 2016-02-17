<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<c:choose>
<c:when  test="${!empty datas }">
<form id="shopping-form" class="form-inline" action="${ctx}/member/req/advertiser/create/createReq" >
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">采媒车</h4>
</div>
<div class="modal-body">
	<table width="100%" class="table" id="car-table">
	  <tr>
	    <th scope="col">邀约媒体</th>
	    <th scope="col">媒体类型</th>
	    <th scope="col">认证类型</th>
	    <th scope="col">粉丝数</th>
	    <th scope="col">服务类型</th>
	    <th scope="col" class="text-right">价格</th>
	    <th scope="col"></th>
	  </tr>
	  <c:forEach items="${datas }" var="item" varStatus="status">
	  <tr>
	    <td><input type="checkbox" class="quote-select"  name="mediaQuoteId[${status.index}]" value="${item.id }" /> &nbsp  ${item.media.name }</td>
	    <td><zy:dic value="${item.media.mediaType }"/></td>
	    <td><zy:dic value="${item.media.category }"/></td>
	    <td>${item.media.fans }</td>
	    <td><zy:dic value="${item.type }"/></td>
	    <td>${item.price }</td>
	    <td class="text-right">
	    	<input type="button" name="delete" class="media-delete" data-value="${item.id }" value="删除"  />
	    </td>
	  </tr>
	  </c:forEach>
	</table>
</div>
<div class="modal-footer">
	<div class="col-xs-2" style="padding-left: 8px;">
		<input type="checkbox" class="btn-u btn-u-default" style="float:left"  id="selectAll-btn" /><label  class="control-label" style="float:left"> &nbsp  全选</label>
	</div>
	<div class="col-xs-10" >
		<button type="button" class="btn-u btn-u-default"  id="submit-btn">立即邀约</button>
	</div>
</div>
<input type="hidden" name="quoteIds" />
</form>
</c:when>

<c:otherwise>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">采媒车</h4>
</div>
<div class="modal-body">
	<p> 您的采媒车为空，请去添加媒体吧！</p>
</div>
<div class="modal-footer">
	<button type="button" class="btn-u btn-u-default" data-dismiss="modal">关 闭</button>
</div>
</c:otherwise>
</c:choose>

<script type="text/javascript">
	$('#submit-btn').click(function(){
		var quoteIds = new Array();
		$('.quote-select').each(function(){
			if ($(this).is(':checked')){
				quoteIds.push($(this).attr("value"));
			}
		});
		if (quoteIds.length == 0) {
			common.alert("请选择媒体！");
		} else {
			$('#shopping-form').find('[name="quoteIds"]').val(quoteIds);
			$('#shopping-form').submit();
		}
	});
	$('#selectAll-btn').change(function(){
		var that = $(this);
		var isChecked = that.is(':checked');
		$('.quote-select').each(function(){
			$(this)[0].checked=isChecked;
		});
	});
	$('.quote-select').change(function(){
		var selectAll = true;
		$('.quote-select').each(function(){
			if (!$(this).is(':checked')) {
				selectAll = false;
				return false;
			}
		});	
		if (selectAll) {
			$('#selectAll-btn')[0].checked=selectAll;
		} else {
			$('#selectAll-btn')[0].checked=selectAll;
		}
	});
	$('.media-delete').click(function(){
		var queteId = $(this).data("value");
		var that = $(this);
		$.post('${ctx}/shoppingcart/delMediaFromCart', {id:queteId}, function(rsp){
			var opts = {life: 1000};
			common.showMessage(rsp, opts);
			that.parent("td").parent("tr").remove();
			//采媒车里媒体数量
			var carNum = parseInt($('#car-num').text());
			if(carNum==1){
				$('#car-num').text(0);
			}else{
				$('#car-num').text(carNum-1);
			}
			
		});
	});
</script>

