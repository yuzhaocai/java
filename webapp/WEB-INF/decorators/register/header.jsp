<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!--=== Header v1 ===--> 
<div id="header" class="container-fluid"> 
	<div class="row">
		<div class="header-v1">
            <!-- Topbar -->
            <div class="topbar-v1">
                <div class="container">
                    <div class="row">
                        <div class="col-xs-6">
                          <div class="row">
                            <ul class="list-inline top-v1-contacts">
                                <li>
                                    <a id="qrcode-mc" href="#focus" title="关注采媒在线" >
										<i class="glyphicon glyphicon-qrcode"></i> 关注采媒在线
									</a>
                                </li>
                            </ul>
                          </div>
                        </div>
                        
                        <div class="col-xs-6">
                          <div class="row">
                            <ul class="list-inline top-v1-data">
                                <li><a href="${ctx }/register">免费注册</a></li>
                                <li><a href="#" id="loginBtn" >登录</a></li>
                            </ul>
                          </div>
                        </div>
                    </div>        
                </div>
            </div>
            <!-- End Topbar -->

            <!-- Navbar -->
            <div class="navbar navbar-default mega-menu" role="navigation">
                <div class="container">
                    <div class="navbar-header">
                        <div class="row">
                            <div class="col-xs-3">
                                <a class="navbar-brand" href="${ctx}/">
                                    <img id="logo-header" src="${ctx}/static/assets/img/logo.png" alt="Logo">
                                </a>
                            </div>                            
                        </div> 
                    </div>
				</div><!--/end container-->
          	</div><!--Navbar-->
          
       </div><!--End header-v1 -->            
	</div><!-- End row -->
</div><!--=== End Header v1 ===-->


<div id="focus" style="display:none">
	<img alt="关注采媒在线" width="300" src="${ctx}/static/assets/img/qrcode-mc.jpg">
</div>


<script type="text/javascript">
$(function() {
	// ------ active fancybox ------
	// Set custom style, close if clicked, change title type and overlay color
	$("#qrcode-mc").fancybox({
		//href: '${ctx}/static/assets/img/qrcode-mc.jpg',
		autoCenter  : false,
		autoScale   : false,
		autoDimensions: false,
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
	
	
	
	//弹出登录框
	$('#loginBtn').click(function() {
		var modal = '<div class="modal fade modal-sm" id="login-modal" tabindex="-1" role="dialog" data-focus-on="input:first" data-width="350" ></div>';
		var $loginModal = $('#login-modal');
		if( $loginModal.length == 0 ) {
			$loginModal = $(modal);
			$('body').append($loginModal);
		}
		$loginModal.loadModal('${ctx}/loginModal?ajax');
	});
	
});
</script>
