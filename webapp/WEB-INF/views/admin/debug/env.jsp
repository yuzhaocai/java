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
    <ul class="breadcrumb"><li><span class="glyphicon glyphicon-home"></span> 系统调试</li>
        <li class="active">系统环境</li>
        
        <span id="loading" class="pull-right"><img src="${ctx}/static/img/loading.gif" /></span>
    </ul>
  </div><!-- / 右侧标题 -->
  
  
  <div class="panel-body"><!-- 右侧主体内容 -->
    <fieldset>
        <legend>系统变量</legend>        
<textarea class="form-control" rows="15">
<%
Properties props = System.getProperties();
List keys = new ArrayList(props.keySet());
Collections.sort(keys);
for(Object key : keys) {
	String value = props.getProperty(key.toString());
	out.write(key + " = " + value + "\n");
}
%>
</textarea>
    </fieldset>
    <br>
    <fieldset>
        <legend>程序配置</legend>
<textarea class="form-control" rows="15">
<%
props = PropertyUtils.getProperties();
keys = new ArrayList(props.keySet());
Collections.sort(keys);
for(Object key : keys) {
    String value = props.getProperty(key.toString());
    out.write(key + " = " + value + "\n");
}
%>
</textarea>        
    </fieldset>
  </div>

</div>


<script type="text/javascript">
$(function() {
	menu.active('#debug-env');
});
</script>
    
</body>
</html>
