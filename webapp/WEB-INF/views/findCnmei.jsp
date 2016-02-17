<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>忘记密码-采媒在线</title>
    <link rel="stylesheet" href="${ctx}/static/assets/css/find.css">
</head>

<body>
	<div class="banner">
		<div class="container bannerSub">
			<img alt="发现采媒" src="${ctx }/static/assets/img/findcaimei/bannerImg.jpg">
			<div class="category show findCategory"></div>
		</div>
	</div>
	<!--=== Container ===-->
	<div class="findMain">
	    <div class="container product">
		    <div class="row findImgtxt">
	            <div><h4><b>小编推荐</b></h4></div>
	            <div class="clearfix">
            		<c:if test="${editorRecommend!=null }">
	                	<c:forEach items="${editorRecommend }" var="item" begin="0" end="4">
	                		<div class="findImgtxtSub tooltipS">
			                    <a href="${item.link }" target="_blank"><img src="<zy:fileServerUrl value="${item.pic }"/>" alt="${item.title }"></a>
			                    <h3><a href="${item.link }" target="_blank" title="${item.title }">${item.title }</a></h3>
			                    <div class="hide">
			                    	${item.outLine }
			                    </div>
			                  	<p><zy:out value="${item.outLine }" len="30"/></p>
			                </div>
	                	</c:forEach>
                	</c:if>
	          </div>
	        </div>
	        <div class="row"><a href="http://www.cnmei.com" target="_blank"><img src="${ctx }/static/assets/img/findcaimei/bannerImg0.jpg" alt="发现采媒在线"></a></div>
	        <div class="row findImgtxt findImgtxtMore">
	            <div><h4><b>不能不看</b></h4></div>
	            <div class="clearfix" id="cnmei-list">
	          	</div>
	          	<div class="btn-more" id="load-more"><a href="javascript:void(0);" id="btn-more"></a></div>
	        </div>
