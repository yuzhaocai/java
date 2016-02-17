<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<div class="panel panel-default">

<div class="panel-heading"><a href="#"><span class="glyphicon glyphicon-th-large"></span> <strong>功能菜单</strong></a></div>

<div class="panel-body leftmenu"><!-- 左侧菜单 --> 
  <ul class="list-group leftmenu-list">
    <shiro:hasPermission name="admin:audit">
    <li>
      <a id=""><span></span> 审核工作台</a>
      <ul class="list-group">
      <shiro:hasPermission name="audit:req">
        <li><a href="${ctx}/admin/audit/req" class="list-group-item" id="audit-req">需求审核</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="audit:media">
        <li><a href="${ctx}/admin/audit/media" class="list-group-item" id="audit-media">媒体审核</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="audit:write">
        <li><a href="${ctx}/admin/audit/write" class="list-group-item" id="audit-write">撰稿需求</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="audit:refuse">
        <li><a href="${ctx}/admin/audit/refuse" class="list-group-item" id="audit-refuse">拒付审核</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="audit:complaint">
        <li><a href="${ctx}/admin/audit/complaint" class="list-group-item" id="audit-complaint">举报管理</a></li>
      </shiro:hasPermission>
      
      </ul>
    </li>
    </shiro:hasPermission>
    
    <shiro:hasPermission name="adminmember">
    <li>
      <a id=""><span></span> 会员管理</a>
      <ul class="list-group">
        <li><a href="${ctx}/admin/member" class="list-group-item" id="member-register">注册会员查询</a></li>
      
        <li><a href="${ctx}/admin/member/cert" class="list-group-item" id="member-cert">实名认证审核</a></li>
      
      </ul>
    </li>
    </shiro:hasPermission>
    <shiro:hasPermission name="admin:media">
    <li>
      <a id=""><span></span> 媒体管理</a>
      <ul class="list-group">
      <shiro:hasPermission name="media:list">
        <li><a href="${ctx}/admin/media/media" class="list-group-item" id="media-media">媒体查询</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="media:input">
        <li><a href="${ctx}/admin/mediaInput" class="list-group-item" id="media-input">媒体录入</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="media:input">
        <li><a href="${ctx}/admin/mediaImport" class="list-group-item" id="media-import">媒体导入</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="media:tags">
        <li><a href="${ctx}/admin/media/mediaTag" class="list-group-item" id="media-mediaTag">媒体标签管理</a></li>
      </shiro:hasPermission>
      <shiro:hasPermission name="media:input">
        <li><a href="${ctx}/admin/media/mediaTagImport" class="list-group-item" id="media-mediaTagImport">媒体标签导入</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="media:organizations">
        <li><a href="${ctx}/admin/media/organization" class="list-group-item" id="media-organization">机构管理</a></li>
      </shiro:hasPermission>
      </ul>
    </li>
    </shiro:hasPermission>

    <shiro:hasPermission name="admin:othermedia">
    <li>
      <a id=""><span></span> 更多媒体管理</a>
      <ul class="list-group">
      <shiro:hasPermission name="othermedia:index">
        <li><a href="${ctx}/admin/otherMedia/" class="list-group-item" id="other-media">媒体管理</a></li>
      </shiro:hasPermission>
      <shiro:hasPermission name="othermedia:create">
        <li><a href="${ctx}/admin/otherMedia/create" class="list-group-item" id="other-media-create">发布媒体</a></li>
      </shiro:hasPermission>
      <shiro:hasPermission name="othermedia:order">
 		<li><a href="${ctx}/admin/intention/" class="list-group-item" id="othermedia-intention">订单管理</a></li>
      </shiro:hasPermission>
      </ul>
    </li>
    </shiro:hasPermission>
    <shiro:hasPermission name="admin:order">
    <li>
      <a id=""><span></span> 订单管理</a>
      <ul class="list-group">
      <shiro:hasPermission name="adminorder:req">
        <li><a href="${ctx}/admin/order/req" class="list-group-item" id="order-req">需求查询</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="adminorder:order">
        <li><a href="${ctx}/admin/order/order" class="list-group-item" id="order-order">订单查询</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="adminorder:invoice">
        <li><a href="${ctx}/admin/order/invoice" class="list-group-item" id="order-invoice">发票管理</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="adminorder:message">
        <li><a href="${ctx}/admin/order/orderMessage" class="list-group-item" id="order-orderMessage">留言管理</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="adminloan:audit">
        <li><a href="${ctx}/admin/order/loan" class="list-group-item" id="order-loan">垫资审核</a></li>
      </shiro:hasPermission>
      </ul>
    </li>
    </shiro:hasPermission>
	
    <shiro:hasPermission name="admin:user">
    <li>
      <a id=""><span></span> 后台用户管理</a>
      <ul class="list-group">
      <shiro:hasPermission name="user:list">
        <li><a href="${ctx}/admin/user" class="list-group-item" id="user-man">用户管理</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="role:list">
        <li><a href="${ctx}/admin/role" class="list-group-item" id="role-man">角色管理</a></li>
      </shiro:hasPermission>
      </ul>
    </li>
    </shiro:hasPermission>
    
    <shiro:hasPermission name="adminfinancial:manage">
    <li>
      <a id=""><span></span> 资金管理</a>
      <ul class="list-group">
      <shiro:hasPermission name="adminfinancial:withdraw">
          <li><a href="${ctx}/admin/withdraw" class="list-group-item" id="withdraw-man"> 提现申请</a></li>
      </shiro:hasPermission>
      <shiro:hasPermission name="adminfinancial:chargeLog">
          <li><a href="${ctx}/admin/chargeLog" class="list-group-item" id="charge-log-man"> 充值记录</a></li>
      </shiro:hasPermission>
      <shiro:hasPermission name="adminfinancial:transaction">
          <li><a href="${ctx}/admin/transaction" class="list-group-item" id="transaction-man"> 交易流水</a></li>
      </shiro:hasPermission>
      <shiro:hasPermission name="adminfinancial:offerline">
          <li><a href="${ctx}/admin/offerline" class="list-group-item" id="offerline-man"> 线下服务记录</a></li>
	  </shiro:hasPermission>      
      </ul>
    </li> 
    </shiro:hasPermission>
    
    <shiro:hasPermission name="adminMediaQuote:manage">
    <li>
      <a id=""><span></span> 报价管理</a>
      <ul class="list-group">
        <shiro:hasPermission name="adminMediaQuote:update">
        <li><a href="${ctx}/admin/mediaQuote" class="list-group-item" id="media-quote-list">报价修改</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="adminMediaQuote:audit">
        <li><a href="${ctx}/admin/mediaQuote/audit" class="list-group-item" id="media-quote-audit">报价审核</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="adminMediaStar:manage">
        <li><a href="${ctx}/admin/mediaStar" class="list-group-item" id="media-star">报价属性</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="adminActivity:manage">
        <li><a href="${ctx}/admin/activity" class="list-group-item" id="audit-activity">活动折扣</a></li>
        </shiro:hasPermission>
      </ul>
    </li>
    </shiro:hasPermission>    
    
    
    <shiro:hasPermission name="advSetting:manage">
    <li>
      <a id=""><span></span> 广告管理</a>
      <ul class="list-group">
      <shiro:hasPermission name="advSetting:add">
        <li><a href="${ctx}/admin/advSetting/addAdv" class="list-group-item" id="add-adv">添加广告</a></li>
      </shiro:hasPermission>
      <shiro:hasPermission name="advSetting:delete">
        <li><a href="${ctx}/admin/advSetting" class="list-group-item" id="del-adv">删除广告</a></li>
      </shiro:hasPermission>
      </ul>
    </li>
	</shiro:hasPermission>
    
    <shiro:hasPermission name="opinion:manage">
    <li>
      <a id=""><span></span> 意见管理</a>
      <ul class="list-group">
      <shiro:hasPermission name="opinion:business">
        <li><a href="${ctx}/admin/opinion/list/OPINION_BUSINESS" class="list-group-item" id="OPINION_BUSINESS">业务咨询</a></li>
      </shiro:hasPermission>
      <shiro:hasPermission name="opinion:web">
        <li><a href="${ctx}/admin/opinion/list/OPINION_WEB" class="list-group-item" id="OPINION_WEB">网站吐槽</a></li>
      </shiro:hasPermission>
		<shiro:hasPermission name="opinion:technology">
		<li><a href="${ctx}/admin/opinion/list/OPINION_TECHNOLOGY" class="list-group-item" id="OPINION_TECHNOLOGY">技术问题</a></li>
		</shiro:hasPermission>
      </ul>
    </li>
	</shiro:hasPermission>
    
    <shiro:hasPermission name="sysadmin">
    <li>
      <a id=""><span></span> 系统管理</a>
      <ul class="list-group">
      <shiro:hasPermission name="sysadmin:func">
        <li><a href="${ctx}/admin/func" class="list-group-item" id="func-man">功能管理</a></li>
      </shiro:hasPermission>
      
      <shiro:hasPermission name="sysadmin:dic">
        <li><a href="${ctx}/admin/dic" class="list-group-item" id="dic-man">字典管理</a></li>
      </shiro:hasPermission>
      <%-- 
	      <li><a href="${ctx}/admin/solr" class="list-group-item" id="admin-solr"> 重建索引</a></li>
	      <li><a href="${ctx}/admin/blacklist" class="list-group-item" id="admin-blacklist"> 黑名单管理</a></li>
      --%>
	    <li><a href="${ctx}/admin/research" class="list-group-item" id="admin-research"> 竞品分析</a></li>
      </ul>
    </li>
    </shiro:hasPermission>
	<%-- 
    <shiro:hasPermission name="admindebug">
    <li>
      <a id=""><span></span> 系统调试</a>
      <ul class="list-group">
	      <li><a href="${ctx}/admin/debug/env" class="list-group-item" id="debug-env"> 系统环境</a></li>
      </ul>
    </li>
    </shiro:hasPermission>
	--%>

  </ul>
</div><!-- / 左侧菜单 -->

</div>

<script type="text/javascript">
$(function() {
	setTimeout(function() {
		if( $('.leftmenu > ul.leftmenu-list > li > ul > li> a.active').length == 0 ) {
			$('.leftmenu > ul.leftmenu-list > li:first a').click();
		}
	}, 500);
})
</script>