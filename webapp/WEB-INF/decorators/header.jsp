
<%@page import="com.lczy.media.util.UserContext"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
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
                            <ul class="list-inline top-v1-data inline-block pull-left">
                                <li>
                                    <a id="qrcode-mc" href="#focus" title="关注采媒在线" >
										<i class="glyphicon glyphicon-qrcode"></i> 关注采媒在线
									</a>
                                </li>
                                <li>
                                    <a target="_blank" href="http://www.cnmei.com/cms/changelog">更新日志</a>
                                </li>
                            </ul>
                          </div>
                        </div>
                        
                        <div class="col-xs-6">
                          <div class="row">
                            <ul class="list-inline top-v1-data">
                            <shiro:authenticated>
                            	<li><a class="text-primary">您好，<shiro:principal />！</a> </li>
                            </shiro:authenticated>
                            <shiro:guest>
                                <li><a href="${ctx }/register">免费注册</a></li>
                                <li><a href="#" id="loginBtn" >登录</a></li>
                            </shiro:guest>
   							
                            <shiro:authenticated>
                                <li><a href="${ctx }/member"><span class="glyphicon glyphicon-home"></span> 会员中心</a></li>
<%--                                 <shiro:hasRole name="advertiser"> --%>
<!--                                 <li><a href="javascript:void(0)" id="car-title"><span class="fa fa-shopping-cart"></span> 采媒车 <span class="badge badge-red rounded-2x" id="car-num"></span></a></li> -->
<%--                                 </shiro:hasRole> --%>
                                <li><a href="#" id="changePwd"><span class="fa fa-key"></span> 修改密码</a></li>
                               
                                <li><a href="${ctx }/logout"><span class="glyphicon glyphicon-lock"></span> 退出</a></li>
                            </shiro:authenticated> 
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
                        <div class="row headerMargin">
                            <div class="col-xs-3">
                                <a class="navbar-brand" href="${ctx}/">
                                    <img id="logo-header" src="${ctx}/static/assets/img/logo.png" alt="Logo">
                                </a>
                            </div>    
                                                    
                        	<!-- Search Block -->
                        	<!-- 元旦样式注释
                            <div class="monkey"><img src="${ctx}/static/assets/img/yuandan-bg-houzi.png" ></div>
 -->
                             <div class="col-xs-5">
                            	<div class="search-wrap">
                                    <ul class="list-unstyled list-inline search-tabs">
                                        <li data-value="weixin" data-placeholder="搜微信">微信</li>
                                        <li data-value="weibo" data-placeholder="搜微博">微博</li>
                                    	<shiro:authenticated>
	                                    	<li data-value="req" data-placeholder="搜需求">需求</li>
			                            </shiro:authenticated>
                                    	<shiro:guest>
	                                    	<li data-value="req" data-placeholder="搜需求" data-login="true">需求</li>
			                            </shiro:guest>
                                    </ul>
                                   <div class="input-group">
                                       <i class="fa fa-search"></i>
                                       <input type="hidden" id="data-type" name="data-type" value="" >
                                       <input type="text" class="form-control" id="searchValue" name="name" value="${param.name}">
                                       <span class="input-group-btn"><button type="submit" class="btn btn-default" id="data-search">搜索</button></span>
                                   </div>                                  
                                </div>        
                            </div>
                            <!-- End Search Block -->
                            <div class="col-xs-4 text-center">
	                            <div class="col-md-8">
		                            <p>
										<img src="${ctx }/static/assets/img/icon-hotline.png">
										400-006-0586
									</p>
		                            <shiro:guest>
		                            <div class="row">
										  <div class="col-md-6">
											  <a class="btn btn-default btnAD" href="javascript:void(0)" onClick="$('#loginBtn').click()">
															<span>一键</span>
															 投广告										  
											  </a>
										  </div>
										  <div class="col-md-6">
											  <a class="btn btn-default btnAD" href="javascript:void(0)" onClick="$('#loginBtn').click()">
												  			<span>一键</span>
															 接广告
											  </a>
										  </div>
									</div>
		                            </shiro:guest>
		                            
		                            <shiro:authenticated>
		                            <div class="row">
		                            	<div class="col-md-12">
									  		<shiro:hasRole name="provider">
													  	<a class="btn btn-default btnAD" href="javascript:void(0)" href="#" id="accept-order">
															<span>一键</span>
															 接广告
														</a>
			                              	</shiro:hasRole>
											<shiro:hasRole name="advertiser">
					                            		<a class="btn btn-default btnAD" href="javascript:void(0)" id="send-order">
															<span>一键</span>
															 投广告
														</a>
					                        </shiro:hasRole>
				                        </div>
									</div>
									</shiro:authenticated>
	                            </div>
	                            <div class="col-md-4">
	                            	<img src="${ctx }/static/assets/img/codeIOS.png">
	                            </div>
                            
                            	<%-- <img src="${ctx }/static/assets/img/qrcode-apple.jpg"> --%>
                            </div>
                        </div> 
                    </div>
                </div>    
        		
                <div class="clearfix"></div>
        
                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse">
                    <div class="container">
                        <div class="row">
                        <div class="cate-title m-category">
                            	<a href="javascript:void(0)">媒体推荐 <i class="fa fa-caret-down"></i></a>
                            	<div style="display:none" class="category">            	
					                <div class="cate-wrap">
					                	<ul class="cate-list">
					                    	<li>
					                        	<div class="cate-item">
					                                <h2 class="title"><a href="${ctx }/weixin"><span class="fa fa-wechat"></span> 微信频道<i class="fa fa-angle-right"></i></a></h2>
					                                <div class="links">
					                                	<c:forEach items="${weixinCategories }" var="item">
					                                    	<a href="${ctx}/weixin?category=${item.itemCode}">${item.itemName }</a>
					                                    </c:forEach>                        
					                                </div>
					                                <div class="tags">
					                                	<a href="${ctx}/weixin?tags=000026">本地粘性强</a>
					                                	<a href="${ctx}/weixin?tags=000074">草根大号</a>
					                                	<a href="${ctx}/weixin?tags=000080">段子手</a>
					                                	<a href="${ctx}/weixin?tags=000022">日活量高</a>
					                                	<a href="${ctx}/weixin?tags=000025">文案水平高</a>
					                                	<a href="${ctx}/weixin?tags=000077">意见领袖</a>
					                                </div>
					                                <div class="all">
					                                    <div class="links">
					                                        <dl class="dl-horizontal">
					                                            <dt><i>类</i> 认证类别：</dt>
					                                            <dd>
					                                            	<c:forEach items="${weixinCategories }" var="item">
								                                    	<a href="${ctx}/weixin?category=${item.itemCode}">${item.itemName }</a>
								                                    </c:forEach>
					                                            </dd>
					                                        </dl>
					                                    </div>                                    
					                                    <div class="tags">
					                                    	<dl class="dl-horizontal">
					                                            <dt><i>类</i> 标签：</dt>
					                                            <dd style="margin-left:70px;">                                        
						                                            <c:forEach items="${recTags }" var="item">
								                                    	<a href="${ctx}/weixin?tags=${item.id}">${item.name }</a>
								                                    </c:forEach>
					                                            </dd>
					                                        </dl>
					                                    </div>
					                                </div>
					                            </div>
					                        </li>
					                    	<li>
					                        	<div class="cate-item">
					                                <h2 class="title"><a href="${ctx }/weibo"><span class="fa fa-weibo"></span> 微博频道<i class="fa fa-angle-right"></i></a></h2>
					                                <div class="links">
					                                    <%-- <c:forEach items="${weiboCategories }" var="item">
						                                    <a href="${ctx}/weibo?category=${item.itemCode}">${item.itemName }</a>
						                                </c:forEach>    --%>
						                                 <a href="${ctx}/weibo?category=WEIBO_C_BLUE">机构认证<b class="color-blue">V</b></a>
					                                
					                                   	 <a href="${ctx}/weibo?category=WEIBO_C_YELLOW">个人认证<b class="color-yellow">V</b></a>
					                                
					                                </div>
					                                <div class="tags">
					                                	<a href="${ctx}/weibo?tags=000080">段子手</a>
					                                	<a href="${ctx}/weibo?tags=000078">娱乐明星</a>
					                                	<a href="${ctx}/weibo?tags=000075">网络红人</a>
					                                	<a href="${ctx}/weibo?tags=000076">行业名人</a>
					                                	<a href="${ctx}/weibo?tags=000077">意见领袖</a>
					                                	<a href="${ctx}/weibo?tags=000025">文案水平高</a>
					                                </div>
					                                <div class="all">
					                                    <div class="links">
					                                        <dl class="dl-horizontal">
					                                            <dt><i>类</i> 认证类别：</dt>
					                                            <dd>
						                                            <c:forEach items="${weiboCategories }" var="item">
									                                	<a href="${ctx}/weibo?category=${item.itemCode}">${item.itemName }</a>
									                                </c:forEach>
					                                            </dd>
					                                    	</dl> 
					                                    </div>
					                                    <div class="tags">
					                                        <dl class="dl-horizontal">
					                                            <dt><i>类</i> 标签：</dt>
					                                            <dd style="margin-left:70px;">
					                                                <c:forEach items="${recTags }" var="item">
								                                    	<a href="${ctx}/weibo?tags=${item.id}">${item.name }</a>
								                                    </c:forEach>
					                                            </dd>
					                                        </dl>
					                                	</div>
					                                </div>
					                            </div>
					                        </li>
					                    	<li>
					                        	<div class="cate-item">
					                        		<shiro:guest>
						                                <h2 class="title"><a href="javascript:void(0)" onClick="$('#loginBtn').click()"><span class="fa fa-send"></span> 广告悬赏</a></h2>
						                                <div class="links">
						                                    <c:forEach items="${mediaTypes }" var="item">
							                                    <a href="javascript:void(0)" onClick="$('#loginBtn').click()">${item.itemName }</a>
							                                </c:forEach>                      
						                                </div>
					                        		</shiro:guest>
					                        		<shiro:authenticated>
					                                <h2 class="title"><a href="${ctx}/plaza"><span class="fa fa-send"></span> 广告悬赏</a></h2>
					                                <div class="links">
					                                    <c:forEach items="${mediaTypes }" var="item">
						                                    <a href="${ctx}/plaza?mediaType=${item.itemCode}">${item.itemName }</a>
						                                </c:forEach>                      
					                                </div>
					                        		</shiro:authenticated>
					                            </div>
					                        </li> 
					                        <li>
					                        	<div class="cate-item">
					                                <h2 class="title"><a href="${ctx}/other"><span class="fa fa-group"></span> 更多媒体<i class="fa fa-angle-right"></i></a></h2>
					                                <div class="tags">
					                                    <%-- <c:forEach items="${otherMediaCategories }" var="item">
										                    <a href="${ctx }/other?search_EQ_category=${item.itemCode}" > ${item.itemName}</a>
										                </c:forEach>   --%>
									                    <a href="${ctx}/other?search_EQ_category=MEDIA_C_PORAL" >门户网站</a>
									                    <a href="${ctx}/other?search_EQ_category=MEDIA_C_NEWSPAPER" >报纸广告</a>
									                    <a href="${ctx}/other?search_EQ_category=MEDIA_C_APP" >APP广告</a>
									                    <a href="${ctx}/other?search_EQ_category=MEDIA_C_VIDEO" >视频网站</a>
									                    <a href="${ctx}/other?search_EQ_category=MEDIA_C_OUTDOOR" >户外广告</a>
									                    <a href="${ctx}/other?search_EQ_category=MEDIA_C_MAGAZINE" >杂志广告</a>
										                                            
					                                </div>
					                                <div class="all">
					                                    <div class="tags">
					                                        <dl class="dl-horizontal">
					                                            <dt><i>类</i> 类别：</dt>
					                                            <dd style="margin-left:70px;">
					                                            	<c:forEach items="${otherMediaCategories }" var="item">
													                    <a href="${ctx }/other?search_EQ_category=${item.itemCode}" > ${item.itemName}</a>
													                </c:forEach>
					                                            </dd>
					                                         </dl>   		                              
					                                    </div>
					                            	</div>
					                            </div>
					                            
					                        </li>                     
					                    </ul>
					                </div>       
					            </div>
                            </div><!-- /cate-title -->
                            
                        <ul class="nav navbar-nav">
                            <!-- Home -->
                            <li data-id="home"  ><a href="${ctx}/"><i class="fa fa-home"></i>首页</a></li>
                            <shiro:authenticated>
                            <li data-id="plaza" ><a href="${ctx}/plaza"><i class="fa fa-send"></i>广告悬赏</a></li>
                            </shiro:authenticated>
                            <shiro:guest>
	                            <li data-id="plaza" ><a href="javascript:void(0)" onClick="$('#loginBtn').click()"><i class="fa fa-send"></i>广告悬赏</a></li>
                            </shiro:guest>
                            <li data-id="official"><a href="${ctx}/official"><i class="fa fa-user"></i>官方媒体</a></li>
                            <li data-id="weixin"><a href="${ctx}/weixin"><i class="fa fa-wechat"></i>微信频道</a></li>
                            <li data-id="weibo" ><a href="${ctx}/weibo"><i class="fa fa-weibo"></i>微博频道</a></li>
                            <li data-id="other-media" ><a href="${ctx}/other"><i class="fa fa-group"></i>更多媒体</a></li>
                            <li data-id="findCnmei"  ><a target="_blank" href="${ctx }/findCnmei"><i class="fa-findcaimei"></i>发现采媒</a></li>
                        </ul>                        
                        </div>
                    </div><!--/end container-->
                </div><!--/navbar-collapse-->
            </div>            
            <!-- End Navbar -->
		</div>
	</div>
