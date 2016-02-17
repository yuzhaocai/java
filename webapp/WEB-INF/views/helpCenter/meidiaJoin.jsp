<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!doctype html>
<html>
<head>
    <title>媒体入驻</title>
</head>

<body>

        <!-- Main -->
        <div class="panel panel-default">
        	<div class="panel-heading h4"><i class="ico-vline"></i>媒体入驻</div>
            <div class="panel-body">
                <div class="article-box">
                    <div class="p-title no1">
                        <h5>&nbsp;&nbsp;媒体入驻<br><small class="text-uppercase">Media entering</small></h5>
                    </div>
                    <img src="${ctx }/static/assets/img/mediajoin.png" class="img-responsive"> 
                	<div class="margin-top-20">
                		<shiro:guest>
	                    	<h5>1、<a href="${ctx }/register" class="highlight-blue">会员注册</a></h5>
	                    	<p>“会员类型”请选择“我是媒体”</p>                    
	                        <a href="${ctx }/register"><img src="${ctx }/static/assets/img/mediajoin-01.png" ></a>
                        </shiro:guest>
                        <shiro:authenticated>
                        	<h5>1、<a href="${ctx }/" class="highlight-blue">会员注册</a></h5>
	                    	<p>“会员类型”请选择“我是媒体”</p>                    
	                        <a href="${ctx }/"><img src="${ctx }/static/assets/img/mediajoin-01.png" ></a>
                        </shiro:authenticated>
                    	<h5>2、会员登录后，进入“会员中心”，点击“我的媒体”---“创建新媒体”</h5>
                        <img src="${ctx }/static/assets/img/mediajoin-02.png">
                    </div>
                </div>
            </div>
        </div>



<!--=== End Container ===-->





<script>
$(function() {
	menu.addActive('#meidiaJoin');		
});


</script>

    
</body>
</html>
