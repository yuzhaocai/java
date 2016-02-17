<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
	
<!--=== Content ===-->
<div class="container">
	<div class="row">
		<!-- 顶部border start-->
		<div class="prod-page">
			<div class="prod-detail-wrap">
				<div class="prod-detail-meta">
					<div class="prod-img">
						<div class="prod-img-left <c:if test="${cooperate}"> stick </c:if>"><img src="${media.showPic }" alt="${media.name}" ></div>
						<div class="prod-text"> 
							<c:if test="${!empty mediaTags}">
								<dl>
									<dt>标签：</dt>
									<dd class="tags-list">
										<ul class="list-inline">
											<c:forEach items="${mediaTags }" var="item">
												<li>${item.name }</li>
											</c:forEach>
										</ul>
									</dd>
								</dl>
							</c:if>
						</div>
					</div>
					<div class="prod-block">
						<h4 class="prod-title">
							<c:choose>
								<c:when test="${media.mediaType eq 'MEDIA_T_WEIXIN' }">
									微信昵称：<b>${media.name }</b>
								</c:when>
								<c:when test="${media.mediaType eq 'MEDIA_T_WEIBO' }">
									微博昵称：<b>${media.name }</b>
								</c:when>
							</c:choose>
						</h4>
						<div class="prod-text fit-prod">
<%-- 							<c:if test="${media.mediaType eq 'MEDIA_T_WEIXIN' }"> --%>
								<dl class="pro-list" >
									<dt style="width:80px">适用产品：</dt>
									<dd>
										<ul class="list-inline">
											<c:forEach items="${mediaProductsList }" var="item" varStatus="status" >
												<c:if test="${status.index < 5}" >
													<li>
														<zy:dic value="${item }"/>
													</li>
												</c:if>
											</c:forEach>
										</ul>
									</dd>
									<c:if test="${fn:length(mediaProductsList) > 5}" >
										<i class="fa fa-caret-down product-show "></i>
									</c:if>
								</dl>
								<dl class="pro-list active" style="display:none">
	                                <dt style="width:80px;">适用产品：</dt>
	                                <dd>
	                                    <ul class="list-inline">
	                                        <c:forEach items="${mediaProductsList }" var="item" varStatus="status" >
												<li>
													<zy:dic value="${item }"/>
												</li>
											</c:forEach>
	                                    </ul>
	                                </dd>
	                                <c:if test="${fn:length(mediaProductsList) > 5}" >
		                                <i class="fa fa-caret-up product-show"></i>
									</c:if>
                        		</dl>
