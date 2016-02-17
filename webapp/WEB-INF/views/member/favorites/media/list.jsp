<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>我的订单</title>
</head>

<body>
        
  <!-- Main -->
<div class="main-panel" style="height:auto; min-height:400px;">
	<div class="headline"><h5>我的收藏</h5></div>
    <!-- pad -->                
    <div class="pad mtab-v2">
	    <ul class="nav nav-tabs">
	        <li class="active"><a>微信</a></li>
	        <li class=""><a href="${ctx}/member/favoritesMedia/weibo" >微博</a></li>
	        <li class=""><a href="${ctx}/member/favoritesMedia/other" >其他媒体</a></li>
	    </ul>                
		<div class="tab-content">
			<div class="tab-pane active" id="transactions">
			<div class="topwrap">
              	<form class="form-inline">
                  <div class="form-group form-group-sm">
                  		<label>名称</label>
                  		<input name="search_LIKE_media.name" value="${param.search_LIKE_media.name }" type="text" class="form-control"> 
                  </div>
                  <button class="btn-u btn-u-sm btn-u-dark"><i class="fa fa-search"></i> 查询</button>
              	</form>
            </div>
			<ul id="medias" class="prod-list clearfix thumbnails " >
				 <c:forEach items="${data.content }" var="item" varStatus="stat">
				 	<li>
					    <div class="thumbnail thumbnail-display ">
							<div class="photo-wrap" style="align:center;">
						        <a href="${ctx}/weixin/detail/${item.media.id}">
						            <img src="${item.media.showPic}" class="photo" alt="${item.media.name}">
						            <span class="photo-mask"></span>
						        </a>
								<button type="button" class="remove-favorites" data-id="${item.media.id}" >取消收藏</button>
							</div>
						    <div class="thumbnail-block caption">
						    	<h1 class="thumbnail-title"><a href="${ctx}/weixin/detail/${item.media.id}">${item.media.name}</a></h1> 
					            <ul class="list-unstyled thumbnail-text">                                   
					                <li>类别：<span><zy:dic value="${item.media.category}" /></span></li>
					                <li>粉丝：<span>${item.media.fans}</span></li>
					                <li>行业：<p class="trades">
										<span><zy:dic value="${item.media.industryType}" /></span>
									</p>
									</li>
					            </ul>
						    </div>
					    </div> 
					</li>
				 </c:forEach>
	    	</ul>
       		</div>
     	</div>
	</div>
    <!-- End Tab v2 --> 
    <tags:pagination page="${data }" />
</div>

<!-- End Main -->
<script type="text/javascript">
menu.active('#favorites-media');

$('#medias').delegate(".remove-favorites", "click", function(){
	var fid = $(this).data("id");
	var type = 'FAVORITES_WEIXIN';
	bootbox.confirm("您确定要取消收藏？",function(result){
		if(result){
			$.post("${ctx}/member/favoritesMedia/delete", {mediaId:fid,type:type}, function(rsp) {
				rsp = $.parseJSON(rsp);
				if (rsp.result){
					window.location.reload();
				} else {
					
				}
			});
		}
	});
});
</script>

</body>
</html>
