<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>组织机构管理</title>
</head>

<body>

<div class="panel panel-default">

 	<div class="panel-heading"><!-- 右侧标题 -->
	  <ul class="breadcrumb">
	      <li><span class="glyphicon glyphicon-home"></span> 组织机构管理</li>
	      <li class="active">组织机构列表</li>
	  </ul>
	</div><!-- / 右侧标题 -->
	
	<div class="panel-body"><!-- 右侧主体内容 -->
	
	  <!-- 查询 -->
    <div class="row">
      <div class="col-md-12">
        <form class="form-inline" action="${ctx }/admin/org/list" method="post" >
        	<div class="form-group">
			      <label class="sr-only" for="roleName">组织名称：</label>
			      <input type="text" class="form-control input-sm" id="search_LIKE_name" name="search_LIKE_name" value="${param.search_LIKE_name }" placeholder="按组织名称查询">
		    </div>
		    <div class="form-group">
		      <button type="submit" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-search"></span> 搜索</button>
		    </div>
		    
		      
		    <a class="btn btn-primary btn-sm pull-right" href="${ctx }/admin/org/create"><span class="glyphicon glyphicon-plus"></span> 创建组织</a>

        </form>
      </div>
    </div>
    <!-- / 查询 -->
    
    <br>
    
	  <div class="tab-content">
	    <div class="tab-pane active">
	      <table class="table table-bordered table-condensed table-hover" id="table1">
	        <thead>
	          <tr class="thead">
		          <th class="text-center" width="40">序号</th>
		          <th class="text-left">组织名称</th>
		          <th width="80">联系人</th>
		          <th width="120">电话</th>
		          <th class="text-center" width="130">创建时间</th>
		          <th class="text-center" width="80">状态</th>
		          <th class="text-center" width="120">操作</th>
	          </tr>
	        </thead>
	        <tbody>
	          
				<c:forEach items="${data.content}" var="item" varStatus="stat">
				  <tr>
				    <td class="text-center">${stat.count }</td>
				    <td>${item.name}</td>
				    <td>${item.linkman}</td>
				    <td>${item.telPhone}</td>
				    <td><fmt:formatDate value="${item.createTime }" pattern="yyyy/MM/dd HH:mm:ss" /></td>
				    <td class="text-center"><zy:dic value="${item.status }" /></td>
				    <td class="text-center">
					    <a href="${ctx}/admin/org/${item.id}/edit" id="editLink-${item.id}">修改</a>
					     <span class="cutline"></span>
					    <a href="#" id="delLink-${item.id}" onclick="deleteById('${item.id}', '${item.name }')">删除</a>
				    </td>
				  </tr>
				</c:forEach>
    
	        </tbody>
	      </table>
	      
        <tags:pagination page="${data}" />
	  
	    </div>
	    
	  </div>
	  
	</div><!-- /右侧主体内容 -->

</div>

<form id="action-form" action="" method="post">
</form>
  
  
<script type="text/javascript">

$(function() {
  menu.active('#org-list');
});
	
function deleteById(id, name) {
	var $form = $('#action-form');
	$form.attr('action', '${ctx}/admin/org/' + id + '/delete');
	bootbox.setDefaults({size:'normal'});
	bootbox.confirm('您确定要删除组织『' + name + '』吗？', function(result) {
	  if(result) {
	    $form[0].submit();
	  }
	});
}


</script>

</body>
</html>
