<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html>
<head>
    <title>采媒在线</title>
    
    <script src="${ctx}/static/assets/js/jQueryRotate.2.2.js"></script>
    <script src="${ctx}/static/assets/js/jquery.easing.min.js"></script>
    
	<script type="text/javascript" src="${ctx}/static/bower_components/flipcountdown/jquery.flipcountdown.js"></script>
	<link rel="stylesheet" href="${ctx}/static/bower_components/flipcountdown/jquery.flipcountdown.css">  
</head>

<body>
<style type="text/css">
    .mingdan {
        float:right;
        width:445px; 
        height:625px; 
        background:url(${ctx}/static/assets/img/huodong-mingdan.png) no-repeat top center;
        overflow:hidden;
    }
    .mingdan-box {
        width:100%;
        height:500px;
        margin-top:100px;
        overflow:hidden;
    }
    .mingdan-list {
        margin:0;
        padding:0;
        list-style:none;
    }
    .mingdan-list > li {
        height:58px;
        line-height:58px;
        padding:0 30px;
        font-size:16px;
        border-bottom:1px solid #ccc;
        overflow:hidden;
    }
    .mingdan-list > li span {
        display:inline-block;
        float:left;
    }
    .mingdan-list > li .tx {
        margin-top:15px;
        width:30px;
        height:30px;
        border:1px solid #ccc;
        border-radius:50%!important;
        overflow:hidden;
        vertical-align:middle;
    }
    .mingdan-list > li .tx img {
        width:100%;
        height:100%;
        vertical-align:top;
    }
    .mingdan-list > li .xm {
        width:80px;
        margin:0 15px;
        color:#ea5a4a;
        white-space:nowrap;
        overflow:hidden;
        text-overflow: ellipsis;    
    }
    .mingdan-list > li .xx {
        color:#333;
    }
    
    
    /**/
    .fudongceng {
        position:fixed;
        top:0; 
        left:0; 
        width:100%; 
        height:100%;  
        z-index:10000; 
        background-color:rgba(0,0,0,0.95);
    }
    
    
    .ly-plate{
        position:relative;
        width:650px;
        height:650px;
    }
    .rotate-bg{
        width:650px;
        height:650px;
        background:url(${ctx}/static/assets/img/zhuanpan.png);
        position:absolute;
        top:0;
        left:0
    }
    .ly-plate div.lottery-star{
        width:300px;
        height:300px;
        position:absolute;
        top:175px;
        left:175px;
        /*text-indent:-999em;
        overflow:hidden;
        background:url(rotate-static.png);
        -webkit-transform:rotate(0deg);*/
        outline:none
    }
    .ly-plate div.lottery-star #lotteryBtn{
        cursor: pointer;
        position: absolute;
        top:0;
        left:0;
        *left:-107px
    }    
    .ly-plate div.lottery-choujiang{
        position:absolute;
        top:285px;
        left:285px;
        /*text-indent:-999em;
        overflow:hidden;
        background:url(rotate-static.png);
        -webkit-transform:rotate(0deg);*/
        outline:none
    }
    .ly-plate div.lottery-choujiang img{
        cursor: pointer;
        position: absolute;
        top:0;
        left:0;
        *left:-107px
    }    
    .ly-plate div.lottery-loading{
        position:absolute;
        top:315px;
        left:315px;
        /*text-indent:-999em;
        overflow:hidden;
        background:url(rotate-static.png);
        -webkit-transform:rotate(0deg);*/
        outline:none
    }
</style> 

<!--=== Container ===-->
<div class="container-fluid" style="padding:0;">
    <div class="clearfix" style="background:url(${ctx}/static/assets/img/huodong-1p.jpg) no-repeat top center;height:427px;"></div>
    <div class="clearfix" style="background:url(${ctx}/static/assets/img/huodong-2p.jpg) no-repeat top center;height:949px;">
    <c:choose>
        <c:when test="${end }">
        <h1 style=" width:730px; font-size:60px; margin:115px auto 100px; color:#fff;">很遗憾! <span style="color:#fecc2f;">抽奖活动已经结束!</span></h1>
        </c:when>
        <c:when test="${empty times or times.times >= 3 }">
        <h1 style=" width:730px; font-size:60px; margin:115px auto 100px; color:#fff;">恭喜您! <span style="color:#fecc2f;">获得<span id="lottery-times">3</span>次抽奖机会!</span></h1>
        </c:when>
        <c:when test="${times.times < 3 }">
        <h1 style=" width:730px; font-size:60px; margin:115px auto 100px; color:#fff;">恭喜您! <span style="color:#fecc2f;">获得<span id="lottery-times">${times.times }</span>次抽奖机会!</span></h1>
        </c:when>
    </c:choose>
        <div class="container">
            <div class="row">
                <div class="col-xs-6">
				    <div class="ly-plate">
				        <div class="rotate-bg"></div>
				        <div class="lottery-star"><img src="${ctx}/static/assets/img/zhizhen.png" id="lotteryBtn"></div>
                        <div class="lottery-choujiang"><img src="${ctx}/static/assets/img/choujiang.png" id="lotteryBtn1"></div>
                        <div class="lottery-loading" id="loading" class="pull-right"><img src="${ctx}/static/legacy/img/loading.gif" /></div>                        
				    </div>                
				</div>
                <div class="col-xs-6">
                    <div class="mingdan">
                        <div class="mingdan-box">
                            <marquee direction="up" loop="-1" width="100%" height="500" >
                                <ul class="mingdan-list">
                                    <c:forEach items="${data }" var="item">
                                    <li><span class="tx"><img src="${ctx}/static/assets/img/user.jpg"></span><span class="xm">${item.customer.name }</span><span class="xx"><fmt:formatDate value="${item.createTime }" pattern="HH:mm"/>抽中 ${item.name }</span></li>
                                    </c:forEach>
                                </ul>
                            </marquee>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    
    </div>
    <div style="background:url(${ctx}/static/assets/img/huodong-3p.jpg) no-repeat top center; height:666px;"></div>
    <div style="background:url(${ctx}/static/assets/img/huodong-4p.jpg) no-repeat top center; height:427px;"></div>
