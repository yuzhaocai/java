<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>所辖媒体</title>
</head>

<body>
        
  <!-- Main -->
  <div class="main-panel">

      	<div class="headline"><h4>所辖媒体</h4></div>
      
		<div class="row"><!-- 查询条件 -->
			<div class="col-md-12">
			  
		      <form class="form-inline" action="${ctx }/org/media" method="post">
		      
		        <div class="form-group">
		          <input type="text" class="form-control input-sm" id="search_LIKE_name" name="search_LIKE_name"
		          	value="${param.search_LIKE_name }" placeholder="媒体名称">
		        </div>
		        <div class="form-group">
                   <input type="text" class="form-control input-sm Wdate " id="search_GTE_createTime" name="search_GTE_createTime" value="${param.search_GTE_createTime }" placeholder="注册日期-开始" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'#F{$dp.$D(\'search_LTE_createTime\')||\'%y-%M-%d\'}'})">
		        </div>
		        <div class="form-group">
                   <input type="text" class="form-control input-sm Wdate " id="search_LTE_createTime" name="search_LTE_createTime" value="${param.search_LTE_createTime }" placeholder="注册日期-结束" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})">
		        </div>

		        <div class="form-group">
		          <button type="submit" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-search"></span> 搜索</button>
		        </div>
		        	        	
		      </form>
			
			</div>
        </div><!-- /查询条件 -->
        
		<br/>	
		
		<table class="table table-bordered table-condensed table-hover">
			<thead class="thead">
			<tr>
				<th>媒体名称</th>
				<th class="text-center" width="30%">入驻时间</th>
				<th class="text-center" width="40%">操作</th>
			</tr>
			</thead>
			
			<tbody>
			
			<c:forEach items="${data.content }" var="item" varStatus="stat">
				<tr>
					<td>${item.name }</td>
					<td class="text-center"><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd" /></td>
					<td>
						<a class="mediaDetail" data-id="${item.id }">详情</a>
						<a class="deregulation" data-id="${item.id }" data-name="${item.name }">解除监管</a>
						<a class="userInfo" data-id="${item.id }">用户信息</a>
						<a class="orderRecord"  href="${ctx }/org/mediaOperation/orderRecord?id=${item.id }&mediaName=${item.name }">订单记录</a>
						<a class="transaction"  href="${ctx }/org/mediaOperation/transaction?id=${item.id }&mediaName=${item.name }">资金流水记录</a>
					</td>
				</tr>
			</c:forEach>

			<c:if test="${empty data.content }">
				<tr>
					<td colspan="4">
					没有搜索到符合条件的记录！
					</td>
				</tr>
			</c:if>
			
			</tbody>	
		</table>
		
		<tags:pagination page="${data}" />
      
</div>
<!-- End Main -->
<!-- 媒体详情 -->
<div class="modal fade" id="media-view" tabindex="-1" role="dialog" data-width="900" data-replace="true" data-backdrop="static"></div>

<!-- 用户信息 -->
<div class="modal fade" id="media-user" tabindex="-1" role="dialog" data-width="900" data-replace="true" data-backdrop="static"></div>
<script type="text/javascript">
$(function() {
	menu.active('#org-media');
	
	//媒体详情
	$('.mediaDetail').click(function() {
		$('#media-view').loadModal('${ctx}/org/mediaOperation/detail?ajax', {id : $(this).attr("data-id")});
	});
	
	//用户信息
	$('.userInfo').click(function() {
		$('#media-user').loadModal('${ctx}/org/mediaOperation/userInfo?ajax', {id : $(this).attr("data-id")});
	});
	
	//解除监管
	$('.deregulation').click(function() {
		var $this = $(this);
		var name = $this.data('name');
		bootbox.confirm("您确定要解除媒体： <span style='color:red'>" + name+ "</span> 的监管吗？",function(result) {
			if (result) {
				$.post('${ctx}/org/mediaOperation/deregulation?ajax',{mediaId:$this.data('id'),organizationId:'${organizationId }'},function(data){
					window.location.reload();
				});
			}
		});
	});
});
</script>
   
</body>
</html>
