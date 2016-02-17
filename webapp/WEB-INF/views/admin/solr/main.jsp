<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<html>
<head>
	<title>索引管理</title>
</head>

<body>

<div class="panel panel-default">

	<div class="panel-heading"><!-- 右侧标题 -->
	  <ul class="breadcrumb">
	      <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
	      <li class="active">索引管理</li>
	  </ul>
	</div><!-- / 右侧标题 -->
  
  
	<div class="panel-body"><!-- 右侧主体内容 -->
		<div class="row">
			<div class="col-md-2">
			
				<form action="${ctx }/admin/solr/rebuildMedia" method="post">
					<button type="submit" class="btn btn-primary" >重建媒体索引</button>
				</form>
			
			</div>
			<div class="col-md-2">
				
				<form action="${ctx }/admin/solr/rebuildRequirement" method="post">
					<button type="submit" class="btn btn-primary" >重建需求索引</button>
				</form>
			
			</div>
		</div>
	</div>

</div>

<script type="text/javascript">

$(function() {
	$('[type="submit"]').click(function() {
		this.disabled = true;
	});
})

</script>
	
</body>
</html>
