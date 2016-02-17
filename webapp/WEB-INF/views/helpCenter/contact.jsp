<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!doctype html>
<html>
<head>
    <title>联系我们</title>
</head>

<body>

 

        <!-- Main -->
        <div class="panel panel-default">
        	<div class="panel-heading h4"><i class="ico-vline"></i>联系我们</div>
            <div class="panel-body">
            	<img src="${ctx }/static/assets/img/map.jpg" class="img-responsive"> 
                <div class="article-box">
                    <div class="p-title no1">
                        <h5>&nbsp;&nbsp;联系我们<br><small class="text-uppercase">contact	us</small></h5>
                    </div>
                    <p>
                		地址：北京市海淀区四道口路大钟寺8号院8号楼</p>
                    <p>
						客户服务：<br>
						 联系人：徐小姐<br>
						TEL:010-57139905<br>
						E-mail:kefu@cnmei.com<br>
                    </p>
                    <p>
                   		媒体合作：<br>
						联系人：张先生<br>
						TEL:15313671763<br>
						E-mail:zhangzichen@cnmei.com<br>
                   	</p>
                   	<p>
                   		 市场合作：<br>
						联系人：林先生<br>
						TEL：15313671759<br>
						E-mail：vipservice@cnmei.com<br>
                   	</p>
<!--                   	<p> -->
<!--                    		 私人定制：<br> -->
<!--                    	</p> -->
                </div>
            </div>
        </div>




<!--=== End Container ===-->




<script>
$(function() {
	menu.addActive('#contact');		
});

</script>

    
</body>
</html>
