<%@ page contentType="text/html;charset=UTF-8"  isErrorPage="true" session="false"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<%response.setStatus(200);%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta name="decorator" content="printable" />

	<title>404 - 页面不存在</title>
	<style type="text/css">
		body{
			padding:0;
			margin:0;
		    background:#c8c8c8 url(${ctx}/static/assets/img/error-bg.png) center top repeat-y;
			font-family:'微软雅黑';
		}
		img{border:none;}
		.pad{
			z-index:10;
			position:relative;
			min-height:772px;
			background:url(${ctx}/static/assets/img/404.png) no-repeat center top;
		}
		.pad h3{
			position:absolute;
			bottom:50px;
			width:100%;
			color:#666;
			text-align:center;
			font-size:26px;
			font-weight:normal;
		}
		.back {
			display:block;
			margin:auto;
			color:#666;
			width:279px;
			height:79px;
			font-size:26px;
			line-height:79px;
			text-decoration:none;
			background:url(${ctx}/static/assets/img/404btn.png) no-repeat center bottom;
			
		}
	
	</style>
	
	<script src="${ctx}/static/bower_components/jquery/dist/jquery.min.js" type="text/javascript"></script>
	
</head>

<body>
	<div class="pad">
		<h3>抱歉！您访问的页面已不在这个星球。<br><br><br>
			<a href="${ctx}/" class="back"></a>
		</h3>
	</div>
	<script type="text/javascript">
		$(function(){
			toReturn();
		});
		var seconds = 6;
		/*返回倒计时*/
		function toReturn() {
			if (seconds <= 0) {
				window.location.replace('${ctx}/');
			} else {
				seconds--;
				$('.back').text("返回首页(" + seconds + "秒)");
				setTimeout(toReturn, 1000);
			}
		}
	</script>
</body>	
</html>