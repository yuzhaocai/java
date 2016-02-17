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
        <li class="active">修改组织信息</li>
    </ul>
  </div><!-- / 右侧标题 -->
  
  <div class="panel-body"><!-- 右侧主体内容 -->
  
	<form id="org-form" action="${ctx}/admin/org/${org.id }/edit" method="post" class="form-horizontal">
    	<zy:token/>
		
	<fieldset>
		<legend><small>登录信息</small></legend>
			
	    <div class="form-group form-group-sm">
	       <label for="loginName" class="col-md-3 control-label"><span class="text-red">* </span>登录账号:</label>
	       <div class="col-md-6 has-feedback">
	         <input type="text" class="form-control" id="loginName" name="loginName" value="${user.loginName}" readonly="readonly" />
	       </div>
	    </div>
	    <%-- 
	    <div class="form-group form-group-sm">
         <label for="password" class="col-md-3 control-label"><span class="text-red">* </span>密码:</label>
         <div class="col-md-6 has-feedback">
           <input type="password" class="form-control" id="password" name="password" value="${param.password}" />
         </div>
        </div>
      
	    <div class="form-group form-group-sm">
         <label for="checkPassword" class="col-md-3 control-label"><span class="text-red">* </span>重复密码:</label>
         <div class="col-md-6 has-feedback">
           <input type="password" class="form-control" id="checkPassword" name="checkPassword" />
         </div>
      	</div>
	    --%>
			
	</fieldset>
	
	<fieldset>
		<legend><small>组织信息</small></legend>
			
	    <div class="form-group form-group-sm">
	       <label for="name" class="col-md-3 control-label"><span class="text-red">* </span>组织名称:</label>
	       <div class="col-md-6 has-feedback">
	         <input type="text" class="form-control" id="name" name="name" value="${org.name}" />
	       </div>
	    </div>
        
	    <div class="form-group form-group-sm">
         <label for="linkman" class="col-md-3 control-label"><span class="text-red">* </span>联系人:</label>
         <div class="col-md-6 has-feedback">
           <input type="text" class="form-control" id="linkman" name="linkman" value="${org.linkman}" />
         </div>
        </div>
        
	    <div class="form-group form-group-sm">
         <label for="telPhone" class="col-md-3 control-label"><span class="text-red">* </span>电话:</label>
         <div class="col-md-6 has-feedback">
           <input type="text" class="form-control" id="telPhone" name="telPhone" value="${org.telPhone}" />
         </div>
        </div>
        
	    <div class="form-group form-group-sm">
         <label for="email" class="col-md-3 control-label"><span class="text-red"></span>邮箱:</label>
         <div class="col-md-6 has-feedback">
           <input type="text" class="form-control" id="email" name="email" value="${org.email}" />
         </div>
        </div>
			
	</fieldset>

		
		<br/>
		
		 <div class="form-group">
			<div class="col-md-offset-3 col-md-2">
			   <a class="btn btn-default btn-block" href="${ctx}/admin/org/list"><span class="glyphicon glyphicon-remove"></span> 取 消</a>
			</div>
			<div class="col-md-2">
			  <button type="submit" class="btn btn-primary btn-block" id="btn-submit"><span class="glyphicon glyphicon-ok"></span> 保 存</button>
			</div>
		</div>

	</form>

  </div>
	
</div>

<script type="text/javascript">
$(function() {
	menu.active('#org-list');
	
	$('#org-form').validate({
		debug: true,
		submitHandler: function(form) {
			common.disabled('#btn-submit');
			form.submit();
		}, 
		rules: { 
			name: {
				required: true, 
				minlength:2, 
				maxlength:20,
				remote: '${ctx}/common/checkCustomerName?oldName='+encodeURIComponent('${org.name}')
				},
			coAddress: {
				minlength: 6,
				maxlength: 40
			},
			linkman: {
				required: true,
				minlength: 2,
				maxlength: 30
			},
			telPhone: {
				required: true,
				digits:true,
				minlength:8
			},
			email : {
				email: true
			}
		},
		messages: {
			name: {
				remote: '组织名称已经存在，请重新输入！'
			}
		}
	});
});
</script>
</body>
</html>
