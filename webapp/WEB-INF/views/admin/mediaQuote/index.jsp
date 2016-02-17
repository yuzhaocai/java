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
                           <input name="search_LIKE_media.customer.mobPhone" value="${param['search_LIKE_media.customer.mobPhone'] }" type="text" class="form-control"> 
                       </div>                             
                       <div class="form-group form-group-sm"> 
                           <label>昵称</label>
                           <input name="search_LIKE_media.name" value="${param.search_LIKE_media.name }" type="text" class="form-control"> 
                       </div> 
                       <div class="form-group form-group-sm"> 
                           <label>创建时间</label>
                           <div class="input-group">
                           <input type="text" class="form-control" id="search_GTE_media.createTime" name="search_GTE_media.createTime" value="${param.search_GTE_media.createTime }" 
                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_media.createTime\')}'})">
                           <div class="input-group-addon">至</div>
                           <input type="text" class="form-control" id="search_LTE_media.createTime" name="search_LTE_media.createTime" value="${param.search_LTE_media.createTime }" 
                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_media.createTime\')}'})">
                           </div>
                       </div>
                       <div class="form-group form-group-sm"> 
                           <label>大类</label>
                           <select class="form-control" name="search_EQ_media.mediaType"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaTypes}" var="mediaType">
                               <option value="${mediaType.itemCode }" <c:if test="${mediaType.itemCode eq param.search_EQ_media.mediaType }">selected</c:if>>${mediaType.itemName }</option>
                               </c:forEach>
                           </select>
                       </div>                                
                       <div class="form-group form-group-sm"> 
                           <label>实名认证</label>
                           <select class="form-control" name="search_EQ_media.customer.certified"> 
                               <option value="">不限</option>
                               <option value="true" <c:if test="${param['search_EQ_media.customer.certified'] eq 'true' }">selected</c:if>>是</option>
                               <option value="false" <c:if test="${param['search_EQ_media.customer.certified'] eq 'false' }">selected</c:if>>否</option>
                           </select>
                       </div> 
                       <div class="form-group form-group-sm"> 
                           <label>级别</label>
                           <select class="form-control" name="search_EQ_media.level"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaLevels}" var="mediaLevel">
                               <option value="${mediaLevel.itemCode }" <c:if test="${mediaLevel.itemCode eq param['search_EQ_media.level'] }">selected</c:if>>${mediaLevel.itemName }</option>
                               </c:forEach>
                           </select>
                       </div>
                       <div class="form-group form-group-sm"> 
                           <label>状态</label>
                           <select class="form-control" name="search_EQ_media.status"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaStatus}" var="mediaStatus">
                               <option value="${mediaStatus.itemCode }" <c:if test="${mediaStatus.itemCode eq param['search_EQ_media.status'] }">selected</c:if>>${mediaStatus.itemName }</option>
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
                           <th>报价类型</th>
                           
                           
                           <th>结算价</th>
                           <th>报价</th>
                           <th>含税</th>
                           <th>修改方式</th>
                           <th></th>
                       </tr>
                   </thead>
                   <tbody>
                       <c:forEach items="${data.content }" var="item">
                       <tr>
                           <td><img src="<zy:fileServerUrl value="${item.media.showPic }"/>" class="photo"></td>
                           <td><zy:dic value="${item.media.mediaType }"/></td>
                           <td><zy:dic value="${item.media.category }"/></td>                            
                           <td>${item.media.name }</td>
                           <td><zy:dic value="${item.type }"/></td>
                           <td>${item.priceMedia }</td>
                           <td>${item.price }</td>
                           <td>
                           <c:choose>
                           
                           <c:when test="${item.modifyType eq 'ADMIN_QUOTE'}">
                                ---
                           </c:when>
                           <c:when test="${item.media.provideInvoice}">
                                ---
                           </c:when>
                           <c:otherwise>
                                ${item.tax}
                           </c:otherwise>
                           </c:choose>
                           </td>
                           <td><zy:dic value="${item.modifyType }"/></td>
                           <td>
                                <div class="btn-group" role="group" aria-label="...">
                                    <a href="javascript:void()"  onclick="editPrice('${item.id }');" class="btn btn-default btn-sm preview-btn"><i class="fa fa-edit"></i>修改报价</a>
                                </div>
                           </td>
                       </tr>
                       </c:forEach>
                   </tbody>
               </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
    
    <form id="actionForm" action="${ctx}/admin/mediaQuote/update" method="post">
       <input type="hidden" id="media-quote-id" name="id">
       <input type="hidden" id="media-quote-price" name="price">
       
    </form>    
<script type="text/javascript">

$(function() {
    menu.active('#media-quote-list');
});

function editPrice(id) {
	var msg = '请输入新的报价：';
    bootbox.prompt(msg, function(result) {
        if (result) {
        	var price = parseInt(result);
        	if (isNaN(price) || price < 0) {
        	    bootbox.alert("请输入正整数价格！");
        	} else {
        		$("#media-quote-id").val(id);
        		$("#media-quote-price").val(price);
        		$("#actionForm").submit();
        	}
        }
    });
    return false;
}

</script>        
</body>
</html>
