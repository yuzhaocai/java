<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
        <!-- Sidebar -->
    	<div class="sidebar-v3">
        	<!-- Sidebar Menu -->
            <a href="${ctx}/helpCenter/about" class="sidebar-menu-title" ><i class="fa fa-user"></i><strong>关于我们</strong></a> 
        	<div class="list-group">
                <a href="${ctx}/helpCenter/about" class="list-group-item" id="about"><i class="fa fa-caret-right"></i> 关于采媒在线</a> 
                <a href="${ctx}/helpCenter/contact" class="list-group-item" id="contact"><i class="fa fa-caret-right"></i> 联系我们</a> 
                <a href="${ctx}/helpCenter/news" class="list-group-item" id="news"><i class="fa fa-caret-right"></i> 媒体报道</a>
                <a href="${ctx}/helpCenter/joinUs" class="list-group-item" id="joinUs"><i class="fa fa-caret-right"></i> 加入我们</a>
            </div>
            <a href="${ctx}/helpCenter/meidiaJoin" class="sidebar-menu-title"><i class="fa fa-users"></i><strong>服务合作</strong></a>
            <div class="list-group">
                <a href="${ctx}/helpCenter/meidiaJoin" class="list-group-item" id="meidiaJoin"><i class="fa fa-caret-right"></i> 媒体入驻</a> 
                <a href="${ctx}/helpCenter/enterprisesJoin" class="list-group-item" id="enterprisesJoin"><i class="fa fa-caret-right"></i> 企业入驻</a> 
                <a href="${ctx}/helpCenter/serviceTerms" class="list-group-item" id="serviceTerms"><i class="fa fa-caret-right"></i> 服务条款</a>
                <a href="${ctx}/helpCenter/businessCooperation" class="list-group-item" id="businessCooperation"><i class="fa fa-caret-right"></i> 私人定制</a>
            </div> 
            <a href="${ctx}/helpCenter/adsProcess" class="sidebar-menu-title"><i class="fa fa-compass"></i><strong>帮助中心</strong></a>
            <div class="list-group">
                <a href="${ctx}/helpCenter/adsProcess" class="list-group-item" id="adsProcess"><i class="fa fa-caret-right"></i> 广告需求投放流程</a> 
                <a href="${ctx}/helpCenter/memberRegister" class="list-group-item" id="memberRegister"><i class="fa fa-caret-right"></i> 会员注册</a> 
                <a href="${ctx}/helpCenter/memberLevel" class="list-group-item" id="memberLevel"><i class="fa fa-caret-right"></i> 会员等级</a>
                <a href="${ctx}/helpCenter/question" class="list-group-item" id="question"><i class="fa fa-caret-right"></i> 常见问题</a>
                <a target="_blank" href="${ctx }/updateLog" class="list-group-item"><i class="fa fa-caret-right"></i> 更新日志</a>
                <a target="_blank" href="${ctx }/guidepage" class="list-group-item"><i class="fa fa-caret-right"></i> 新手教程</a>
            </div> 
        </div>
        <!-- End Sidebar -->
