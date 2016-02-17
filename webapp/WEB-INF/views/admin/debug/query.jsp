<%@page import="com.lczy.common.util.PropertyUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Properties"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<html>
<head>
    <title>系统配置</title> 
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统调试</li>
        <li class="active">接口调试</li>
        <span id="loading" class="pull-right"><img src="${ctx}/static/img/loading.gif" /></span>
    </ul>
  </div><!-- / 右侧标题 -->
  
  
  <div class="panel-body"><!-- 右侧主体内容 -->
    <fieldset>
	   <legend>域名查询</legend>        
	   <form id="domain-form">
	   	<div class="form-group">
	    	<label>接口名称</label> 
	    	<select id="serviceName" name="serviceName" class="form-control">
	    		<option value="auditResult">实名审核状态查询</option>
	    		<option value="domainInfo">域名信息查询</option>
	    	</select>
	   	</div>
       	
		<div class="form-group">
			<label >域名</label>
			<input type="text" class="form-control" id="domain" name="domain" placeholder="请输入需要查询的域名">
		</div>   	

	   </form>	

	  <div class="form-group">
	  	<button class="btn btn-primary btn-lg" type="button" id="domain-query"> 查 询 </button>
	  </div>       	

      <div class="form-group">
		<textarea id="response" class="form-control" rows="15"></textarea>
      </div> 	

    </fieldset>
  </div>

</div>


<script type="text/javascript">
$(function() {
	menu.active('#debug-query');
	
	$('#domain-query').click(function() {
		$.post('${ctx}/admin/debug/query?ajax', $('#domain-form').serialize() , function(data) {
			$('#response').text(data);
		});
	});

});
</script>
    
</body>
</html>
