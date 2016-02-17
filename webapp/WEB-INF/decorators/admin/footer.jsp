<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- footer -->   
<div id="footer" class="row footer" style="padding: 10px 0;">
  <div class="container">
    <div class="footer_top"></div>
    <div class="footer-bottom text-center">
        Copyright <span class="glyphicon glyphicon-copyright-mark"></span> <fmt:formatDate value="${now }" pattern="yyyy"/> Lczy Inc. All Rights Reserved.&nbsp;&nbsp;北京龙采正元软件技术有限公司 版权所有
    </div>
  </div>
</div>

<!-- /footer -->

<tags:errors />

<script type="text/javascript">

$(function() {
	
    $(document).ajaxStart(function() {
        $('#loading').show();
    }).ajaxStop(function() {
        $('#loading').hide();
    });
    
    if ($(document).height() > $(window).height()) {
    	$('#scrollUp').show();
    } else {
    	$('#scrollUp').hide();
    };
    
    $(function () {
        $('[data-toggle="popover"]').popover();
    });
    //后台管理去掉投诉建议和联系QQ
    $('#complaintControl').remove();
    $('#qqControl').remove();
});
</script>