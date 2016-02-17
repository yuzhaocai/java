<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>


   	<!-- Sidebar Menu -->
   	<div class="sidebar sidebar-menu">
		<div class="sidebar-menu-title"><i class="fa fa-bars"></i><strong>会员中心</strong></div> 
		<div id="leftBar" class="list-group">
		<shiro:hasRole name="advertiser">
			<a href="${ctx }/member/req/advertiser"    id="my-req" class="list-group-item"> 我的需求</a> 
			<a href="${ctx }/member/order/advertiser" id="advertiser-order" class="list-group-item"> 我的订单</a>
			<a href="${ctx }/member/refund/list" id="my-refund"  class="list-group-item"> 我的退款</a>
			<%-- 
			<a href="${ctx }/member/article/list" id="my-article"  class="list-group-item"> 广告素材管理</a> 
			--%>
			<a href="${ctx }/member/favoritesMedia/list" id="favorites-media"   class="list-group-item"> 我的收藏</a> 
		</shiro:hasRole>
		<shiro:hasRole name="provider">
			<a href="${ctx }/member/media/list" id="my-media" class="list-group-item"> 我的媒体</a>  
			<a href="${ctx }/member/req/deal" id="deal-req"   class="list-group-item"> 待处理需求</a> 
			<a href="${ctx }/member/order/provider" id="provider-order"   class="list-group-item"> 我的订单</a> 
			<a href="${ctx }/member/favoritesReq/list" id="favorites-req"   class="list-group-item"> 我的收藏</a> 
		</shiro:hasRole>
			<a href="${ctx }/member/account" id="my-account" class="list-group-item"> 我的资金</a>
			<a href="${ctx }/member/userInfo" id="user-info"  class="list-group-item"> 我的信息</a>
			<a href="${ctx }/member/message" id="message-center"  class="list-group-item"> <span class="badge rounded badge-blue msg-badge"></span>消息</a>
		</div>
	</div>

    <!-- End Sidebar Menu -->
    
<script type="text/javascript">
$(function() {
	app.loadSiteMessageBadge();
});
</script>
