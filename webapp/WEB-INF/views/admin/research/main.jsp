<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<html>
<head>
	<title>竞品分析</title>
</head>

<body>

<div class="panel panel-default">

	<div class="panel-heading"><!-- 右侧标题 -->
	  <ul class="breadcrumb">
	      <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
	      <li class="active">竞品分析</li>
	  </ul>
	</div><!-- / 右侧标题 -->
  
  
	<div class="panel-body"><!-- 右侧主体内容 -->
		<div class="row">
			<div class="col-md-12">
			
				<iframe id="main-frame" style="border: none; width: 100%"
					src="${url }"></iframe>
			
			</div>
		</div>
	</div>

</div>

<script type="text/javascript">

//左侧菜单高亮显示
menu.active('#admin-research');

$(function() {
	var $frame = $('#main-frame');
	
	$(window).resize(function() {
		var h = $(document).height();
		$frame.outerHeight(h - $frame.offset().top - 120);
	});
	
	$(window).resize();
})

</script>
	
</body>
</html>