</div>
<!--=== End Header v1 ===-->

<div id="focus" style="display:none">
	<img alt="关注采媒在线" width="300" src="${ctx}/static/assets/img/qrcode-mc.jpg">
</div>

<!--一键下单模态框-->
	<div class="modal fade" id="send-order-modal" tabindex="-1"
		role="dialog" data-width="520" data-height="180" aria-labelledby="myModalLabel"
		data-replace="true">

	</div>
	
<!--一键接单模态框-->
	<div class="modal fade" id="accept-order-modal" tabindex="-1"
		role="dialog" data-width="544" data-height="280" aria-labelledby="myModalLabel"
		data-replace="true">

	</div>
<c:if test="${!empty active}">
<script type="text/javascript">
	var active = '${active}';
	var activeSearch = '${activeSearch}';
</script>
</c:if>
<!-- 导入营销QQ -->
<div id="qqWpa">
	<!-- WPA Button Begin -->
	<script charset="utf-8" type="text/javascript" src="http://wpa.b.qq.com/cgi/wpa.php?key=XzkzODA1NDcyNF8zNjI2NTJfNDAwMDA2MDU4Nl8"></script>
	<!-- WPA Button End -->
</div>

<!-- 右侧浮动内容 begin -->
<div class="floatRShow">
    <shiro:authenticated>
        <div class="floatRCon shopCarBor hasGoods">
		  	<a href="javascript:void(0);" class="shopCar" id="car-title"><span id="car-num"></span></a>
		</div>
    </shiro:authenticated>
    <shiro:guest>
        <div class="floatRCon shopCarBor hasGoods">
		  	<a href="javascript:void(0);" onClick="$('#loginBtn').click()" class="shopCar"><span>0</span></a>
		</div>
    </shiro:guest>

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
<script type="text/template" id="cartTemp">
	<li>
    	<img src="{showPic}" class="photo">
        <div class="imgtxt-info">
        	<a href="${ctx}/${param.prefix}/detail/{id}" target="_blank" class="tt">{name}</a>
            <p>类别：<span><c:choose>
								<c:when test="{category==null}">{category}</c:when>
								<c:otherwise>{categoryName}</c:otherwise>
						  </c:choose>
					</span>  行业：<span>{industryTypes}</span>  粉丝：<span>{fans}</span></p>
        </div>
        <div class="toolbar">
        	<span class="btn-close" onclick="javascript:delMediafromCart('{id}');"></span>
        </div>
    </li>
