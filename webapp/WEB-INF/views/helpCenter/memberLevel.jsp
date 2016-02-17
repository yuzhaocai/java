<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!doctype html>
<html>
<head>
    <title>会员等级</title>
</head>

<body>

 
<!--=== Container ===-->

        <!-- Main -->
        <div class="panel panel-default">
        	<div class="panel-heading h4"><i class="ico-vline"></i>会员等级</div>
            <div class="panel-body">
            	<div class="article-box">
               	  <div class="p-title no1">
                        <h5>&nbsp;&nbsp;会员等级<br><small class="text-uppercase">Member Level</small></h5>
                    </div>
                    <p>采媒在线会员可通过“等级成长”来获取越来越多的“会员特权”<br>
会员根据“信用值”的不同分为vip1至vip9级</p>
					<img src="${ctx }/static/assets/img/memberlevel-01.png" class="img-responsive">
					<div class="p-title no2" id="tq" >
                        <h5>&nbsp;&nbsp;特权详解<br><small class="text-uppercase">Privilege Introduction</small></h5>
                    </div>

                    <p >信息免审：获得该特权的采媒在线会员，创建、修改广告投放需求或媒体时，无须先通过后台信息审核，会立即出现在网页上。</p>
                    
                    <p >佣金优惠：成功交易的订单，采媒在线平台对该订单媒体会员方会收取有一定比例的佣金，获得“佣金优惠”的会员可免交一定比例的佣金。</p>
                    
                    <p >红名特权：获得该特权的会员在成功创建“媒体”或“广告投放需求”后，会在网页标红显示，标红显示会彰显会员的可信度，“媒体”更容易接到订单，“需求”更容易获得媒体的应邀</p>
                    
                    <p >列表页优先显示：获得该特权的会员所创建的媒体将在所属媒体列表页中排序靠前。</p>
					<div class="p-title no3">
                        <h5>&nbsp;&nbsp;什么是信用值？怎么获得？<br><small class="text-uppercase">What is the credit coin？How to get it?</small></h5>
                    </div>
                  <p>信用值是采媒在线会员信用度和级别的参考标准，会员信用值由以下行为增加或减少：</p>
                    <img src="${ctx }/static/assets/img/memberlevel-02.png" width="311" height="192" class="img-responsive">
                </div>
                
           
        
        
    
    </div>
</div>



<!--=== End Container ===-->


<script>
$(function() {
	menu.addActive('#memberLevel');		
});

</script>

    
</body>
</html>
