<%@page import="com.lczy.media.util.UserContext"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>系统管理</title>
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
        <li class="active">用户管理</li>
    </ul>
  </div><!-- / 右侧标题 -->

  <div class="panel-body"><!-- 右侧主体内容 -->
  	
		<div class="row"><!-- 查询条件 -->
			<div class="col-md-10">
			  
		      <form class="form-inline" action="${ctx }/admin/user" method="post">
		        <div class="form-group">
		          <label class="sr-only" for="search_LIKE_loginName">登录账号：</label>
		          <input type="text" class="form-control input-sm" id="search_LIKE_loginName" name="search_LIKE_loginName" value="${param.search_LIKE_loginName }" placeholder="按登录账号查询">
		        </div>
		        <div class="form-group">
		          <label class="sr-only" for="search_LIKE_nickname">用户名：</label>
		          <input type="text" class="form-control input-sm" id="search_LIKE_nickname" name="search_LIKE_nickname" value="${param.search_LIKE_nickname }" placeholder="按用户名查询">
		        </div>
		        <div class="form-group">
		          <label class="sr-only" for="search_LIKE_custName">客户名：</label>
		          <input type="text" class="form-control input-sm" id="search_LIKE_custName" name="search_LIKE_custName" value="${param.search_LIKE_custName }" placeholder="按客户名查询">
		        </div>
		        <div class="form-group">
		          <label for="search_EQ_roleId">角色：</label>
		          <select name="search_EQ_role.id" class="form-control input-sm">
		               <option value="" <c:if test="${empty param.search_EQ_role.id }">selected</c:if> > -选择角色
		               <zy:options items="${roles }" itemLabel="roleName" itemValue="id" selecteds="${param['search_EQ_role.id'] }"/>
		          </select>
		        </div>
		        <div class="form-group">
		          <button type="submit" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-search"></span> 搜索</button>
		        </div>
		      </form>
			
			</div>
			
		</div><!-- /查询条件 -->
	  
	  <div class="row"><!-- 操作按钮组 -->
	    <div class="col-md-12">
	      <div class="btn-group-sm pull-right mtb10">
	      <shiro:hasPermission name="user:create">
	        <a class="btn btn-primary" href="${ctx }/admin/user/create" ><span class="glyphicon glyphicon-plus"></span> 创建新用户</a>
	      </shiro:hasPermission>
	      </div>
	    </div>
	  </div><!-- /操作按钮组 -->
				
		<table id="contentTable" class="table table-bordered table-condensed table-hover">
			<thead class="thead">
			<tr>
			<%-- 
			  <th class="text-center"><input type="checkbox" name="chk_all" onclick="if(this.checked==true) { checkAll('chk_list'); } else { clearAll('chk_list'); }" /></th>
			--%>
				<th class="text-center" width="40">序号</th>
				<th>用户名</th>
				<th>所属会员</th>
				<th>角色</th>
				<th class="text-center">创建时间</th>
				<th class="text-center">到期时间</th>
				<th class="text-center">状态</th>
				<%-- 
				<th>最后登录时间</th>
				<th>最后登录IP</th>
				--%>
				<th class="text-right">操作</th>
			</tr>
			</thead>
			
			<tbody>
			<%  
			pageContext.setAttribute("currentUser", UserContext.getCurrent().getLoginName());
			%>
			<c:forEach items="${data.content}" var="user" varStatus="stat">
				<tr>
					<td class="text-center">${stat.count }</td>
					<td>${user.loginName}</td>
					<td>${user.customer.name}</td>
					<td>${user.role.roleName}</td>
					<td class="text-center"><fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
					<td class="text-center"><fmt:formatDate value="${user.expiredTime}" pattern="yyyy-MM-dd" /> </td>
					<td class="text-center"><zy:dic value="${user.status}" /></td>
					<td class="text-right">
					<c:if test="${! (user.loginName eq currentUser) }">
					  <a href="${ctx}/admin/user/update/${user.id}" > 修改</a>
					  
					  <span class="cutline"></span> 
					  <a href="#" class="delete-user" data-name="${user.loginName }" data-id="${user.id }"> 删除</a>
					  
					  <span class="cutline"></span> 
					  <a href="#" class="reset-pwd" data-name="${user.loginName }" data-id="${user.id }""> 重置密码</a>
					  
					  <c:if test="${user.status eq 'USER_ENABLED' }">
						<span class="cutline"></span> 
						<a href="#" class="dimission" data-name="${user.loginName }" data-id="${user.id }"> 离职</a>
					  </c:if>
					</c:if>
					</td>
				</tr>
			</c:forEach>
			
			</tbody>		
		</table>
		
		<tags:pagination page="${data}" />
    
  </div><!-- /右侧主体内容 -->

</div>

<form id="actionForm" action="" method="post">
   <input type="hidden" id="ids" name="ids">
</form>

<script type="text/javascript">

$(function() {
	menu.active('#user-man');
	
	var $form = $('#actionForm');
	var user = {};
	
	user.confirm = function (dom, url, title, message) {
		var id = $(dom).data('id');
		$form.attr('action', url + id);
		//bootbox.setDefaults({size:'large'});
		common.confirm(title, message, function(result) {
			if(result) {
			  $form.submit();
			}
	 	});
		return false;
	};
	
	$('.reset-pwd').click(function() {
		var url = '${ctx}/admin/user/resetPwd/';
		return user.confirm(this, url, '请确认', '您确定要重置 [' + $(this).data('name') + '] 的密码？');	
	});
	
	$('.delete-user').click(function() {
		var url = '${ctx}/admin/user/delete/';
		return user.confirm(this, url, '警告', '您确定要删除 [' + $(this).data('name') + '] 吗？');				  
	});
	
	$('.dimission').click(function() {
		var url = '${ctx}/admin/user/dimission/';
		return user.confirm(this, url, '警告', '您确定要设置 [' + $(this).data('name') + '] 为“离职”状态吗？');		  
	});
	
});
  

</script>

</body>
</html>