</div>
<!--=== End Container ===-->

<c:if test="${wait }">
<div class="fudongceng">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div style=" position:relative; width:1170px; height:565px; margin-top:150px; background:url(${ctx}/static/assets/img/huodong-fudong.png) no-repeat left top; clear:both;">
                    <a href="${ctx }/" class="pull-right"><img src="${ctx}/static/assets/img/huodong-backtohome.png"></a>

<shiro:guest>
                    <a href="${ctx}/register" style=" position:absolute; left:600px; top:450px;">
                        <img src="${ctx}/static/assets/img/huodong-zhuce.png">
                    </a>
</shiro:guest>
<shiro:authenticated>
				    <table  style=" position:absolute; left:600px; top:450px; border:0px; color: white;">
				        <tr>
				            <td style="width:110px;text-align:center;">天</td>
				            <td style="width:120px;text-align:center;">小时</td>
				            <td style="width:120px;text-align:center;">分钟</td>
				            <td style="width:120px;text-align:center;">秒</td>
				        </tr>
				        <tr>
				            <td colspan="4"><span id="retroclockbox"></span></td>
				        </tr>
				    </table>                    
</shiro:authenticated>
                </div>
            </div>
        </div>
    </div>
    
</div>
</c:if>

<script>
$(function(){
<shiro:authenticated>
    var timeOut = function(){  //超时函数
        $("#lotteryBtn").rotate({
            angle:0, 
            duration: 10000, 
            animateTo: 2160, //这里是设置请求超时后返回的角度，所以应该还是回到最原始的位置，2160是因为我要让它转6圈，就是360*6得来的
            callback:function(){
                alert('网络超时')
            }
        }); 
    }; 
    var rotateFunc = function(awards,angle,text){  //awards:奖项，angle:奖项对应的角度
        $('#lotteryBtn').stopRotate();
        $("#lotteryBtn").rotate({
            angle:0, 
            duration: 5000, 
            animateTo: angle+1440, //angle是图片上各奖项对应的角度，1440是我要让指针旋转4圈。所以最后的结束的角度就是这样子^^
            callback:function(){
            	bootbox.alert(text);            	
            }
        }); 
    };
    
    var startRotateTime = 0;
    
    $("#lotteryBtn").rotate({ 
       bind: 
         { 
            click: function(){
                <c:if test="${end }">
                bootbox.alert("对不起，抽奖活动已经结束，请下次再来吧！");                
                return;
                </c:if>
                
            	var now = new Date().getTime();
            	if (now - startRotateTime - 6000 < 0) {
            		return;
            	}
            	
                $('#loading').show();
            	startRotateTime = now;
            	if ($("#lottery-times").html() == "0") {
                    bootbox.alert("对不起，您的抽奖次数已经用完！");                
            		return;
            	}
            	
            	$.post("${ctx}/lottery/start", function(data){
                    $('#loading').hide();
            		if (data.result) {
            			$("#lottery-times").html(data.times);
            			if (data.prize) {
            				if (data.prize.category == "LOTTERY_C_ADVERTISE") {
                                var angle = [22,112,202,292];
                                angle = angle[Math.floor(Math.random()*angle.length)]
            				    rotateFunc(1,angle,"恭喜您抽中" + data.prize.name + "，稍后会有客服与您取得联系！");
            				} else {
                                var angle = [67,247];
                                angle = angle[Math.floor(Math.random()*angle.length)]
                                rotateFunc(2,angle,"恭喜您抽中" + data.prize.name + "，稍后会有客服与您取得联系！");
            				}
            			} else {
                            var angle = [158,338];
                            angle = angle[Math.floor(Math.random()*angle.length)]
                            rotateFunc(0,angle,"很遗憾，这次您未抽中奖！");
            			}
            		} else {
                        rotateFunc(0,0,data.message);
            		}
            	}, "json");            	
            }
         } 
       
    });
    
    $("#lotteryBtn1").click(function() {
    	$("#lotteryBtn").click();    	
    })
    
    <c:if test="${wait }">
    var startTime = '${start_time}'.replace(/-/g,   "/");
    $('#retroclockbox').flipcountdown({
        size:"lg",
        beforeDateTime: startTime
    });

    var timeout = (new Date(startTime)).getTime() - <%=System.currentTimeMillis()%>;
    if (timeout > 0)
    window.setTimeout(function() {
        window.location.reload();
    }, timeout);
    </c:if>
    
</shiro:authenticated>

<c:if test="${not end }">
<shiro:guest>
	$("#lotteryBtn").click(function() {
	    $("#loginBtn").click();       
	})
	$("#lotteryBtn1").click(function() {
	    $("#lotteryBtn").click();       
	})
</shiro:guest>
</c:if>
})
</script>
</body>
</html>

