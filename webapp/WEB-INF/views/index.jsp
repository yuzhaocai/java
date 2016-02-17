<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>采媒在线</title>
</head>

<body>
<!--=== 首屏 ===--> 
<div class="container">
	<div class="row">
    	<div class="prime">
            <div class="category show">            	
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
            <!-- Magazine Slider -->
            <div class="carousel slide" id="prime-carousel">
                <ol class="carousel-indicators">
            	<c:choose>
            		<c:when test="${首页海报[0] != null}" >
            			<c:forEach items="${首页海报}" var="item" varStatus="status" >
		                    <li class="rounded-x <c:if test="${status.index == 0}"> active </c:if>" data-target="#prime-carousel" data-slide-to="${status.index }"></li>
                    	</c:forEach>
                    </c:when>
                    <c:otherwise>
                    	<li class="active rounded-x" data-target="#prime-carousel" data-slide-to="0"></li>
                    	<li class="rounded-x" data-target="#prime-carousel" data-slide-to="1"></li>
                    </c:otherwise>
                </c:choose>
                </ol>
                <div class="carousel-inner">
                    <c:choose>
                    	<c:when test="${首页海报[0] != null}" >
                    		<c:forEach items="${首页海报}" var="item" varStatus="status" >
		                    <div class="item <c:if test="${status.index == 0}"> active </c:if>">
		                        <a href="${item.link }" title="${item.title }" ><img src="<zy:fileServerUrl value="${item.pic }"/>" width="700" height="400"  ></a>
		                    </div>
		                    </c:forEach>
                    	</c:when>
                    	<c:otherwise>
                    		<div class="item metro active">
                        	<div class="metro-block">
                        	<c:choose>
                        		<c:when test="${像素墙A1[0] != null}">
                        			<a href="${像素墙A1[0].link }" class="block block2"><img src="<zy:fileServerUrl value="${像素墙A1[0].pic }"/>"><div class="info">${像素墙A1[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block2"><img src="http://202.85.221.165/static/meilifang/assets/img/banners/b210x100.jpg"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
							<c:choose>
                        		<c:when test="${像素墙A2[0] != null}">
									<a href="${像素墙A2[0].link }" class="block block1"><img src="<zy:fileServerUrl value="${像素墙A2[0].pic }"/>"><div class="info">${像素墙A2[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block1"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/lkw.png" width="215" height="194"/><div class="info">刘恺威</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙A3[0] != null}">
									<a href="${像素墙A3[0].link }" class="block block1"><img src="<zy:fileServerUrl value="${像素墙A3[0].pic }"/>"><div class="info">${像素墙A3[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block1"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/zly.png" width="215" height="194"/><div class="info">赵丽颖</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙A4[0] != null}">
									<a href="${像素墙A4[0].link }" class="block block2 bg-color-blue"><img src="<zy:fileServerUrl value="${像素墙A4[0].pic }"/>"><div class="info">${像素墙A4[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block2 bg-color-blue"><i class="fa fa-2x fa-search"></i><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙A5[0] != null}">
									<a href="${像素墙A5[0].link }" class="block block2 bg-color-blue"><img src="<zy:fileServerUrl value="${像素墙A5[0].pic }"/>"><div class="info">${像素墙A5[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block2 bg-color-blue"><i class="fa fa-2x fa-play-circle-o"></i><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        </div>
                        <div class="metro-block">
                        	 <c:choose>
                        		<c:when test="${像素墙B1[0] != null}">
									<a href="${像素墙B1[0].link }" class="block block2 bg-color-blue"><img src="<zy:fileServerUrl value="${像素墙B1[0].pic }"/>"><div class="info">${像素墙B1[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block2 bg-color-blue"><i class="fa fa-2x fa-clock-o"></i><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙B2[0] != null}">
									<a href="${像素墙B2[0].link }" class="block block4 bg-color-brown"><img src="<zy:fileServerUrl value="${像素墙B2[0].pic }"/>"><div class="info">${像素墙B2[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block4 bg-color-brown active"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/prod1.jpg"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙B3[0] != null}">
                        			<a href="${像素墙B3[0].link }" class="block block1"><img src="<zy:fileServerUrl value="${像素墙B3[0].pic }"/>"><div class="info">${像素墙B3[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block1"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/lkw.png"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙B4[0] != null}">
                        			<a href="${像素墙B4[0].link }" class="block block1"><img src="<zy:fileServerUrl value="${像素墙B4[0].pic }"/>"><div class="info">${像素墙B4[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block1"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/zly.png"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        </div>
                        
                        <div class="metro-block">
                           	<c:choose>
                        		<c:when test="${像素墙C1[0] != null}">
                        			<a href="${像素墙C1[0].link }" class="block block1"><img src="<zy:fileServerUrl value="${像素墙C1[0].pic }"/>"><div class="info">${像素墙C1[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block1"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/zly.png"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙C2[0] != null}">
                        			<a href="${像素墙C2[0].link }" class="block block1"><img src="<zy:fileServerUrl value="${像素墙C2[0].pic }"/>"><div class="info">${像素墙C2[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block1"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/zly.png"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙C3[0] != null}">
                        			<a href="${像素墙C3[0].link }" class="block block2 bg-color-dark"><img src="<zy:fileServerUrl value="${像素墙C3[0].pic }"/>"><div class="info">${像素墙C3[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block2 bg-color-dark"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/hs.png"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙C4[0] != null}">
                        			<a href="${像素墙C4[0].link }" class="block block2 bg-color-green"><img src="<zy:fileServerUrl value="${像素墙C4[0].pic }"/>"><div class="info">${像素墙C4[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block2 bg-color-green"><i class="fa fa-2x fa-newspaper-o"></i><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙C5[0] != null}">
                        			<a href="${像素墙C5[0].link }" class="block block2 bg-color-dark"><img src="<zy:fileServerUrl value="${像素墙C5[0].pic }"/>"><div class="info">${像素墙C5[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block2 bg-color-dark"><img src="http://202.85.221.165/static/meilifang/assets/img/banners/b210x100.jpg"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        </div>
                        
                        <div class="metro-block">
                        	<c:choose>
                        		<c:when test="${像素墙D1[0] != null}">
                        			<a href="${像素墙D1[0].link }" class="block block2 bg-color-blue"><img src="<zy:fileServerUrl value="${像素墙D1[0].pic }"/>"><div class="info">${像素墙D1[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block2 bg-color-blue">期待你的加盟！</a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙D2[0] != null}">
                        			<a href="${像素墙D2[0].link }" class="block block4 bg-color-brown"><img src="<zy:fileServerUrl value="${像素墙D2[0].pic }"/>"><div class="info">${像素墙D2[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block4 bg-color-brown"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/prod-newspaper3.jpg"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙D3[0] != null}">
                        			<a href="${像素墙D3[0].link }" class="block block1"><img src="<zy:fileServerUrl value="${像素墙D3[0].pic }"/>"><div class="info">${像素墙D3[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block1"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/lkw.png"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        	<c:choose>
                        		<c:when test="${像素墙D4[0] != null}">
                        			<a href="${像素墙D4[0].link }" class="block block1"><img src="<zy:fileServerUrl value="${像素墙D4[0].pic }"/>"><div class="info">${像素墙D4[0].title }</div></a>
                        		</c:when>
                        		<c:otherwise>
                        			<a href="#" class="block block1"><img src="http://202.85.221.165/static/meilifang/assets/img/portfolio/zly.png"/><div class="info">期待你的加盟</div></a>
                        		</c:otherwise>
                        	</c:choose>
                        </div>
                    </div> 
                    	</c:otherwise>
                    </c:choose>
                </div>
                <a class="left carousel-control rounded-x" href="#prime-carousel" role="button" data-slide="prev">
                    <i class="fa fa-angle-left arrow-prev"></i>
                </a>
                <a class="right carousel-control rounded-x" href="#prime-carousel" role="button" data-slide="next">
                    <i class="fa fa-angle-right arrow-next"></i>
                </a>
            </div>
            <!-- End Magazine Slider -->
				<shiro:authenticated>
					<div class="m-counter after">
						<div class="counter-block">
							<p class="counters">
								<span class="counter txt-md">${mediaCount }</span>
							</p>
							<h4>媒体数</h4>
						</div>
						<div class="counter-block">
							<p class="counters">
								<span class="counter txt-md">${orderCount }</span>
							</p>
							<h4>总成交量</h4>
						</div>
						<div class="counter-block">
							<h2>悬赏金额</h2>
							<p class="counters">
								<span class="counter">${offerSums }</span> 
								<small>元</small>
							</p>
						</div>
					</div>
				</shiro:authenticated>
				<shiro:guest>
            <div class="m-counter">            
                <div class="counter-block">
                    <div class="log">
                    	<h4>采媒在线会员 <small class="pull-right"><a href="${ctx }/register">立即注册</a></small></h4>
                        <form class="login-wrap form-horizontal" id="login-index" action="${ctx}/login?savedURL=${savedURL}" method="post" >                        
                        <div class="form-group form-group-sm has-feedback">
                        	<label class="control-label col-xs-3">手机号：</label>
                            <div class="col-xs-9">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                                    <input type="text" class="form-control"  placeholder="手机号" id="username" name="username" value="${param.username}" maxlength="11">
                                </div>
                            </div>
                        </div>
                        <div class="form-group form-group-sm has-feedback">
                        	<label class="control-label col-xs-3">密码：</label>
                            <div class="col-xs-9">                        
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                                    <input type="password" class="form-control" placeholder="密码" id="password" name="password" value="" maxlength="20">
                                </div>
                            </div>
                        </div>
                        <div class="form-group form-group-sm">
                        	<label class="control-label col-xs-3">验证码：</label>
                            <div class="col-xs-9">  
                                <div class="input-group">
                                    <input type="text" class="form-control" maxlength="5" placeholder="验证码" id="captcha" name="captcha">
                                     <span class="input-group-addon captcha"><img id="img_captcha_index" src="${ctx}/images/kaptcha.jpg" style="cursor: pointer;height: 30px;"></span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group form-group-sm">
                            <div class="col-xs-9 col-xs-offset-3">
                            <button type="submit" class="btn-u btn-u-red" id="btn-login-index">登录</button>
                            </div>
                        </div>
                    </form>
                    </div>
                    <ul class="list-unstyled list-inline mt20">
                        <li>
                            <p class="counters"><span class="counter">${mediaCount }</span></p>
                            <h2>媒体数</h2>
                        </li> 
                        <li>
                            <p class="counters"><span class="counter">${offerSums }</span></p>
                            <h2>悬赏金额</h2>                            
                        </li>
                    </ul>
                </div>
            	<div class="counter-block hide">
                	<h3><b>有态度的媒体私享家</b></h3>                	
                    <div class="row">
                    	<div class="col-xs-12">
                        	<p class="counters"><span class="counter txt-md">${mediaCount }</span></p>
                        	<h4 style="margin-bottom:0;">媒体数</h4>
                        </div>
                    </div>
                    <hr class="hr-xs">
                    <div class="row">
                    	<div class="col-xs-12">
                    		<p class="counters"><span class="counter txt-md">${orderCount }</span></p>
                			<h4 style="margin-bottom:0;">总成交量</h4>
                        </div>
                    </div>
                    <hr class="hr-xs">
                    <div class="row">                
                        <h2>悬赏金额</h2>
                        <p class="counters"><span class="counter">120,000</span></p>
                    </div>
                </div>
            </div>
        	</shiro:guest>
        </div>
    </div>
</div>
 
<!--=== Container ===-->
<div class="container product">
	<div class="row">
		<!-- tablist -->
		<div role="tablist" class="tablist tab-v4 margin-bottom-30">
	    	<ul class="nav nav-tabs">
			 	<li class=""><a aria-expanded="false" href="#a" aria-controls="a" role="tab" data-toggle="tab">优媒推荐</a></li>
			 	<li class=""><a aria-expanded="false" href="#b" aria-controls="b" role="tab" data-toggle="tab">新媒亮相</a></li>
		     	<li class=""><a aria-expanded="false" href="#c" aria-controls="c" role="tab" data-toggle="tab">名企案例</a></li>
		   		<li class=""><a aria-expanded="false" href="#d" aria-controls="d" role="tab" data-toggle="tab">媒体黑马</a></li>
			    <li class=""><a aria-expanded="true" href="#e" aria-controls="e" role="tab" data-toggle="tab">接单冠军</a></li>
		 	</ul>
		    <div class="tab-content">
		 		<div role="tabpanel" class="tab-pane active" id="a">
		     		<!-- 优媒推荐 -->
					<div class="row">
						<c:if test="${weixinRec!=null }">
					 	<c:forEach items="${mediaRec }" var="item" begin="0" end="5">
		           			<div class="col-xs-2">
								<div class="thumbnails thumbnail-style <c:if test="${item.coop }">stick</c:if>">
<%-- 									<a class="stickNew" href="${ctx }/static/assets/img/floatStrick.png" title="战略合作媒体"></a> --%>
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
				<!-- 结束 疯狂抢购 -->               
	  			</div>
				<div role="tabpanel" class="tab-pane" id="b">
		   		<!-- 新媒亮相 -->
					<div class="row">
					</div>
				<!-- 结束 热卖商品 -->
		   		</div>
				<div role="tabpanel" class="tab-pane" id="c">
		   			<!-- 名企客户 -->
					<div class="row">
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
				<!-- 结束 名企客户 -->
		   		</div>
				<div role="tabpanel" class="tab-pane" id="d">
			   		<!-- 新品上架 -->
					<div class="row">
					</div>
				<!-- 结束 新品上架 -->
			  	</div>
				<div role="tabpanel" class="tab-pane" id="e">
				   	<!-- 猜您喜欢 -->
					<div class="row">
					</div>
				<!-- 结束 猜您喜欢 -->
			    </div>
			</div>
		</div><!--/ tablist -->
                       
        	<!-- 微信频道 -->
            <div class="floor floor-main">
                <div class="floor-side">
                    <div class="fs-title">
                        <h2>微信频道</h2>
                        <em class="hide">微信频道</em>
                        <div class="fs-title-link"><a href="${ctx }/weixin">更多 》</a></div>
                    </div>
                    <div class="fs-tabs">
                        <ul class="nav nav-pills nav-stacked"> 
                            <li class="active"><a href="#wechat-recommend" data-toggle="tab">推荐媒体</a></li>
                            <li><a href="#wechat-service" data-toggle="tab">服务号</a></li>
                            <li><a href="#wechat-subscript" data-toggle="tab">订阅号</a></li>
                            <li><a href="#wechat-personal" data-toggle="tab">个人微信号</a></li>                          
                        </ul>    
                    </div>
                    <div class="tags">
                        <a href="${ctx}/weixin?tags=000026">本地粘性强</a>
                        <a href="${ctx}/weixin?tags=000074">草根大号</a>
                        <a href="${ctx}/weixin?tags=000080">段子手</a>
                        <a href="${ctx}/weixin?tags=000022">日活量高</a>
                        <a href="${ctx}/weixin?tags=000025">文案水平高</a>
                        <a href="${ctx}/weixin?tags=000077">意见领袖</a>
                        <a href="${ctx}/weixin?tags=000076">行业名人</a>
                        <a href="${ctx}/weixin?tags=000075">网络红人</a>
                        <a href="${ctx}/weixin?tags=000078">娱乐明星</a>
                    </div>
                </div>
                <!--// floor-side -->
                <div class="floor-content">
                    <div class="tab-content">
                        <div class="tab-pane active" id="wechat-recommend">
                            <!-- start -->
                            <ul class="h-prod-list">
                            	<c:if test="${weixinRec!=null }">
                            	<c:forEach items="${weixinRec }" var="item" begin="0" end="7">
								   	<li>
		                               	<div class="thumbnails thumbnail-style <c:if test="${item.coop }">stick</c:if>">
		                                    <a class="fancybox-button zoomer" data-rel="fancybox-button" title="${item.title }" href="${item.link }" target="_blank">     
		                                   		<img src="<zy:fileServerUrl value="${item.pic }"/>">                                             
		                                    </a>                    
		                                    <div class="caption">
		                                        <h3><a class="hover-effect" href="${item.link }" target="_blank">${item.title }</a></h3>
		                                    </div>
		                                </div>
	                                </li>                     	
                            	</c:forEach>
                            	</c:if>
                            </ul>
                            <!-- end -->                        
                        </div>
                        <div class="tab-pane" id="wechat-service">
                        	<ul class="h-prod-list"></ul>
                        </div>
                        <div class="tab-pane" id="wechat-subscript">
                        	<ul class="h-prod-list"></ul>
                        </div>
                        <div class="tab-pane" id="wechat-personal">
                        	<ul class="h-prod-list"></ul>
                        </div>
                    </div>
                    <!--// tab-content -->
                </div>                
                <!--// floor-content -->
                <div class="floor-sidebar" id="marketingCaseFloor">
                    <div class="panel">
                        <div class="panel-heading">营销经典</div>
                        <div class="panelImgTxt">
                        	<c:if test="${marketingHead!=null }">
                        	<c:forEach items="${marketingHead }" var="item" begin="0" end="1">
                          	<div class="figures">
	                            <a href="${item.link }" target="_blank" title="${item.title }"><img src="<zy:fileServerUrl value="${item.pic }"/>"></a>
	                            <p><a href="${item.link }" target="_blank"  title="${item.title }">${item.title }</a></p>
		                       </a>
                          	</div>
                        	</c:forEach>
                        	</c:if>
                        </div>
                        <div class="panel-body panelBodyText" id="marketingCase">
                            <ul class="list-unstyled" >
                            </ul>
                        </div>
                        <div class="panel-footer">
                            <div>
                                <button type="button" class="btn-link h-prev" disabled><i class="fa fa-angle-left"></i></button>
                                <button type="button" class="btn-link h-next"><i class="fa fa-angle-right"></i></button>  
                            </div>                          
                        </div>
                    </div>
                </div>
                <!--// floor-sidebar -->
                  
            </div><!--// Floor -->
        
        	<!-- 微博频道 -->
            <div class="floor floor-main">
                <div class="floor-side">
                    <div class="fs-title">
                        <h2>微博频道</h2>
                        <div class="fs-title-link"><a href="${ctx }/weibo">更多 》</a></div>
                    </div>
                    <div class="fs-tabs">
                        <ul class="nav nav-pills nav-stacked"> 
                            <li class="active"><a href="#weibo-recommend" data-toggle="tab">推荐媒体</a></li>
                            <li><a href="#weibo-blue" data-toggle="tab">机构认证</a></li>
                            <li><a href="#weibo-yellow" data-toggle="tab">个人认证</a></li>
                            <li><a href="#weibo-null" data-toggle="tab">个人未认证</a></li>                        
                        </ul>    
                    </div>
                    
                    <div class="tags">
                        <a href="${ctx}/weibo?tags=000026">本地粘性强</a>
                        <a href="${ctx}/weibo?tags=000074">草根大号</a>
                        <a href="${ctx}/weibo?tags=000080">段子手</a>
                        <a href="${ctx}/weibo?tags=000022">日活量高</a>
                        <a href="${ctx}/weibo?tags=000025">文案水平高</a>
                        <a href="${ctx}/weibo?tags=000077">意见领袖</a>
                        <a href="${ctx}/weibo?tags=000076">行业名人</a>
                        <a href="${ctx}/weibo?tags=000075">网络红人</a>
                        <a href="${ctx}/weibo?tags=000078">娱乐明星</a>
                    </div>
                </div><!--// floor-side -->
                <div class="floor-content">
                    <div class="tab-content">
                        <div class="tab-pane active" id="weibo-recommend">
	                        <!-- start -->
	                        <ul class="h-prod-list">
	                        	<c:if test="${weiboRec!=null }">
	                        	<c:forEach items="${weiboRec }" var="item" begin="0" end="7">
				   				<li>
	                             	<div class="thumbnails thumbnail-style <c:if test="${item.coop }">stick</c:if>">
	                                  <a class="fancybox-button zoomer" data-rel="fancybox-button" title="${item.title }" href="${item.link }" target="_blank">     
	                                 		<img src="<zy:fileServerUrl value="${item.pic }"/>">                                             
	                                  </a>                    
	                                  <div class="caption">
	                                      <h3><a class="hover-effect" href="${item.link }" target="_blank">${item.title }</a></h3>
	                                  </div>
	                              	</div>
	                             </li>                     	
	                        	</c:forEach>
	                        	</c:if>
	                        </ul>
	                        <!-- end -->                        
	                    </div>
	                    <div class="tab-pane" id="weibo-blue">
	                    	<ul class="h-prod-list"></ul>
	                    </div>
	                    <div class="tab-pane" id="weibo-yellow">
	                    	<ul class="h-prod-list"></ul>
	                    </div>
	                    <div class="tab-pane" id="weibo-null">
	                    	<ul class="h-prod-list"></ul>
	                    </div>
					</div><!--// tab-content -->
				</div><!--// floor-content -->
                <div class="floor-sidebar" id="mediaCaseFloor">
                    <div class="panel">
                        <div class="panel-heading">媒体案例</div>
                        <div class="panel-body" id="mediaCase">
                            <ul class="list-unstyled">
                            </ul>
                        </div>
                        <div class="panel-footer">
                            <div>
                                <button type="button" class="btn-link h-prev" disabled><i class="fa fa-angle-left"></i></button>
                                <button type="button" class="btn-link h-next"><i class="fa fa-angle-right"></i></button>  
                            </div>                          
                        </div>
                    </div>
                </div><!--// floor-sidebar -->              
            </div><!--// Floor -->
			<div class="partner">
                <div><h4><b>合作伙伴</b></h4></div>
                <ul class="list-unstyled">
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
            </div>
        
            <!-- <div class="border border-color-grey padding-10 padding-left-20 padding-right-20 margin-bottom-30">
                <div><h4><b>合作伙伴</b></h4></div>
                <div class="partner">
                    <ul class="list-unstyled">
                        <li><a href="#" target="_blank"><img src="assets/img/partner.jpg"></a></li>
                        <li><a href="#" target="_blank"><img src="assets/img/partner.jpg"></a></li>
                        <li><a href="#" target="_blank"><img src="assets/img/partner.jpg"></a></li>
                        <li><a href="#" target="_blank"><img src="assets/img/partner.jpg"></a></li>
                        <li><a href="#" target="_blank"><img src="assets/img/partner.jpg"></a></li>
                        <li><a href="#" target="_blank"><img src="assets/img/partner.jpg"></a></li>
                        <li><a href="#" target="_blank"><img src="assets/img/partner.jpg"></a></li>
                        <li><a href="#" target="_blank"><img src="assets/img/partner.jpg"></a></li>
                    </ul>
                </div>
                <hr class="hr-md hide" style="border-style:dashed; border-color:#ccc;">
                <div class="hide"><h4><b>友情链接</b></h4></div>
                <div class="friendlylink hide">
                    <a href="#" target="_blank">39体检</a>
                    <a href="#" target="_blank">免费范文网</a>
                    <a href="#" target="_blank">淄博分类信息</a>
                    <a href="#" target="_blank">万国表官网</a>
                    <a href="#" target="_blank">北京设计公司</a>
                    <a href="#" target="_blank">刷百度下拉框</a>
                    <a href="#" target="_blank">万国表官网</a>
                    <a href="#" target="_blank">北京设计公司</a>
                    <a href="#" target="_blank">刷百度下拉框</a>                
                </div>
            </div> --><!--//合作伙伴 -->            
    </div>    
</div>
<!--=== End Container ===-->

<script type="text/template" id="media-temp1">
	<li>
		<div class="thumbnails thumbnail-style ">
	       <a class="fancybox-button zoomer {stick}" data-rel="fancybox-button" title="{name}" href="${ctx}/{type}/detail/{id}" target="_blank">     
	      		<img src="{showPic}" alt="{name}" >                                              
	       </a>                    
	       <div class="caption">
	           <h3><a class="hover-effect" href="${ctx}/{type}/detail/{id}" target="_blank">{name}</a></h3>
	           
	       </div>
	   	</div>
	</li> 
</script>

<script type="text/template" id="media-temp2">
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

<script type="text/template" id="case-temp1">
	 <li><i></i><a  href="${ctx}/{mediaType}/detail/{mediaId}#f" target="_blank" title="{titleAll}">{title}</a></li>
</script>
<script type="text/template" id="marketing-temp">
	 <li><i></i><a  href="{url}" target="_blank" title="{titleAll}">{title}</a></li>
</script>

<!-- JS Implementing Plugins -->
<!-- 计数插件 -->
<script type="text/javascript" src="${ctx}/static/assets/plugins/counter/waypoints.min.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/plugins/counter/jquery.counterup.min.js"></script>


<script type="text/javascript">

	$('#prime-carousel').carousel({interval: 5000});
$(function() {
	var defad = parseInt(Math.random()*5); 
	$('.nav-tabs').children().each(function(index, element){
		if (index == defad){
			$(this).children("a").mouseover();
		}
	});

	$('#img_captcha_index').click(function() {
		$(this).attr({src:"${ctx}/images/kaptcha.jpg?t=" + Math.random()});
	});
	
	
	OwlCarousel.initOwlCarousel();  
	App.initCounter(); 	//计数  
	//首页取消“全部分类”的下拉菜单
	setTimeout(function() {	
		$('.m-category').off();
	}, 200);
	
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
	
	function loadCase(params, selector, $template) {
		$.get('${ctx}/loadCase?ajax', params, function(rsp) {
			if( rsp.result ) {
				var html = [];
				$.each(rsp.page.data, function(i, o) {
					html.push($template.format(o));
				});
				$(selector).html(html.join(''));
				rolling('mediaCaseFloor');
			}
		});		
	}
	function loadMarketing(selector, $template) {
		$.get('${ctx}/loadMarketing?ajax', function(rsp) {
			if( rsp.result ) {
				var html = [];
				$.each(rsp.data, function(i, o) {
					html.push($template.format(o));
				});
				$(selector).html(html.join(''));
				rolling('marketingCaseFloor');
			}
		});		
	}
	
	function rolling(mediaTypeFloor){
		$('#'+mediaTypeFloor).each(function(index, element) {        
			var caseBoxHeight=$(this).children('.panel').children('.panel-body').height(),
			caseHeight=$(this).children('.panel').children('.panel-body').children('ul').height(),
			panelFooter=$(this).children('.panel').children('.panel-footer'),
			btnBar=panelFooter.children('div');
			
		if(caseHeight>caseBoxHeight) {
			panelFooter.show();					
					btnBar.children('.h-next').click(function(){
												
						var caseBox  = $(this).parents('.panel-footer').siblings('.panel-body'),
							caseCont = $(this).parents('.panel-footer').siblings('.panel-body').children('ul'),
							scrollBottom =$(caseBox).height() + $(caseBox).scrollTop();
							caseBox.animate({
								'scrollTop' : "+=351px"
							}, 1000);
							if(scrollBottom >= 300){//caseBox.offset().top > 0
								$(this).siblings('.h-prev').removeAttr('disabled');
							}
							if(caseCont.height()-scrollBottom <=351+17){
								$(this).prop('disabled',true);
							}
					});
					
					btnBar.children(".h-prev").click(function(){
							
						var caseBox  = $(this).parents('.panel-footer').siblings('.panel-body'),
							caseCont = $(this).parents('.panel-footer').siblings('.panel-body').children('ul'),
							scrollBottom =$(caseBox).height() + $(caseBox).scrollTop();
								
							caseBox.animate({
								'scrollTop' : "-=351px"
							}, 1000);
							if(scrollBottom-caseCont.height() < caseBox.height()){
								$(this).siblings('.h-next').prop('disabled',false);
							}
							if(caseBox.scrollTop() <= 351){
								$(this).prop('disabled',true);
							}									
					});					
				}
				else{
					caseFooter.hide();
				}
			});
	}
	
	var $mediaTemp1 = $('#media-temp1');
	var $mediaTemp2 = $('#media-temp2');
	var $caseTemp1 = $('#case-temp1');
	
	//加载 微信-案例
	loadMarketing('#marketingCase > .list-unstyled', $('#marketing-temp'));
	
	//加载 微信-案例
	loadCase({}, '#mediaCase > .list-unstyled', $caseTemp1);
	
	//加载 微信-推荐媒体
// 	loadAd({adType: "a"}, '#a > .row', $mediaTemp2);
	
	//加载 微信-推荐媒体
	loadAd({adType: "d"}, '#d > .row', $mediaTemp2);
	
	//加载 微信-推荐媒体
	loadAd({adType: "e"}, '#e > .row', $mediaTemp2);
	
	//加载 微信-推荐媒体
	load({pageSize: 6, sort:'createTime desc'}, '#b > .row', $mediaTemp2);
	
	//加载 微信-推荐媒体
// 	load({mediaType: 'MEDIA_T_WEIXIN', pageSize: 8, sort:'modifyTime desc'}, '#wechat-recommend > .h-prod-list', $mediaTemp1);

	//加载 微信-服务号
	load({mediaType: 'MEDIA_T_WEIXIN', category:'WEIXIN_C_SERVICE', pageSize: 8, sort:'modifyTime desc'}, '#wechat-service > .h-prod-list', $mediaTemp1);

	//加载 微信-订阅号
	load({mediaType: 'MEDIA_T_WEIXIN', category:'WEIXIN_C_SUBSCRIBE', pageSize: 8, sort:'modifyTime desc'}, '#wechat-subscript > .h-prod-list', $mediaTemp1);

	//加载 微信-个人微信号
	load({mediaType: 'MEDIA_T_WEIXIN', category:'WEIXIN_C_PERSONAL', pageSize: 8, sort:'modifyTime desc'}, '#wechat-personal > .h-prod-list', $mediaTemp1);
	
	//加载 微博-推荐媒体
// 	load({mediaType: 'MEDIA_T_WEIBO', pageSize: 8, sort:'modifyTime desc'}, '#weibo-recommend > .h-prod-list', $mediaTemp1);
	
	//加载 微博-机构认证
	load({mediaType: 'MEDIA_T_WEIBO', category:'WEIBO_C_BLUE', pageSize: 8, sort:'modifyTime desc'}, '#weibo-blue > .h-prod-list', $mediaTemp1);

	//加载 微博-个人认证
	load({mediaType: 'MEDIA_T_WEIBO', category:'WEIBO_C_YELLOW', pageSize: 8, sort:'modifyTime desc'}, '#weibo-yellow > .h-prod-list', $mediaTemp1);

	//加载 微博-个人未认证
	load({mediaType: 'MEDIA_T_WEIBO', category:'WEIBO_C_NULL', pageSize: 8, sort:'modifyTime desc'}, '#weibo-null > .h-prod-list', $mediaTemp1);

	
	
	
	
	$('#marketingCase').delegate('a', 'click', function() {
		$(this).attr('style','color:gainsboro');
	});
	
	$('#mediaCase').delegate('a', 'click', function() {
		$(this).attr('style','color:gainsboro');
	});
});
 
$(document).ready(function() { 
	//控制首页成功案例高度
	var floorMain_h=$('.floor').height();
	$('.floor-sidebar .panel').outerHeight(floorMain_h);
});
//为表单注册validate函数
$("#login-index").validate({
	rules : {
		username: {
			required:true,
		},
		password: {
			required:true,
			rangelength : [6,20]
		},
		captcha:  {
			required:true
		}
	},
	messages: {
		username: {
		  required: "请输入手机号",
		},
		password: {
		 required: "请输入密码",
		 rangelength : "密码应为6~20位字符"
		},
		captcha: {
		 required: "请输入验证码"
		}
	}
});


var message = '${message}';
if(message != ''&&(typeof message) != 'undefined' ){
	common.showMessage("投诉建议成功！", 1000);
}


</script>

    
</body>
</html>