</script>

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
	$.post("${ctx}/shoppingcart/getNum", function(result){
		$('#car-num').text(result?result:0);
	});
	
	//默认高亮“首页”
	var activeId = (typeof active) != 'undefined' ? active : 'home';
	//common.log(activeId);
	$('ul.navbar-nav li').each(function() {
		if( $(this).data('id') == activeId ) {
			$(this).addClass('active');
			return false;
		}
	});
	var activeSearchId = (typeof activeSearch) != 'undefined' ? activeSearch : 'weixin';
	var hasDefault = false;
	$('.search-wrap .search-tabs li').each(function() {
		if( $(this).data('value') == activeSearchId ) {
			$(this).addClass('active');
			$('#data-type').val($(this).data('value'));
			$('#searchValue').attr('placeholder',$(this).data('placeholder'));
			hasDefault = true;
			return false;
		}
    });
	//初始化搜索栏的状态
	if (!hasDefault) {
		var $firstTab = $('.search-wrap .search-tabs li:first').addClass('active');
		$('#data-type').val($firstTab.data('value'));
		$('#searchValue').attr('placeholder',$firstTab.data('placeholder'));
	}
	//弹出登录框
	$('#loginBtn').click(function() {
		var modal = '<div class="modal fade modal-sm" id="login-modal" tabindex="-1" role="dialog" data-focus-on="input:first" data-width="350" ></div>';
		var $loginModal = $('#login-modal');
		if( $loginModal.length == 0 ) {
			$loginModal = $(modal);
			$('body').append($loginModal);
		}
		var url = window.location.href;
		$loginModal.loadModal('${ctx}/loginModal?ajax', {savedURL:url});
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
	
	
	$('#data-search').click(function() {
		var value = $('#data-type').val();
		var searchValue = $("#searchValue").val();
		if (value == "req") {
			var $tab = $('.search-wrap .search-tabs li.active');
			if( $tab.data('login') ) {
				$("#loginBtn").click();
			} else {
				window.location.href="${ctx}/plaza?name="+searchValue;
			}
		} else if (value == "weibo") {
			window.location.href="${ctx}/weibo?name="+searchValue;
		} else if (value == "weixin") {
			window.location.href="${ctx}/weixin?name="+searchValue;
		}
	});
	$('.search-wrap .search-tabs li').click(function() {
		$(this).addClass('active').siblings().removeClass('active');        
		var value = $(this).data('value');
		var placeholder = $(this).data('placeholder');
		$('#data-type').val(value);
		$('#searchValue').attr('placeholder',placeholder);
    });

	
	//弹出一键下单模态框
	$('#send-order').click(function() {
		$('#send-order-modal').loadModal('${ctx}/order/direct/send?ajax');
	});
	//弹出一键接单模态框
	$('#accept-order').click(function() {
		$('#accept-order-modal').loadModal('${ctx}/order/direct/accept?ajax');
	});
	
	//键盘点击事件
	$(document).keypress(function(event){
// 		console.log(event);
		if(event.keyCode==13){
			$('#data-search').click();
		}
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
