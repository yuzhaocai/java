<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!doctype html>
<html>
<head>
    <title>会员注册</title>
</head>

<body>
 
<!--=== Container ===-->
        <!-- Main -->
        <div class="panel panel-default">
        	<div class="panel-heading h4"><i class="ico-vline"></i>会员注册</div>
            <div class="panel-body">
            	<div class="article-box">
	                <p>1、为保护会员的隐私，未登录的游客只能浏览采媒在线首页，必须注册会员才能查看“媒体列表页”、“广告悬赏”</p>
	                <p>2、媒体会员可在成功注册后创建若干媒体信息，并可根据自己下属媒体的档期和服务类别去“广告悬赏”接单，所有成功创建的媒体可享受采媒在线免费提供的“信息展示”服务和合适的“广告需求推送”服务。</p>
	                <p>3、企业会员可注册为“广告主”类型的会员，广告主可查看所有媒体的信息和服务报价，并可以免费创建自己的“广告投放需求”，等待或邀请媒体来接单。</p>                
	              	<p> 采媒在线会员注册界面简洁友好，仅需手机号就能注册成功！</p>
	                <a href="${ctx }/register">
	                	<img src="${ctx }/static/assets/img/memberregister-01.png" class="img-responsive">
	                </a>
	                <shiro:guest>
	                	<a href="${ctx }/register" class="highlight-blue">立即去注册采媒在线会员</a>
	                </shiro:guest>
	                <shiro:authenticated>
	                	<a href="${ctx }/" class="highlight-blue">立即去注册采媒在线会员</a>
	                </shiro:authenticated>
                </div>
                
            </div>
        </div>
        <!--// Panel End-->



<!--=== End Container ===-->


<script>
$(function() {
	menu.addActive('#memberRegister');		
});

</script>

    
</body>
</html>