<%-- 							</c:if> --%>
						</div>
						<div class="prod-text-main">
	                      	 <div class="row">
	                          	<div class="col-xs-6 prod-name">
									<c:if test="${media.mediaType eq 'MEDIA_T_WEIXIN' }">
										微信号：
	                          			<span>${media.account }</span>
									</c:if>
	                          	</div>
	                         	<div class="col-xs-6 text-right"><i class="fa fa-star"></i> 粉丝数：<b class="highlight">${media.fans }</b></div>
	                       	</div>
	                       	<div class="row">
	                          	<div class="col-xs-4">认证类别：<zy:dic value="${media.category }"/></div>
	                           		<c:if test="${!empty mediaFansDirList }">
			                           	<div class="col-xs-8 text-right">粉丝方向：
												<c:forEach items="${mediaFansDirList }" var="item" >
														<zy:dic value="${item }"/>
												</c:forEach>
										</div>
									</c:if>
	                       	</div>
	                   	</div>
	                   	<form id="create-form" class="form-horizontal" action="${ctx}/member/req/advertiser/create/detailCreate?medias=${media.id}"  method="post">
		                   	<div class="prod-text">
							   	<dl>
									<dt>类型：</dt>
									<dd>
										<ul class="list-inline">
											<c:forEach items="${mediaIndustryTypeList }" var="item" >
												<li><zy:dic value="${item }"/></li>
											</c:forEach>
										</ul>
									</dd>
								</dl>
								<dl>
									<dt>地区：</dt>
									<dd>
										<ul class="list-inline">
											<c:forEach items="${mediaRegionList }" var="item" >
												<li>
														<zy:area id="${item }"/>
												</li>
											</c:forEach>
										</ul>
									</dd>
								</dl>
								<dl class="selectQuotes">
									<dt style=" padding-top" >选择：</dt>
									<dd class="prod-size">
										<shiro:guest>
										请登陆后查看报价
										</shiro:guest>
										<shiro:authenticated>
										<c:choose>
											<c:when test="${!empty media.mediaQuotes }">
												<c:forEach items="${media.mediaQuotes }" var="item" varStatus = "status">
													<a href="javascript:void(0)" class="${item.type} categories" data-value="${item.id}" >
											    		<zy:dic value="${item.type }" />
											    		<b>￥${item.price }</b>
										    		</a>
							                    </c:forEach>
											</c:when>
											<c:otherwise>暂无报价</c:otherwise>
										</c:choose>
										</shiro:authenticated>
									</dd>
								</dl>	
								<div class="form-group has-feedback" style="display:none">
									<input type="hidden" name="mediaQuote">
								</div>
								<dl style="margin-top:20px;">
		                	        <dt></dt>
		                            <dd class="btnbar">
		                            	<shiro:guest>
		                            	<button type="button" class="btn-u btn-u-red" data-toggle="modal" data-target="#create-req" id="btn-login" >立即登录</button>
		                            	</shiro:guest>
		                            	<shiro:authenticated>
			                        	<c:if test="${!empty media.mediaQuotes }">
			                        		<c:choose>
			                        			<c:when test="${cust.custType eq 'CUST_T_ADVERTISER' }">
			                        			<ul class="list-inline">
			                        				<li>
				                        			<button type="button" class="btn-u btn-u-red" data-toggle="modal" data-target="#create-req" id="btn-submit" >立即邀约</button>
				                        			</li>
			                        				<li>
				                        			<button type="button" class="btn-u btn-u-red" data-toggle="modal" data-target="#create-req" id="btn-add" >加入采媒车</button>
				                        			</li>
			                        				<li>
				                        			<button type="button" class="btn-u btn-u-red" data-toggle="modal"  id="btn-favorites" >收藏</button>
				                        			</li>
				                        			<li>
				                        			<div id="selectQuotes-error" style="display:none">请选择服务类型</div>
				                        			</li>
				                        		</ul>
				                        		</c:when>
			                        			<c:when test="${cust.custType eq 'CUST_T_PROVIDER' }">
			                        			<ul class="list-inline">
			                        				<li>
				                        			<button type="button" class="btn-u btn-u-red" data-toggle="modal" data-target="#create-req" id="btn-provider" >立即邀约</button>
				                        			</li>
				                        			<li>
				                        			<div id="custType-error" style="display:none">请使用广告主账户登录</div>
				                        			</li>
				                        		</ul>
				                        		</c:when>
			                        		</c:choose>
				                        </c:if>
				                        <c:if test="${empty media.mediaQuotes }">
				                        	<c:if test="${cust.custType eq 'CUST_T_ADVERTISER' }">
				                        	<button type="button" class="btn-u btn-u-red" data-toggle="modal"  id="btn-favorites" >收藏</button>
				                        	</c:if>
				                        </c:if>
				                        </shiro:authenticated>
		                            </dd>
		                        </dl>
							</div>
						</form>
                   	</div>
				</div>
			</div>
			<div class="main-wrap">
				<div class="prod-detail-nav">
					<div id="prod-detail-nav">
						<ul class="nav navbar-nav">
							<li class="active"><a href="#a">媒体简介</a></li>
							<c:if test="${!empty media.mediaQuotes }">
								<li><a href="#e">服务报价</a></li>
							</c:if>
							<c:if test="${!empty media.mediaCases }">
								<li><a href="#f">案例</a></li>
							</c:if>
						</ul>
					</div>
				</div> 	
				<div class="panel panel-default" id="a">
					<div class="panel-heading hide"><h4>媒体简介</h4></div>
					<div class="panel-body">
						<p class="pre">${media.description }</p>
					</div>
				</div>
				
				<c:if test="${!empty media.mediaQuotes }">
					<div class="panel panel-default" id="e">
						<div class="panel-heading"><h4>服务报价</h4></div>
						<div class="panel-body">
							<shiro:guest>
							请登陆后查看报价
							</shiro:guest>
							<shiro:authenticated>
							<ul class="sub-prod-item">
								<c:forEach items="${media.mediaQuotes }" var="item">
									<li>
										<div class="sub-prod">
											<div class="sub-prod-img">
											<c:choose>
												<c:when test="${item.type eq 'WEIXIN_S_SINGLE_P'}">
													<img src="${ctx}/static/assets/img/detail/small/weixin_single_p.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weixin_single_p.png"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIXIN_S_MULTI_P_F'}">
													<img src="${ctx}/static/assets/img/detail/small/weixin_single_p_f.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weixin_single_p_f.png"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIXIN_S_MULTI_P_S'}">
													<img src="${ctx}/static/assets/img/detail/small/weixin_single_p_s.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weixin_single_p_s.png"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIXIN_S_MULTI_P_T'}">
													<img src="${ctx}/static/assets/img/detail/small/weixin_single_p_t.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weixin_single_p_t.png"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIXIN_S_MULTI_P_O'}">
													<img src="${ctx}/static/assets/img/detail/small/weixin_single_p_o.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weixin_single_p_o.png"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIXIN_S_SLICE'}">
													<img src="${ctx}/static/assets/img/detail/small/weixin_s_slice.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weixin_s_slice.png"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIXIN_S_FRIEND'}">
													<img src="${ctx}/static/assets/img/detail/small/weixin_s_friend.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weixin_s_friend.png"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIBO_S_01'}">
													<img src="${ctx}/static/assets/img/detail/small/weibo_s_01.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weibo_s_01.jpg"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIBO_S_02'}">
													<img src="${ctx}/static/assets/img/detail/small/weibo_s_02.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weibo_s_02.jpg"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIBO_S_03'}">
													<img src="${ctx}/static/assets/img/detail/small/weibo_s_03.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weibo_s_03.jpg"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIBO_S_04'}">
													<img src="${ctx}/static/assets/img/detail/small/weibo_s_04.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weibo_s_04.jpg"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIBO_S_05'}">
													<img src="${ctx}/static/assets/img/detail/small/weibo_s_05.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weibo_s_05.jpg"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIBO_S_06'}">
													<img src="${ctx}/static/assets/img/detail/small/weibo_s_06.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weibo_s_06.jpg"></div>
												</c:when>
												<c:when test="${item.type eq 'WEIBO_S_07'}">
													<img src="${ctx}/static/assets/img/detail/small/weibo_s_07.jpg">
		                                        	<div class="sourse"><span></span><i></i><img src="${ctx}/static/assets/img/detail/weibo_s_07.jpg"></div>
												</c:when>
											</c:choose>
											</div>
											<div class="sub-prod-block">
												<dl class="dl-horizontal ">
													<dt>类型：</dt><dd><zy:dic value="${item.type }" /></dd>
	                                        		<dd><button type="button" class="${item.type } priceBtn" data-value="${item.id}">￥${item.price }</button></dd>
												</dl>
											</div>
										</div>
									</li>
				 				</c:forEach>
							</ul>
							</shiro:authenticated> 
						</div>
					</div>
				</c:if>
				<c:if test="${!empty media.mediaCases }">
					<div class="panel panel-default" id="f">
						<div class="panel-heading"><h4>案例</h4></div>
						<c:forEach items="${media.mediaCases }" var="item" varStatus="status">
							<div class="panel-body">
								<dl class="dl-horizontal case-block">
									<dt>案例标题：</dt><dd class="case-title">${item.title }</dd>
									<dt>案例亮点：</dt><dd>${item.light }</dd>
									<dt>案例正文：</dt><dd><p class="pre">${item.content }</p></dd>
								</dl>
								<ul class="case-img-item">
									<c:set var="arr" value="${item.showPic }" />
									<c:forEach items="${fn:split(arr,',')}" var="addr">
										<c:if test="${addr ne ''}">
												<li><img src="${addr }"></li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</c:forEach>
					</div>
				</c:if>
			</div>
		</div>
    </div>