<!-- 	        <div class="row findParLinks"> -->
<!-- 	            <div class="partner"> -->
<!-- 	                <div><h4><b>合作伙伴</b></h4></div> -->
<!-- 	                <ul class="list-unstyled clearfix"> -->
<%-- 	                    <c:if test="${partnerMedia!=null }"> --%>
<%-- 		                	<c:forEach items="${partnerMedia }" var="item" begin="0" end="9"> --%>
<%-- 		                		<li><a target="_blank" href="${item.link }"><img src="<zy:fileServerUrl value="${item.pic }"/>"></a></li> --%>
<%-- 		                	</c:forEach> --%>
<%-- 	                	</c:if> --%>
<!-- 	                </ul> -->
<!-- 	                <hr class="hr-md" style="border-style:dashed; border-color:#ccc;"> -->
<!-- 	                <h4><b>友情链接</b></h4> -->
<!-- 	                <div class="friendlylink"> -->
<%-- 	                    <c:if test="${friendlyLink!=null }"> --%>
<%-- 		                	<c:forEach items="${friendlyLink }" var="item" begin="0" end="17"> --%>
<%-- 	                    		<a href="${item.link }" target="_blank">${item.title }</a> --%>
<%-- 		                	</c:forEach> --%>
<%-- 	                	</c:if>                 --%>
<!-- 	                </div> -->
<!-- 	            </div>//合作伙伴             -->
<!-- 	        </div>     -->
	    </div>
	</div>
	<!--=== End Container ===-->
	<script type="text/template" id="cnmeiTemp">
		<div class="findImgtxtSub">
		    <a href="{url}" target="_blank"><img src="{img}" alt="{title}"></a>
		    <h3><a href="{url}" target="_blank" title="{title}">{title}</a></h3>
		  <p>{outLine}</p>
		</div>
	</script>
	<script type="text/template" id="cateTemp">
        <ul class="cate-list">
        	<li>
            	<div class="cate-item">
                    <h2><a href="#">新手引导</a></h2>
                    <div class="links">
                        <a href="http://www.cnmei.com/cms/media_entering" target="_blank">媒体入驻</a>
                        <a href="http://www.cnmei.com/cms/enterprise_entering" target="_blank">广告主入驻</a>
                        <a href="http://www.cnmei.com/cms/adv_processing" target="_blank">广告投放</a>  
						<a href="http://www.cnmei.com/guidepage" target="_blank">操作指南</a>                            
                    </div>
                </div>
            </li>
        	<li>
            	<div class="cate-item">
                    <h2><a href="#">采媒课堂</a></h2>
                    <div class="links">
                        <a href="http://www.cnmei.com/cms/category/cehuazhuanchang" target="_blank">策划专场</a>
                        <a href="http://www.cnmei.com/cms/category/wenanfenxi" target="_blank">文案分析</a> 
                        <a href="http://www.cnmei.com/cms/category/guanggaocelue" target="_blank">广告策略</a>                           
                    </div>
                </div>
            </li>
        	<li>
            	<div class="cate-item">
                    <h2><a href="#">传媒精粹</a></h2>
                    <div class="links">
                        <a href="http://www.cnmei.com/cms/category/fangjianfengchuan" target="_blank">坊间疯传</a> 
                        <a href="http://www.cnmei.com/cms/category/yingxiao" target="_blank">营销经典</a>  
                        <a href="http://www.cnmei.com/cms/category/guanggaochaoliu" target="_blank">广告潮流</a>                         
                    </div>
                </div>
            </li>                    
        </ul>
 	</script>
	<script type="text/javascript">
	//每次加载媒体数量
	var pageSize = 10;
	//判断是否为首次加载
	var pageFlag;
	
	/* 小编推荐文字描述 */
	$(function(){
		//替换分类
		replaceCategory();
		//显示分类
		$('.m-category .category').show();
		//移除分类悬浮事件
		$('.m-category').unbind("mouseenter mouseleave");
		//概要悬浮事件
// 		editorHover();
		//查询cnmei数据
		queryCnmei(1,20);
		
		//加载更多
// 		$('#btn-more').click(function() {
// 			var data = $(this).data();
// 			var pageNum   = data.pageNum + 1;
// 			if( pageNum <= data.pageTotal ) {
// 				queryCnmei(pageNum,pageSize);
// 			}
// 		});
		
		$(window).scroll(scrollLoad); 
	   
	});
	//滚动加载
	function scrollLoad(){
		//页面文档高度
		var totalHeight=parseFloat($(document).height());
		//浏览器的高度+滚动条距离到顶部高度
		var scrollHeight=parseFloat($(window).height()) + parseFloat($(window).scrollTop());
		
	    if(totalHeight-scrollHeight==0){
	    	$(window).unbind('scroll');
			var data = $('#btn-more').data();
			var pageNum   = data.pageNum + 1;
			if( pageNum <= data.pageTotal ) {
				queryCnmei(pageNum,pageSize);
			}
	    }
	}
	
	/* 小编推荐文字描述 */
	function editorHover(){
		var $tooltipSP=$('.tooltipS p');
		   $($tooltipSP).each(function(index,element) {
				var element = $(this);
				element.attr('style','cursor:pointer');
				var id = element.attr('id');
				var txt = element.html();			
				var direction;
				if((index + 1) % 5 == 0){//被4整除等于0
					direction = 'left';
				} else {
					direction = 'right';
				}
				element.popover({
				  animation:'false',
				  trigger: 'manual',
				  //placement: 'right', //top, bottom, left or right
				  placement: direction, //top, bottom, left or right
				  title: '',
				  html: 'true',
				  content: $(this).prev().text()
				}).on("mouseenter", function () {
				  var _this = this;
				  $(this).popover("show");
				  $(this).siblings(".popover").on("mouseleave", function () {
					$(_this).popover('hide');
				  });
				  			 
				}).on("mouseleave", function () {
				  var _this = this;
				  setTimeout(function () {
					if (!$(".popover:hover").length) {
					  $(_this).popover("hide")
					}
				  });
				});
				
			});
	}
	
	function loadMore(page){
		var data = $('#btn-more').data();
		//首次加载20条数据 之后每次加载10条
		if(!pageFlag){
			data.pageNum = page.pageNum+1;
			pageFlag = true;
		}else{
			data.pageNum = page.pageNum;
		}
		data.pageTotal = page.totalPage;
		
		if( page.pageNum < page.totalPage ) {
			$('#load-more').show();
		} else {
			$('#load-more').hide();
		}
	}
	/* 查询媒体 */
	function queryCnmei(pageNum,pageSize) {
		var $loading = $('<span class="loading">正在加载更多内容</span>');
		$('#load-more').append($loading);
		var data;
		data += '&pageNum=' + pageNum;
		data += '&pageSize=' + pageSize;
		data += '&sort=createTime desc';
		$.post('${ctx}/findCnmei/queryCnmei', data, function(data) {
			var page = $.parseJSON(data);
			if( page.result ) {
				var $temp = $('#cnmeiTemp');
				var html = [];
				$.each(page.data, function(i, o) {
					html.push($temp.format(o));
				});
				
				$('#cnmei-list').append(html.join(''));
				
				$loading.remove();
				loadMore(page);
				$(window).bind('scroll',scrollLoad);
			}
		});
		
	};
	/*替换分类*/
	function replaceCategory(){
		//更换分类名称
		$('.cate-title a:eq(0)').text('发现采媒');
		//添加分类样式
		$('.cate-wrap').parent().addClass('findCategory');
		//替换分类内容
		var $temp = $('#cateTemp');
		$('.cate-wrap').html($temp.format());
	}
	
	</script>
</body>
</html>