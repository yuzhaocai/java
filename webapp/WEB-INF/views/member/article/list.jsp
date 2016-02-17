<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>我的广告素材</title>
</head>

<body>
        
  <!-- Main -->
  <div class="main-panel">     

      <div class="headline"><h4>我的广告素材</h4></div>
      
		<div class="row"><!-- 查询条件 -->
			<div class="col-md-12">
			  
		      <form class="form-inline" action="${ctx }/member/article/list" method="post">
		        <div class="form-group">
		          <input type="text" class="form-control input-sm" id="search_LIKE_title" name="search_LIKE_title" 
		          	value="${param.search_LIKE_title }" placeholder="标题">
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
		        
	        	<a class="btn btn-sm btn-primary pull-right" href="${ctx }/member/article/create" ><span class="glyphicon glyphicon-plus"></span> 创建新素材</a>
	        	
		      </form>
			
			</div>
        </div><!-- /查询条件 -->
		<br/>	
		<table id="contentTable" class="table table-bordered table-condensed table-hover">
			<thead class="thead">
			<tr>
				<th class="text-center" width="60">编号</th>
				<th>标题</th>
				<th class="text-center" width="140">创建时间</th>
				<th class="text-center" width="80">状 态</th>
				<th class="text-center" width="130">可见性</th>
				<th class="text-center" width="120">操 作</th>
			</tr>
			</thead>
			
			<tbody>
			
			<c:forEach items="${data.content }" var="item" varStatus="stat">
				<tr>
					<td class="text-center">${stat.count}</td>
					<td><a target="_blank" href="${ctx }/member/article/${item.id}/view"><zy:out value="${item.title }" len="20" /></a></td>
					<td class="text-center"><fmt:formatDate value="${item.createTime }" pattern="yyyy/MM/dd HH:mm" /></td>
					<td class="text-center"><zy:dic value="${item.status }" style="true" /></td>
					<td class="text-center"><zy:dic value="${item.visable }" style="true" /></td>
					<td class="text-center">
						<div class="btn-group btn-group-xs" role="group">
							<button class="btn btn-link btn-delete" data-id="${item.id}" data-title="${item.title }">删除</button>
							<a class="btn btn-link" href="${ctx }/member/article/${item.id}/edit">修改</a>
						<c:if test="${'ARTICLE_V_SHOW' eq item.visable}">
							<a class="btn btn-link" href="${ctx }/member/article/${item.id}/hide">隐藏</a>
						</c:if>
						<c:if test="${'ARTICLE_V_HIDE' eq item.visable}">
							<a class="btn btn-link" href="${ctx }/member/article/${item.id}/show">显示</a>
						</c:if>							
						</div>
					</td>
				</tr>
			</c:forEach>

			<c:if test="${empty data.content }">
				<tr>
					<td colspan="6">
					没有搜索到符合条件的记录！
					</td>
				</tr>
			</c:if>
			
			</tbody>	
		</table>
		
		<tags:pagination page="${data}" />
      
</div>
<!-- End Main -->

<form id="action-form" action="" method="post"></form>

<script type="text/javascript">
$(function() {
	menu.active('#my-article');
	
	$('button.btn-delete').click(function() {
		var $this = $(this);
		bootbox.confirm('即将删除『' + $this.data('title') + '』，请确认！', function(result) {
			if (result) {
				$('#action-form').attr('action', '${ctx}/member/article/' + $this.data('id') + '/delete').submit();
			}
		});
	});
	
});
</script>
   
</body>
</html>