</div>  
<!-- 返回顶部按钮 -->
<div id="topcontrol" style="position: fixed; bottom: 5px; right: 5px; opacity: 0; cursor: pointer;" title="Scroll Back to Top"></div>  
<!--=== End Content ===-->
<!-- 返回顶部 -->
<script src="${ctx}/static/assets/plugins/back-to-top.js" type="text/javascript"></script>
<script type="text/template" id="recommendTemp">
<li>
    <div class="thumbnail-v2">
        <div class="toparea">
            <div class="photo-wrap">
                <img src="{showPic}" class="photo">
                <span class="photo-mask"></span>
            </div>
            <div class="info-wrap">
                <ul class="ul-detail">
                    <li class="tt"><a href="${ctx}/${param.prefix}/detail/{id}" target="_blank">{name}</a></li>                                    
                    <li><p>类别：<span>{category}</span></p><p><a href="${ctx}/${param.prefix}/detail/{id}" target="_blank" class="highlight">查看报价</a></p></li>
                    <li><p>粉丝：<span>{fans}</span></p><p>行业：<span>{industryTypes}</span></p></li>
                </ul>
            </div>
        </div>
        
        <div class="innerarea">
            <ul class="ul-detail">
                <li>简介：<span>{description}</span></li>
            </ul>
        </div>
    </div>                
