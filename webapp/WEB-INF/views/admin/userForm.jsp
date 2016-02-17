<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>用户管理</title>
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
        <li>用户管理</li>
        <li class="active">
          <c:if test="${'create' eq action }"> 新建用户</c:if>
          <c:if test="${'update' eq action }"> 修改用户</c:if>
        </li>
    </ul>
  </div><!-- / 右侧标题 -->
  
  <div class="panel-body"><!-- 右侧主体内容 -->
  
	<form id="inputForm" action="${ctx}/admin/user/${action }" method="post" class="form-horizontal">
    <zy:token/>
		<input type="hidden" name="id" value="${user.id}"/>
		
		<fieldset>
			<legend><small>用户信息</small></legend>
			
	    <div class="form-group form-group-sm">
	       <label for="loginName" class="col-md-3 control-label"><span class="text-red">* </span>用户名:</label>
	       <div class="col-md-6 has-feedback">
	         <input type="text" class="form-control" id="loginName" name="loginName" value="${user.loginName}"
	           <c:if test="${!empty user.id }">readonly="readonly"</c:if> />
	       </div>
	    </div>
	    
<!-- 	   <div class="form-group form-group-sm"> -->
<!--          <label for="nickname" class="col-md-3 control-label"><span class="text-red">* </span>用户名:</label> -->
<!--          <div class="col-md-6 has-feedback"> -->
<%--            <input type="text" class="form-control" id="nickname" name="nickname" value="${user.nickname}" /> --%>
<!--          </div> -->
<!--        </div> -->
      
      <c:if test="${empty user.id }" ><%-- 新建时显示, 修改时不显示 --%>
      
      <div class="form-group form-group-sm">
         <label for="password" class="col-md-3 control-label"><span class="text-red">* </span>密码:</label>
         <div class="col-md-6 has-feedback">
           <input type="password" class="form-control" id="password" name="password" />
         </div>
      </div>
      
      <div class="form-group form-group-sm">
         <label for="againPassword" class="col-md-3 control-label"><span class="text-red">* </span>重复密码:</label>
         <div class="col-md-6 has-feedback">
           <input type="password" class="form-control" id="againPassword" name="againPassword" />
         </div>
      </div>
      </c:if><%-- /新建时显示 --%>
      
      <div class="form-group form-group-sm">
         <label for="status" class="col-md-3 control-label"><span class="text-red">&nbsp;</span>状态:</label>
         <div class="col-md-6 has-feedback">
           <form:select path="user.status" items="${status }" itemLabel="itemName" itemValue="itemCode" class="form-control"  />
         </div>
      </div>
      
      
      <div class="form-group form-group-sm">
         <label for="userRole" class="col-md-3 control-label"><span class="text-red">&nbsp;</span>角色:</label>
         <div class="col-md-6 has-feedback">
         <%-- 
            <div class="radio">
           <zy:radiobuttons element="label" elementClass="radio-inline" path="user.roleId"  items="${roles }" itemLabel="roleName" itemValue="roleId" />
            </div>
         <c:choose>
	         <c:when test="${empty user.id }" >
	            <select name="roleId">
	            	<c:forEach items="${roles }" var="role">
	            		<option value="${role.id }">${role.roleName }</option>
	            	</c:forEach>
	            </select>
	         </c:when>
	         <c:otherwise>
	            <input type="text" class="form-control" name="roleName" value="${user.role.roleName }" readonly="readonly">
	         </c:otherwise>
         </c:choose>
         --%>
         <form:select path="user.role.id" items="${roles}" itemLabel="roleName" itemValue="id" class="form-control"></form:select>
         </div>
      </div>
      
	  <div class="form-group form-group-sm">
         <label for="nickname" class="col-md-3 control-label"><span class="text-red">&nbsp;</span>到期时间:</label>
         <div class="col-md-6 has-feedback">
           <input type="text" class="form-control" id="expiredTime" name="expiredTime" value='${user.expiredTime}' 
           onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})"/>
           
         </div>
       </div>
       <!-- 格式化“到期日期” -->
	   <div id="expiredTimeFmt" style="display:none"><fmt:formatDate value="${user.expiredTime}" pattern="yyyy-MM-dd" /></div>
		</fieldset>
		
		<br/>
		
		 <div class="form-group">
			<div class="col-md-offset-3 col-md-2">
			   <a class="btn btn-default btn-block" href="${ctx}/admin/user"><span class="glyphicon glyphicon-remove"></span> 返回</a>
			</div>
			<div class="col-md-2">
			  <button type="submit" class="btn btn-primary btn-block" id="submit_btn"><span class="glyphicon glyphicon-ok"></span> 保存</button>
			</div>
		</div>

	</form>

  </div>
	
</div>

<script type="text/javascript">
$(function() {
	menu.active('#user-man');
	// “到期日期” 赋值为格式化后的日期
	$('#expiredTime').val($('#expiredTimeFmt').text());
	
	$('#inputForm').validate({
		rules: {
			loginName: {
				required: true
				, letter: true
				, rangelength : [6, 16]
	      , remote: "${ctx}/common/checkLoginName?oldName=" + encodeURIComponent('${user.loginName}')
			},
			nickname: {
				required: true
				, rangelength : [2, 16]
			  , remote: "${ctx}/admin/user/checkNickname?oldName=" + encodeURIComponent('${user.nickname}')
			},
			password: {
				required: true
				, rangelength : [6, 20]
			},
			againPassword: {
				required: true
				, equalTo : '#password'
			}
		},
		messages: {
			loginName: {
				remote: '登录名已经存在，请重新输入！'
			},
			nickname: {
				remote: '用户名已经存在，请重新输入！'
			}
		}
	});
});
</script>
</body>
</html>
