<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>订单管理</title>
</head>

<body>
        
  <!-- Main -->
  <div class="main-panel">     

      <div class="headline"><h4>订单管理</h4></div>
      
		<div class="row"><!-- 查询条件 -->
			<div class="col-md-12">
			  
		      <form class="form-inline" action="${ctx }/org/order" method="post">
		      	<div class="form-group">
		          <input type="text" class="form-control input-sm" id="search_LIKE_media.name" name="search_LIKE_media.name"
		          	value="${param['search_LIKE_media.name'] }" placeholder="媒体名称">
		        </div>
		        <div class="form-group">
                   <input type="text" class="form-control input-sm Wdate " id="search_GTE_createTime" name="search_GTE_createTime" value="${param.search_GTE_createTime }" placeholder="开始日期" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'#F{$dp.$D(\'search_LTE_createTime\')||\'%y-%M-%d\'}'})">
		        </div>
		        <div class="form-group">
                   <input type="text" class="form-control input-sm Wdate " id="search_LTE_createTime" name="search_LTE_createTime" value="${param.search_LTE_createTime }" placeholder="结束日期" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})">
		        </div>

		        <div class="form-group">
		          <button type="submit" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-search"></span> 搜索</button>
		        </div>
	        	
		      </form>
			
			</div>
        </div><!-- /查询条件 -->
		<br/>	
		<table id="contentTable" class="table table-bordered table-condensed table-hover">
			<thead class="thead">
			<tr>
				<th class="text-center" width="120">订单号</th>
				<th>需求名称</th>
				<th>接单媒体</th>
				<th class="text-center" width="120">订单金额</th>
				<th class="text-center" width="140">时间</th>
				<th class="text-center" width="80">状 态</th>
			</tr>
			</thead>
			
			<tbody>
			
			<c:forEach items="${data.content }" var="item" varStatus="stat">
				<tr>
					<td class="text-center">${item.id }</td>
					<td ><zy:out value="${item.requirement.name }" len="20" /></td>
					<td ><zy:out value="${item.media.name }" len="20" /></td>
					<td class="text-right"><fmt:formatNumber value="${item.amount }" type="currency" currencyCode="CNY" /></td>
					<td class="text-center"><fmt:formatDate value="${item.createTime }" pattern="yyyy/MM/dd HH:mm" /></td>
					<td class="text-center"><zy:dic value="${item.status }" style="true" />
					</td>
				</tr>
			</c:forEach>

			<c:if test="${empty data.content }">
				<tr>
					<td colspan="5">
					没有搜索到符合条件的记录！
					</td>
				</tr>
			</c:if>
			
			</tbody>	
		</table>
		
		<tags:pagination page="${data}" />
      
</div>
<!-- End Main -->

<script type="text/javascript">
$(function() {
	menu.active('#org-order');
});
</script>
   
</body>
</html>
