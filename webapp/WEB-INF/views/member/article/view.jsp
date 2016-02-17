<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="printable" >
    
    <title>预览</title>
    
</head>

<body>

<!--=== Blog Post===-->  
<div class="container">
    <div class="blog blog-full-width margin-bottom-40">
    
        <h2><strong><c:out value="${article.title }" /></strong></h2>
        
        <div class="blog-post-tags">
            <ul class="list-unstyled list-inline blog-info">
                <li><i class="fa fa-calendar"></i> <fmt:formatDate value="${article.modifyTime }" pattern="yyyy.MM.dd"/></li>
                <li><i class="fa fa-pencil"></i> 采媒在线</li>
                <li><i class="fa fa-comments"></i> ${article.click } 次</a></li>
                <%-- 
                <li><button class="btn btn-link btn-xs btn-share"><i class="fa fa-share"></i> 分享</button></li>
                --%>
            </ul>                    
        </div>
           
        <!-- Tag Box v6 --> 
        <div class="tag-box tag-box-v6"><%-- 摘要 --%>
            ${article.summary }
        </div>
        <!-- End Tag Box v6 -->
        
        <%-- 
        <div class="blog-img">
            <img src="assets/img/portfolio/prod1.jpg" class="img-responsive">
        </div>
        --%>  
        
        <div class="blog-cont">
        
        	<c:out value="${article.content }" escapeXml="false" />
        
        </div>

        <%-- 
        <p class="text-center"><button type="button" class="btn btn-success col-xs-offset-4 col-xs-4 btn-share"><i class="fa fa-share"></i> 分享</button></p>
        --%>
    </div>
</div>
<!--=== End Blog Post ===-->



<!--=== Footer ===-->
	<div class="container">
    	<div id="footer-2015">
        <hr>
        <div class="copyright">
        	<jsp:useBean id="now" class="java.util.Date"></jsp:useBean>
            Copyright©2004-<fmt:formatDate value="${now }" pattern="yyyy"/>&nbsp;&nbsp;采媒在线&nbsp;版权所有
        </div>
    </div>
    </div>
<!--=== End Footer ===-->


</body>
</html>

