<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>查看媒体</title>
</head>
<body>
       <div class="panel panel-default">                    
           <div class="panel-heading">
               <ul class="breadcrumb">
                 <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
                 <li>报价属性</li>
                 <li class="active">查看媒体</li>
             </ul>
           </div>
           <div class="panel-body">
               <div class="topwrap">
                   <form class="form-inline">                             
                       <div class="form-group form-group-sm"> 
                           <label>手机</label>
                           <input name="search_LIKE_customer.mobPhone" value="${param['search_LIKE_customer.mobPhone'] }" type="text" class="form-control"> 
                       </div>                             
                       <div class="form-group form-group-sm"> 
                           <label>昵称</label>
                           <input name="search_LIKE_name" value="${param.search_LIKE_name }" type="text" class="form-control"> 
                       </div> 
                       <div class="form-group form-group-sm"> 
                           <label>创建时间</label>
                           <div class="input-group">
                           <input type="text" class="form-control" id="search_GTE_createTime" name="search_GTE_createTime" value="${param.search_GTE_createTime }" 
                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_createTime\')}'})">
                           <div class="input-group-addon">至</div>
                           <input type="text" class="form-control" id="search_LTE_createTime" name="search_LTE_createTime" value="${param.search_LTE_createTime }" 
                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})">
                           </div>
                       </div>
                       <div class="form-group form-group-sm"> 
                           <label>大类</label>
                           <select class="form-control" name="search_EQ_mediaType"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaTypes}" var="mediaType">
                               <option value="${mediaType.itemCode }" <c:if test="${mediaType.itemCode eq param.search_EQ_mediaType }">selected</c:if>>${mediaType.itemName }</option>
                               </c:forEach>
                           </select>
                       </div>                                
                       <div class="form-group form-group-sm"> 
                           <label>实名认证</label>
                           <select class="form-control" name="search_EQ_customer.certified"> 
                               <option value="">不限</option>
                               <option value="true" <c:if test="${param['search_EQ_customer.certified'] eq 'true' }">selected</c:if>>是</option>
                               <option value="false" <c:if test="${param['search_EQ_customer.certified'] eq 'false' }">selected</c:if>>否</option>
                           </select>
                       </div> 
                       <div class="form-group form-group-sm"> 
                           <label>级别</label>
                           <select class="form-control" name="search_EQ_level"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaLevels}" var="mediaLevel">
                               <option value="${mediaLevel.itemCode }" <c:if test="${mediaLevel.itemCode eq param.search_EQ_level }">selected</c:if>>${mediaLevel.itemName }</option>
                               </c:forEach>
                           </select>
                       </div>
                       <div class="form-group form-group-sm"> 
                           <label>状态</label>
                           <select class="form-control" name="search_EQ_status"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaStatus}" var="mediaStatus">
                               <option value="${mediaStatus.itemCode }" <c:if test="${mediaStatus.itemCode eq param.search_EQ_status }">selected</c:if>>${mediaStatus.itemName }</option>
                               </c:forEach>
                           </select>
                       </div>
                       <div class="form-group form-group-sm"> 
                           <label>报价属性</label>
                           <select class="form-control" name="search_EQ_star.id"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaStars}" var="mediaStar">
                               <option value="${mediaStar.id }" <c:if test="${mediaStar.id eq param['search_EQ_star.id'] }">selected</c:if>>${mediaStar.name }</option>
                               </c:forEach>
                           </select>
                       </div>  
                       <div class="form-group form-group-sm">                                
                       <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
                       </div>
                   </form>
               </div>

               <table class="table table-bordered table-condensed table-hover table-photos">
                   <thead>
                       <tr class="thead">
                           <th>头像</th>
                           <th>大类</th>
                           <th>认证类型</th>
                           <th>昵称</th>
                           <th>星级</th>
                           <th></th>
                       </tr>
                   </thead>
                   <tbody>
                       <c:forEach items="${data.content }" var="item">
                       <tr <c:if test="${item.status eq 'MEDIA_S_DISABLED' }">style="background:#ccc"</c:if> >
                           <td><img src="<zy:fileServerUrl value="${item.showPic }"/>" class="photo"></td>
                           <td><zy:dic value="${item.mediaType }"/></td>
                           <td><zy:dic value="${item.category }"/></td>                            
                           <td>${item.name }</td>
                           <td>${item.star.name }</td>
                           <td>
                               <div class="btn-group">
                                   <button class="btn btn-primary btn-sm" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" id="updateMedia">
                                   <i class="fa fa-edit"></i> 修改星级 <i class="fa fa-caret-down"></i>
                                   </button>
                                   <ul class="dropdown-menu radio" aria-labelledby="" style="right:0;left:auto; float:right;background:#337ab7">
	                                   <c:forEach items="${mediaStars }" var="mediaStar">
	                                       <li><button onclick="return assignStar('${mediaStar.id}', '${item.id}');" class="btn btn-primary btn-sm">${mediaStar.name }</button></li>
	                                   </c:forEach>
                                   </ul>
                               </div>                           
                           </td>
                       </tr>
                       </c:forEach>
                   </tbody>
               </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
<script type="text/javascript">

$(function() {
    menu.active('#media-star');
});

function assignStar(starId, mediaId) {
	var msg = '您确定要修改此媒体星级吗?';
    bootbox.confirm(msg, function(result) {
        if (result) {
            $.post("${ctx}/admin/mediaStar/assign?ajax",{id: starId, mediaId: mediaId},function(result){
            	if(result)
                	window.location.reload();
            });
        }
    });
    return false;
}

</script>        
</body>
</html>
