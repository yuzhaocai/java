<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!--=== Footer Version 1 ===-->

<div id="footer" class="container-fluid footer-v1">
    <div class="row footer">
<%--         <div class="container">
            <div class="row">
                <!-- About -->
                <div class="col-xs-2">
                    <a href="index.html"><img id="logo-footer" class="footer-logo" src="${ctx}/static/assets/img/logo6-footer.png" alt=""></a>
                </div><!--/col-md-3-->
                <!-- End About -->
                <!-- 微信，微博二维码 -->
				<div class="col-xs-3 padding-top-20">
					<img class="margin-right-10 margin-left-10 margin-top-20" src="${ctx }/static/assets/img/qrcode-wechat.jpg">
					<img class="margin-right-10 margin-left-10 margin-top-20" src="${ctx }/static/assets/img/qrcode-weibo.jpg">
				</div>

                <!-- 关于我们 -->
                <div class="col-xs-2">
                    <div class="posts">
                        <div class="headline"><h2>关于我们</h2></div>
                        <ul class="list-unstyled simple-list">
                            <li><a href="http://www.cnmei.com/cms/">关于采媒在线</a></li>
                            <li><a href="http://www.cnmei.com/cms/index.php/contact">联系我们</a></li>
                            <li><a href="http://www.cnmei.com/cms/index.php/category/news">媒体报道</a></li>
                            <li><a href="http://www.cnmei.com/cms/index.php/joinus">加入我们</a></li>
                        </ul>
                    </div>
                </div><!--/col-md-3-->  
                <!-- 结束 --> 
                
                <!-- 服务合作 -->
                <div class="col-xs-2">
                    <div class="headline"><h2>服务合作</h2></div>
                    <ul class="list-unstyled simple-list">
                        <li><a href="http://www.cnmei.com/cms/index.php/media_entering">媒体入驻</a></li>
                        <li><a href="http://www.cnmei.com/cms/index.php/enterprise_entering">企业入驻</a></li>
                        <li><a href="http://www.cnmei.com/cms/index.php/terms">服务条款</a></li>
                        <li><a href="http://www.cnmei.com/cms/index.php/custom">私人定制</a></li>
                    </ul>
                </div><!--/col-md-3-->
                <!-- 结束 -->                   

                <!-- 新手指南 -->
                <div class="col-xs-2">
                    <div class="headline"><h2>新手指南</h2></div>
                    <ul class="list-unstyled simple-list">
                        <li><a href="http://www.cnmei.com/cms/index.php/adv_processing">广告需求投放流程</a></li>
                        <li><a href="http://www.cnmei.com/cms/index.php/member_register">会员注册</a></li>
                        <li><a href="http://www.cnmei.com/cms/index.php/member_level">会员等级</a></li>
                        <li><a href="http://www.cnmei.com/cms/index.php/faq">帮助中心</a></li>
                    </ul>
                </div><!--/col-md-3-->
                <!-- 结束 -->   
            </div>
        </div> --%>
    </div><!--/footer--> 

    <div class="copyright row">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">                     
                    <p>
                       版权所有：北京媒立方传媒科技有限公司   京ICP备：15054184号
                    </p>
                    <div class="img-item">
                    	<a href="http://www.bj.cyberpolice.cn/index.jsp" target="_blank" title="网络110报警服务"><img src="${ctx}/static/assets/img/copyright-img1.jpg" width="120" height="50"></a>
                    	<a href="http://www.itrust.org.cn/" target="_blank" title="中国互联网协会"><img src="${ctx}/static/assets/img/copyright-img2.jpg" width="120" height="50"></a>
                    	<a href="http://www.12377.cn/" target="_blank" title="不良信息举报中心"><img src="${ctx}/static/assets/img/copyright-img3.jpg" width="120" height="50"></a>
						<!--可信网站图片LOGO安装开始-->
						<a href="https://ss.knet.cn/verifyseal.dll?sn=e15121511010561814q41x000000&amp;ct=df&amp;a=1&amp;pa=0.7636462515220046" id="kx_verify" tabindex="-1" target="_blank" kx_type="图标式" style="display:inline-block;"><img src="${ctx }/static/assets/img/cnnic.png" style="border:none;" oncontextmenu="return false;" alt="可信网站"></a>
						<!--可信网站图片LOGO安装结束-->
                    </div>
                </div>
            </div>
        </div> 
    </div><!--/copyright-->
</div>
<!--=== End Footer Version 1 ===-->
<script type="text/javascript">
$.ajax({
    url: "${ctx}/cmsFooter",
    global: false,
    type: "POST",
    dataType: "json",
    async: false,
    success: function(rst) {
    	if(rst.result){
        	$('#footer > .footer').html(rst.data);
    	}
    }
});
</script>
<script type="text/javascript" src="${ctx}/static/assets/plugins/back-to-top.js"></script>