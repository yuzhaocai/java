<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>黑名单管理</title>
</head>

<body>

<div class="panel panel-default">

 	<div class="panel-heading"><!-- 右侧标题 -->
	  <ul class="breadcrumb">
	      <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
	      <li class="active">黑名单管理</li>
	  </ul>
	</div><!-- / 右侧标题 -->
	
	<div class="panel-body"><!-- 右侧主体内容 -->
	
	
    <div class="row">
      <div class="col-md-12">
        <form class="form-inline" >
        	<div class="form-group">
			      <label class="sr-only" for="roleName">条目：</label>
			      <input type="text" class="form-control input-sm" id="search_LIKE_item" name="search_LIKE_item" value="${param.search_LIKE_item }" placeholder="按名称查询">
			    </div>
        	<div class="form-group">
			      <label class="sr-only">类型：</label>
			      <select name="search_EQ_type">
			      	<c:forEach items="${types }" var="item">
			      		<option value="${item.itemCode }">${item.itemName }</option>
			      	</c:forEach>
			      </select>
			    </div>
			    <div class="form-group">
			      <button type="submit" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search"></span> 搜索</button>
			    </div>
        </form>
      </div>
    </div>
	     
    <div class="row"><!-- 操作按钮组 -->
      <div class="col-md-12">
	      <div class="btn-group-sm pull-right mtb10">
	        <a class="btn btn-primary" href="${ctx }/admin/blacklist/create"><span class="glyphicon glyphicon-plus"></span> 创建新条目</a>
	      </div>
      </div>
    </div><!-- /操作按钮组 -->
    
	  <div class="tab-content">
	    <div class="tab-pane active">
	      <table class="table table-bordered table-condensed table-hover" id="table1">
	        <thead>
	          <tr class="thead">          
	          <th>序号</th>
	          <th>条目</th>
	          <th>类型</th>
	          <th class="text-right">操作</th>
	          </tr>
	        </thead>
	        <tbody>
				<c:forEach items="${data.content}" var="item" varStatus="stat">
				  <tr>
				    <td>${stat.count}</td>
				    <td>${item.item}</td>
				    <td><zy:dic value="${item.type}"/></td>
				    <td class="text-right">
					    <a class="del-link" data-id="${item.id }">删除</a>
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

<form id="actionForm" action="" method="post"></form>
  
  
<script type="text/javascript">

$(function() {
	//menu.active('#role-man');
	
	var $form = $('#actionForm');
	$('.del-link').click(function() {
		var id = $(this).data('id');
		$form.attr('action', '${ctx }/admin/blacklist/delete/' + id);
		//bootbox.setDefaults({size:'large'});
		common.confirm('警告！', '您确定要删除条目吗？', function(result) {
			if(result) {
			  $form[0].submit();
			}
		});
	
		return false;
	});
});

</script>

</body>
</html>
