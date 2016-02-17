<%@page import="com.lczy.media.util.UserContext"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!--=== Header v1 ===--> 
<div id="header" class="container-fluid"> 
	<div class="row"> 
		<div class="header-v1">
            <!-- Topbar -->
            <div class="topbar-v1">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-xs-6">
                            <ul class="list-inline top-v1-contacts">
                                <li>
                                    <a id="qrcode-mc" href="${ctx}/static/assets/img/qrcode-mc.jpg" title="关注采媒在线" >
										<i class="glyphicon glyphicon-qrcode"></i> ${headerTitle }
									</a>
                                </li>
                                
                            </ul>
                        </div>
                        <div class="col-xs-6">
                            <ul class="list-inline top-v1-data">
                            <shiro:authenticated>
                            	<li><a class="text-primary">您好，<shiro:principal />！</a> </li>
                            </shiro:authenticated>
<%--    							<shiro:hasRole name="advertiser"> --%>
<!--                                 <li><a href="javascript:void(0)" id="car-title"><span class="fa fa-shopping-cart"></span> 采媒车 <span class="badge badge-red rounded-2x" id="car-num"></span></a></li> -->
<%--                             </shiro:hasRole> --%>
                            <shiro:authenticated>
                                <li><a href="#" id="changePwd"><span class="fa fa-key"></span> 修改密码</a></li>
                                <li><a href="${ctx }/logout"><span class="glyphicon glyphicon-lock"></span>退出</a></li>
                            </shiro:authenticated>
                            </ul>
                        </div>
                    </div>        
                </div>
            </div>
            <!-- End Topbar -->
            <div class="navbar navbar-default c-menu" role="navigation">
                <div class="container-fluid">
                	<div class="logo">
                    	<a href="${ctx }/" class="cont1"></a>
                    	<a href="${ctx }/member" class="cont2">我的采媒在线</a>
                    	<a href="${ctx }/" class="cont3"><i class="fa fa-home"></i> 采媒在线首页</a>
                    </div>
                    <ul class="nav navbar-nav">
                        <%-- 
                        <li><a href="home.html">首页</a></li>                      
                        <li><a href="javascript:void(0);">个人主页</a></li>
                        <li><a href="javascript:void(0);">账户设置</a></li>
                        --%>
                    </ul>

                </div>
            </div>

            
		</div>
	</div>
</div>
<!--=== End Header v1 ===-->
<div id="qqWpa">
	<!-- WPA Button Begin -->
	<script charset="utf-8" type="text/javascript" src="http://wpa.b.qq.com/cgi/wpa.php?key=XzkzODA1NDcyNF8zNjI2NTJfNDAwMDA2MDU4Nl8"></script>
	<!-- WPA Button End -->
</div>

<!-- 右侧浮动内容 begin -->
<div class="floatRShow">
	<div class="floatRCon shopCarBor hasGoods">
	  <a href="javascript:void(0);" class="shopCar" id="car-title"><span id="car-num"></span></a>
	</div>
	<div class="floatRCon QQService">
	  <a href="javascript:void(0);" class="QQChart" id="qqControl"></a>
	  <a href="javascript:void(0);" class="serviceTel"></a>
	</div>
	<div class="floatRCon qtTop">
	  <a id="complaintControl" data-toggle="modal" data-target="#modal-complaint"></a>
	  <a href="javascript:void(0);" class="myCollect"></a>
	  <a href="javascript:void(0);" class="QRcode"></a>
	</div>
</div>
<!-- 右侧浮动内容 end -->

<script type="text/javascript">
$(function() {
	// ------ active fancybox ------
	// Set custom style, close if clicked, change title type and overlay color
	$("#qrcode-mc").fancybox({
		autoCenter  : false,
		//wrapCSS    : 'fancybox-custom',
		//closeClick : true,
		openEffect  : 'elastic',
		closeEffect	: 'elastic',

		helpers : {
			title : {
				type : 'inside'
			}
		}
	});
	
	$('#car-title').click(function(){
		var modal = '<div class="modal fade modal-sm" id="shopping-modal" tabindex="-1" role="dialog" data-focus-on="input:first" data-width="700" ></div>';
		var $shoppingModal = $('#shopping-modal');
		if( $shoppingModal.length == 0 ) {
			$shoppingModal = $(modal);
			$('body').append($shoppingModal);
		}
		$shoppingModal.loadModal('${ctx}/shoppingcart/queryMedias?ajax');
	});
	
	$.post("${ctx}/shoppingcart/getNum", function(result){
		$('#car-num').text(result?result:0);
	});
	
	$('.myCollect').click(function(){
		var t = $(this);
		var browserVar = navigator.userAgent.toLowerCase();
		var url = window.location.href;
		var title = $('title').text();
		if( /msie/.test(browserVar)) {
			window.external.addFavorite(url, title);
		} else if (/firefox/.test(browserVar) ||  /opera/.test(browserVar)) {
			t.attr("rel", "sidebar");
			t.attr("title", title);
			t.attr("href", url);
		} else {
			alert("请使用Ctrl+D将本页加入收藏夹！");
		}
	});
});
</script>