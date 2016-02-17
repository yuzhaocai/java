<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>页面未找到-采媒在线</title>
    <link rel="stylesheet" href="${ctx }/static/assets/css/fals.css">
</head>

<body>
<!--=== Container ===-->
    <div class="container product">
        <div class="row">
            <div class="falsReminder">
                <h3>抱歉！页面无法访问......</h3>
                <p>可能因为:</p>
                <p><span>网站有错误&gt;</span>请检查地址是否完整或存在多余字符</p>
                <p><span>网址已失效&gt;</span>可能页面已删除，活动已下线等</p>
                <p><a href="http://www.cnmei.com/cms/faq">了解更多原因&gt;&gt;</a></p>
                <p><a href="javascript:void(0);" id="haveQuestion">我要提问</a></p>
                <p>
               		或者逛逛：
                	<a href="${ctx}/">采媒在线首页</a>
        	        <shiro:guest>
                        <a href="javascript:void(0);" onclick="$('#loginBtn').click()">会员中心</a>
                    </shiro:guest>
                    <shiro:authenticated>
                    	<a href="${ctx }/member/userInfo">会员中心</a>
                    </shiro:authenticated>
                	
                </p>
            </div>
            <div role="tablist" class="tablist tab-v4 margin-bottom-10">
                <ul class="nav nav-tabs tabTop">
                    <li class="active"><a href="#a" aria-controls="a" role="tab" data-toggle="tab">优媒推荐</a></li>
                    <li><a href="#b" aria-controls="b" role="tab" data-toggle="tab">新媒亮相</a></li>
                    <li><a href="#c" aria-controls="c" role="tab" data-toggle="tab">名企案例</a></li>
                    <li><a href="#d" aria-controls="d" role="tab" data-toggle="tab">媒体黑马</a></li>
                    <li><a href="#e" aria-controls="e" role="tab" data-toggle="tab">接单冠军</a></li>
                </ul>
                <div class="tab-content">
                    <!-- 开始  优媒推荐 -->
                    <div role="tabpanel" class="tab-pane active" id="a">
                        <div class="row">
                            <c:if test="${weixinRec!=null }">
						 	<c:forEach items="${mediaRec }" var="item" begin="0" end="5">
			           			<div class="col-xs-2">
									<div class="thumbnails thumbnail-style <c:if test="${item.coop }">stick</c:if>">
										<a class="fancybox-button zoomer" data-rel="fancybox-button" title="${item.title }" href="${item.link }" target="_blank">
											<img src="<zy:fileServerUrl value="${item.pic }"/>">                                                      
										</a>                    
										<div class="caption">
										    <h3><a class="hover-effect" href="${item.link }" target="_blank">${item.title }</a></h3>
									    </div>
			  						</div>
								</div>
		                   	</c:forEach>
							</c:if>
                        </div>
                    </div>
                    <!-- 结束 优媒推荐 --> 
                                  
                    <!-- 开始  新媒亮相 -->
                    <div role="tabpanel" class="tab-pane" id="b">
                        <div class="row"></div>
                    </div>
                    <!-- 结束新媒亮相 -->
                    
                  	<!-- 开始  名企案例 -->
                    <div role="tabpanel" class="tab-pane" id="c">
                        <c:if test="${enterpriseCsut!=null }">
			 			<c:forEach items="${enterpriseCsut }" var="item" begin="0" end="5">
			           		<div class="col-xs-2">
								<div class="thumbnails thumbnail-style">
									<a class="fancybox-button zoomer" data-rel="fancybox-button" title="${item.title }" href="${item.link }" target="_blank">
									<img src="<zy:fileServerUrl value="${item.pic }"/>">                                                      
									</a>                    
									<div class="caption">
			    						<h3><a class="hover-effect" href="${item.link }" target="_blank">${item.title }</a></h3>
			       					</div>
							   	</div>
							</div>
	                   	</c:forEach>
						</c:if>
                    </div>
                    <!-- 结束 名企案例 -->
                    
                    <!-- 开始  媒体黑马 -->
                    <div role="tabpanel" class="tab-pane" id="d">
                        <div class="row"></div>
                    </div>
                    <!-- 结束  媒体黑马 -->
                    
                    <!-- 开始  接单冠军 -->
                    <div role="tabpanel" class="tab-pane" id="e">
                        <div class="row"></div>
                    </div>
                    <!-- 结束 接单冠军 -->
                </div>
            </div>
            <div class="partner">
                <div><h4><b>合作伙伴</b></h4></div>
                <ul class="list-unstyled clearfix">
                    <c:if test="${partnerMedia!=null }">
	                	<c:forEach items="${partnerMedia }" var="item" begin="0" end="9">
	                		<li><a target="_blank" href="${item.link }"><img src="<zy:fileServerUrl value="${item.pic }"/>"></a></li>
	                	</c:forEach>
	                </c:if>
                </ul>
                <hr class="hr-md" style="border-style:dashed; border-color:#ccc;">
                <h4><b>友情链接</b></h4>
                <div class="friendlylink">
                    <c:if test="${friendlyLink!=null }">
	                	<c:forEach items="${friendlyLink }" var="item" begin="0" end="17">
                    		<a href="${item.link }" target="_blank">${item.title }</a>
	                	</c:forEach>
                	</c:if>                 
                </div>
            </div><!--//合作伙伴 -->            
        </div>    
    </div>
<!--=== End Container ===-->
	<script type="text/template" id="media-temp">
		<div class="col-xs-2">
	       <div class="thumbnails thumbnail-style ">
		       <a class="fancybox-button zoomer {stick}" data-rel="fancybox-button" title="{name}" href="${ctx}/{type}/detail/{id}" target="_blank">
		          <img src="{showPic}" alt="{name}">                                                       
		       </a>                    
		       <div class="caption">
		           <h3><a class="hover-effect" href="${ctx}/{type}/detail/{id}" target="_blank">{name}</a></h3>
		       </div>
		   </div>
		</div>
	</script>
	
	<script type="text/javascript">
		var $mediaTemp = $('#media-temp');
		$(function(){
			
			$('.navbar-collapse .container').hide();
			
			$('.floatRShow').hide();

			//我要提问
			$('#haveQuestion').click(function(){
				$('#complaintControl').click();
			})
			
			//加载  新媒亮相
			load({pageSize: 6, sort:'createTime desc'}, '#b > .row', $mediaTemp);
			
			//加载  媒体黑马
			loadAd({adType: "d"}, '#d > .row', $mediaTemp);
			
			//加载  接单冠军
			loadAd({adType: "e"}, '#e > .row', $mediaTemp);
			
			
		});
		
		function loadAd(params, selector, $template) {
			$.get('${ctx}/loadAd?ajax', params, function(rsp) {
				if( rsp.result ) {
					var html = [];
					$.each(rsp.data, function(i, o) {
						if (o.cooperate){
							o.stick = 'stick';
						}
						html.push($template.format(o));
					});
					$(selector).html(html.join(''));
				}
			});		
		}
		
		function load(params, selector, $template) {
			$.get('${ctx}/load?ajax', params, function(rsp) {
				if( rsp.result ) {
					var html = [];
					$.each(rsp.page.data, function(i, o) {
						if (o.cooperate){
							o.stick = 'stick';
						}
						html.push($template.format(o));
					});
					$(selector).html(html.join(''));
				}
			});		
		}
	</script>
</body>
</html>