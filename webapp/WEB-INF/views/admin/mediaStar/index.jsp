<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>报价属性</title>
</head>
<body>
       <div class="panel panel-default">                    
           <div class="panel-heading">
               <ul class="breadcrumb">
                 <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
                 <li class="active">报价属性</li>
             </ul>
           </div>
           <div class="panel-body">
               <table class="table table-bordered table-condensed table-hover table-photos">
                   <thead>
                       <tr class="thead">
                           <th>星级</th>
                           <th>媒体数</th>
                           <th>微信</th>
                           <th>微博</th>
                           <th>操作</th>
                       </tr>
                   </thead>
                   <tbody>
                       <c:forEach items="${mediaStars }" var="item">
                       <tr>
                           <td>${item.name }(<fmt:formatNumber value='${item.percent*100 }' pattern="0"/>%)</td>
                           <td><zy:countStarMedia id="${item.id }" /></td>                            
                           <td><zy:countStarMedia id="${item.id }" mediaType="MEDIA_T_WEIXIN" /></td>                            
                           <td><zy:countStarMedia id="${item.id }" mediaType="MEDIA_T_WEIBO" /></td>                            
                           <td>
	                            <div class="btn-group" role="group" aria-label="...">
	                                <a href="${ctx }/admin/mediaStar/medias?search_EQ_star.id=${item.id }" class="btn btn-default btn-sm preview-btn"><i class="fa fa-list"></i>查看媒体</a>
	                            </div>                
                           </td>
                       </tr>
                       </c:forEach>
                   </tbody>
               </table>
        </div>
    </div>
<script type="text/javascript">

$(function() {
    menu.active('#media-star');
});

</script>        
</body>
</html>
