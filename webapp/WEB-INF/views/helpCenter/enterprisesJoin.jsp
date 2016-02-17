<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!doctype html>
<html>
<head>
    <title>企业入驻</title>
</head>

<body>

 

        <!-- Main -->
        <div class="panel panel-default">
        	<div class="panel-heading h4"><i class="ico-vline"></i>企业入驻</div>
            <div class="panel-body">
                <div class="article-box">
                    <div class="p-title no1">
                        <h5>&nbsp;&nbsp;企业入驻<br><small class="text-uppercase">enterprise entering</small></h5>
                    </div>
                    <img src="${ctx }/static/assets/img/enterprisesjoin.png" class="img-responsive"> 
                	<div class="margin-top-20">
	                	<shiro:guest>
	                		<h5>1、<a href="${ctx }/register" class="highlight-blue">会员注册</a></h5>
	                    	<p>“会员类型”请选择“我是广告主”</p>                    
	                        <a href="${ctx }/register"><img src="${ctx }/static/assets/img/enterprisesjoin-01.png" class="img-responsive"/></a>
	                	</shiro:guest>
	                	<shiro:authenticated>
	             		    <h5>1、<a href="${ctx }/" class="highlight-blue">会员注册</a></h5>
	                    	<p>“会员类型”请选择“我是广告主”</p>                 
	                        <a href="${ctx }/"><img src="${ctx }/static/assets/img/enterprisesjoin-01.png" class="img-responsive"/></a>
	                	</shiro:authenticated>
                    	<h5>2、为保证您的资金安全，享受更好的媒体服务。请在登录后进入“会员中心”，点击“用户信息”完善客户信息</h5>
                        <img src="${ctx }/static/assets/img/enterprisesjoin-02.png">
                    </div>
                </div>
            </div>
        </div>




<!--=== End Container ===-->


<script>
$(function() {
	menu.addActive('#enterprisesJoin');		
});

</script>

    
</body>
</html>
