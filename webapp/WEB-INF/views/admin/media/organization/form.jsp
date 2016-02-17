<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>机构管理</title>
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
        <li>机构管理</li>
        <li class="active">
          <c:if test="${'create' eq action }"> 新建机构</c:if>
          <c:if test="${'update' eq action }"> 修改机构</c:if>
        </li>
    </ul>
  </div><!-- / 右侧标题 -->
  
  <div class="panel-body"><!-- 右侧主体内容 -->
  
	<form id="inputForm" action="${ctx}/admin/media/organization/${action }" method="post" class="form-horizontal">
    <zy:token/>
		<input type="hidden" name="id" value="${org.id}"/>
		
		<fieldset>
	    <div class="form-group form-group-sm">
         <label for="name" class="col-md-3 control-label"><span class="text-red">* </span>机构名称:</label>
         <div class="col-md-6 has-feedback">
           <input type="text" class="form-control" id="name" name="name" value="${org.name}" />
         </div>
      </div>
      
<%--       <c:if test="${empty org.id }" >新建时显示, 修改时不显示 --%>
      
      <div class="form-group form-group-sm">
          <label for="orgType" class="col-md-3 control-label"><span class="text-red">* </span>机构类型:</label>
      
      
          <div class="col-md-6 has-feedback">
	          <select class="form-control" name="orgType"> 
	              <c:forEach items="${organizationTypes}" var="orgType">
	              <option value="${orgType.itemCode }" <c:if test="${org.orgType eq orgType.itemCode }">selected="selected"</c:if> >${orgType.itemName }</option>
	              </c:forEach>                                
	          </select>
          </div>
      </div>
      
        <div class="form-group form-group-sm">
           <label for="loginName" class="col-md-3 control-label"><span class="text-red">* </span>联系方式:</label>
           <div class="col-md-6 has-feedback">
             <input type="text" class="form-control" id="loginName" name="loginName" value="${org.mobPhone}"/>
           </div>
        </div>
        
<%--       </c:if>/新建时显示 --%>
      
      <div class="form-group form-group-sm">
         <label for="orgSummary" class="col-md-3 control-label"><span class="text-red">* </span>简介:</label>
         <div class="col-md-6 has-feedback">
           <textarea class="form-control" id="orgSummary" name="orgSummary" style="height: auto;" rows="3">${org.orgSummary }</textarea>
         </div>
      </div>
      
      
		</fieldset>
		
		<br/>
		
		 <div class="form-group">
			<div class="col-md-offset-3 col-md-2">
			   <a class="btn btn-default btn-block" href="${ctx}/admin/media/organization"><span class="glyphicon glyphicon-remove"></span> 返回</a>
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
	menu.active('#media-organization');
	$('#inputForm').validate({
		rules: {
			loginName: {
				required: true, 
				remote: "${ctx}/common/checkLoginName?oldName=${org.mobPhone}"
			},
			name: {
				required: true, 
				remote: {
					url: "${ctx}/admin/user/checkNickname?oldName=${org.name}",
				    data: {
				    	nickname: function() {
				    		return $("#name").val();
				    	}
				    }
			    }
			},
			orgSummary: {
				required: true
			}
			
		},
		messages: {
			loginName: {
				remote: '联系方式已经存在，请重新输入！'
			},
			name: {
				remote: '机构名称已经存在，请重新输入！'
            },
            orgSummary: {
                required: "请输入机构简介!"
			}
		}
	});
});
</script>
</body>
</html>