</li>
</script>
<script type="text/javascript">
	function loadRecommendMedias() {
		var url = '${ctx}/${param.prefix}/loadRecommendMedias?ajax';
		$.get(url, function(rsp) {
			rsp = $.parseJSON(rsp);
			if( rsp.result ) {
				var $medias = $('#recommend');
				var $temp = $('#recommendTemp');
				var html = [];
				$.each(rsp.page.data, function(i, o) {
					html.push($temp.format(o));
				});
				$medias.html(html.join(''));
			} else {
				//TODO
			}
		});
	}
	function isFavorites(){
		var url = '${ctx}/member/favoritesMedia/isFavorites?ajax';
		var fid = '${media.id}';
		var mediaType = '${media.mediaType}'
		var type;
		if ('MEDIA_T_WEIBO' == mediaType){
			type = 'FAVORITES_WEIBO';
		} else {
			type = 'FAVORITES_WEIXIN';
		}
		$.post(url, {mediaId:fid,type:type}, function(rsp) {
			rsp = $.parseJSON(rsp);
			if (rsp.favorites){
				$('#btn-favorites').text("取消收藏");
				$('#btn-favorites').data().url = '${ctx}/member/favoritesMedia/delete';
			} else {
				$('#btn-favorites').text("收藏");
				$('#btn-favorites').data().url = '${ctx}/member/favoritesMedia/add';
			}
		});
	}
	isFavorites();
	$('#btn-favorites').click(function() {
		var url = $('#btn-favorites').data().url;
		var fid = '${media.id}';
		var mediaType = '${media.mediaType}'
		var type;
		if ('MEDIA_T_WEIBO' == mediaType){
			type = 'FAVORITES_WEIBO';
		} else {
			type = 'FAVORITES_WEIXIN';
		}
		var favorites;
		var text = $(this).text();
		var msg;
		if (text == '收藏'){
			favorites = false;
		} else {
			favorites = true;
		}
		if (favorites == true) {
			bootbox.confirm("您确定要取消收藏？",function(result){
				if(result){
					$.post(url, {mediaId:fid,type:type}, function(rsp) {
						rsp = $.parseJSON(rsp);
						if (rsp.result){
							$('#btn-favorites').text("收藏");
							$('#btn-favorites').data().url = '${ctx}/member/favoritesMedia/add';
						} else {
							
						}
					});	
				}
			});
		} else {
			$.post(url, {mediaId:fid,type:type}, function(rsp) {
				rsp = $.parseJSON(rsp);
				if (rsp.result){
					$('#btn-favorites').text("取消收藏");
					$('#btn-favorites').data().url = '${ctx}/member/favoritesMedia/delete';
					common.showMessage("收藏成功");
				} else {
					
				}
			});
		}
	});
	// 执行筛选
	function setValue(el, name) {
		var value = $(el).data('value');
		$('#create-form').find('[name="' + name + '"]').val(value);
	}
	$("#create-form").validate({ 
		debug: false,
		submitHandler: function(form) {
			if( $('[name="mediaQuote"]').val() == null || $('[name="mediaQuote"]').val() == "") {
				/* var opts = {life: 2000};
				opts.position='bottom-right';
				opts.theme = 'warning';
				$.jGrowl('请选择服务类型', opts); */
				$('#selectQuotes-error').show();
				$('#selectQuotes-error').css("color", 'red');
				return false;
			}
			form.submit(); 
		}, 
		rules: {
			mediaQuote:{
				required: true, 
			}
		},
		messages: {
			mediaQuote: {
				required: '请选择服务类型'
			}
		}
	});
	
	
	//高亮筛选条件
	function activePrice(el) {
		var className = $(el).attr('class');
		className = className.replace("categories", "").replace("priceBtn", "");
		$('.selectQuotes > .prod-size > .active').each(function(){
			$(this).removeClass('active');
		});
		$('.priceBtn').each(function(){
			$(this).removeClass('active');
		});
		$('.' + className).each(function(){
			var value = $(this).data('value');
			if( '' != value ) {
				$(this).addClass('active');
			}
		});
	}
	
	
	$('.categories').click(function() {
		activePrice(this);
		setValue(this, 'mediaQuote');
		$('#selectQuotes-error').delay(1).hide(0);
	});
	
	$('.priceBtn').click(function() {
		activePrice(this);
		setValue(this, 'mediaQuote');
		$('#selectQuotes-error').delay(1).hide(0);
	});
	$('.product-show').click(function(){
		var parent = $(this).closest(".pro-list");
		if ($(this).hasClass("fa-caret-down")) {
			parent.hide();
			parent.next('.pro-list').show();
		} else {
			parent.hide();
			parent.prev('.pro-list').show();
		}
	});
	
	
	$(function() {
		//动画效果div
		$addGoods=$('<div class="addGood">+1</div>');
		$('#btn-add').append($addGoods);
		$('.addGood').hide();
		
// 		loadRecommendMedias();
		$('#btn-submit').click(function() {
			$('#create-form').submit();
		});
		$('#btn-add').click(function() {
			if( $('[name="mediaQuote"]').val() == null || $('[name="mediaQuote"]').val() == "") {
				/* var opts = {life: 2000};
				opts.position='bottom-right';
				opts.theme = 'warning';
				$.jGrowl('请选择服务类型', opts); */
				$('#selectQuotes-error').show();
				$('#selectQuotes-error').css("color", 'red');
				return false;
			}
			var param = $('#create-form').serialize();
			genCartHtml('${ctx}/shoppingcart/addMediasFromDetail',param);
		});
		$('#btn-login').click(function() {
			$('#loginBtn').click();
		});
		$('#btn-provider').click(function() {
			$('#custType-error').show();
			$('#custType-error').css("color", 'red');
		});
	});
	//添加媒体之购物车
	function addInviteMedia(id,name,imgSrc,category,industryTypes,fans,prices){
		var param = {"id":id,"name":name,"imgSrc":imgSrc,"category":category,"industryTypes":industryTypes,"fans":fans,'prices':prices};
		genCartHtml('${ctx}/shoppingcart/addMediasFromDetail',param);
	}
	/**
	 * 显示购物车中媒体信息
	 */
	function genCartHtml(url,param){
		var $temp = $('#cartTemp');
		$.post(url,param,function(result){
			var medias = eval(result['mediaQuotes']);
			$('#car-num').text(medias.length);
			var opts = {life: 1000};
			if(result['msg']=='成功添加该媒体至采媒车'){
				$('.addGood').show().animate({opacity:'0','bottom':'336px','right':'45px'},1000,function(){
					  $(this).css({'position':'fixed','opacity':'1','bottom':'320px','right':'100px','display':'none'});
					  return false;
				});
			}else{
				common.showMessage(result['msg'], opts);
			}
		},'json');
	}
	function creatReq(){
		window.location.href='${ctx}/shoppingcart/createReqToStep3';
	}
	
	$(window).scroll(function(e) {
		if($(window).scrollTop() >= $('.main-wrap').offset().top){
			$('.prod-detail-nav').addClass('affix');	
			$("body").scrollspy({ target: '.prod-detail-nav' })//产品规格导航激活滚动监听	
		}else {
			$('.prod-detail-nav').removeClass('affix');
		}
    });
</script>



