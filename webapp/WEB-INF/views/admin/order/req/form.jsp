<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>需求管理</title>
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
        <li>需求管理</li>
        <li class="active">
          修改需求
        </li>
    </ul>
  </div><!-- / 右侧标题 -->
  
  <div class="panel-body"><!-- 右侧主体内容 -->
  
	<form id="inputForm" action="${ctx }/admin/order/req/update" method="post" class="form-horizontal">
    <zy:token/>
		<input type="hidden" name="id" value="${requirement.id}"/>
		
		<fieldset>
	    <div class="form-group form-group-sm">
         <label for="name" class="col-md-3 control-label"><span class="text-red">* </span>名称:</label>
         <div class="col-md-6 has-feedback">
           <input type="text" class="form-control" id="name" name="name" value="${requirement.name}" />
         </div>
      </div>
      
<!--         <div class="form-group form-group-sm"> -->
<!--            <label for="budget" class="col-md-3 control-label"><span class="text-red">* </span>酬金:</label> -->
<!--            <div class="col-md-6 has-feedback"> -->
<%--              <input type="text" class="form-control" id="budget" name="budget" value="${requirement.budget}" /> --%>
<!--            </div> -->
<!--         </div> -->
        
      <div class="form-group form-group-sm">
         <label for="summary" class="col-md-3 control-label"><span class="text-red">* </span>简介:</label>
         <div class="col-md-6 has-feedback">
           <textarea class="form-control" id="summary" name="summary" style="height: auto;" rows="3">${requirement.summary }</textarea>
         </div>
      </div>
      
      
		</fieldset>
		
		<br/>
		
		 <div class="form-group">
			<div class="col-md-offset-3 col-md-2">
			   <a class="btn btn-default btn-block" href="${ctx}/admin/order/req"><span class="glyphicon glyphicon-remove"></span> 返回</a>
			</div>
			<div class="col-md-2">
			  <button type="submit" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-ok"></span> 保存</button>
			</div>
		</div>

	</form>

  </div>
	
</div>

<script type="text/javascript">
$(function() {
	menu.active('#order-req');
	
	$('#inputForm').validate({
		rules: {
			name: {
				required: true
            },
            budget: {
                required: true
            },
            summary: {
                required: true
			}
			
		}
	});
});
</script>
</body>
</html>
