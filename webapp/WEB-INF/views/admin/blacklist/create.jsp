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
    <li><a href="${ctx}/admin/blacklist"> 黑名单管理</a></li>
    <li class="active"> 新建条目 </li>
  </ul>
</div><!-- / 右侧标题 -->

<div class="panel-body"><!-- 右侧主体内容 -->
	
<form id="form1" role="form" method="post" class="form-horizontal" action="${ctx}/admin/blacklist/create">
	<zy:token/>
	<fieldset>
		<legend><small>黑名单信息</small></legend>
		<div class="form-group form-group-sm">
		   <label for="roleCode" class="col-sm-2 control-label"><span class="text-red">* </span>类型:</label>
		   <div class="col-sm-6 has-feedback">
			   <form:select path="blacklist.type" items="${types}" itemLabel="itemName" itemValue="itemCode" class="form-control"></form:select>
		   </div>
		</div>
		
		<div class="form-group form-group-sm">
       <label for="roleName" class="col-sm-2 control-label"><span class="text-red">* </span>条目:</label>
       <div class="col-sm-6 has-feedback">
         <input type="text" class="form-control" id="item" name="item" placeholder="" />
       </div>
    </div>

	</fieldset>
	
	<div class="form-group">
	   <div class="col-sm-offset-2 col-sm-4">
	     <a class="btn btn-default" href="${ctx}/admin/blacklist/list"><span class="glyphicon glyphicon-remove"></span> 返回</a>
	     <span class="col-sm-offset-1"></span>
	     <button type="submit" class="btn btn-primary" id="submit_btn"><span class="glyphicon glyphicon-ok"></span> 保存</button>
	   </div>
	</div>
          
</form>


</div><!-- / 右侧主体内容 -->

</div>


<script type="text/javascript">
  $(document).ready(function() {
    //menu.active('#role-man');
    
    //为表单注册validate函数
    $("#form1").validate({
    	submitHandler: function(form) {
    		form.submit();
    	},
      rules: {
        type: {
          required: true
        },
        item: {
          required: true
        }
      }
    });
  });
</script>

</body>
</html>
