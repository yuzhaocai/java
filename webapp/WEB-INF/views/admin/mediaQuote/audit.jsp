<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>报价审核</title>
</head>
<body>
       <div class="panel panel-default">                    
           <div class="panel-heading">
               <ul class="breadcrumb">
                 <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
                 <li class="active">报价审核</li>
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
                           <input name="search_LIKE_media.name" value="${param['search_LIKE_media.name'] }" type="text" class="form-control"> 
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
                           <select class="form-control" name="search_EQ_media.mediaType"> 
                               <option value="">不限</option>
                               <c:forEach items="${mediaTypes}" var="mediaType">
                               <option value="${mediaType.itemCode }" <c:if test="${mediaType.itemCode eq param['search_EQ_media.mediaType'] }">selected</c:if>>${mediaType.itemName }</option>
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
                           <label>审核状态</label>
                           <select class="form-control" name="search_EQ_status"> 
                               <option value="">不限</option>
                               <c:forEach items="${quoteAuditStatus}" var="status">
                               <option value="${status.itemCode }" <c:if test="${status.itemCode eq param['search_EQ_status'] }">selected</c:if>>${status.itemName }</option>
                               </c:forEach>
                           </select>
                       </div>
                       <div class="form-group form-group-sm">                                
                       <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
                       </div>
                   </form>
               </div>
 			<div class="subtt">
                <div class="btn-group-sm">
                    <button type="button" class="btn btn-primary" id="pass-btn"><span class="glyphicon glyphicon-ok"></span>通过</button>
                    <button type="button" class="btn btn-danger" id="reject-btn"><span class="glyphicon glyphicon-remove"></span>拒绝</button>
                </div>
            </div>
            <form id="audit-form" method="post">
               <table class="table table-bordered table-condensed table-hover table-photos">
                   <thead>
                       <tr class="thead">
                       	   <th class="text-center"></th>
                           <th>头像</th>
                           <th>大类</th>
                           <th>认证类型</th>
                           <th>昵称</th>
                           <th>报价类型</th>
                           <th>结算价</th>
                           <th>报价</th>
                           <th>含税</th>
                           <th>修改方式</th>
                           <th>提交时间</th>
                           <th>审核状态</th>
                           <th>提交人</th>
                           <th>审核人</th>
                           <th></th>
                       </tr>
                   </thead>
                   <tbody>
                       <c:forEach items="${data.content }" var="item">
                       <tr>
                       	   <td class="text-center">
                       	   	<c:if test="${item.status eq 'AUDIT' }">
                       	   		<input type="checkbox" class="chk_group_item" name="ids" value="${item.id }">
                       	   	</c:if>
                       	   </td>
                           <td><img src="<zy:fileServerUrl value="${item.media.showPic }"/>" class="photo"></td>
                           <td><zy:dic value="${item.media.mediaType }"/></td>
                           <td><zy:dic value="${item.media.category }"/></td>                            
                           <td>${item.media.name }</td>
                           <td><zy:dic value="${item.type }"/></td>
                           
                           <c:choose>
                           <c:when test="${item.modifyType eq 'ADMIN_QUOTE'}">
                           <td>${item.priceMedia }</td>
                           <td><b>${item.price }</b></td>
                           <td>---</td>
                           </c:when>
                           
                           <c:when test="${(item.modifyType eq 'ADMIN_MEDIAQUOTE') or (item.modifyType eq 'MEDIA')}">
                           <td><b>${item.priceMedia }</b></td>
                           <td>${item.price }</td>
                           <td>
                           <c:choose>
                           <c:when test="${item.media.provideInvoice}">---</c:when>
                           <c:otherwise>${item.tax}</c:otherwise>
                           </c:choose>
                           </td>
                           </c:when>
                           
                           <c:otherwise>
                           <td>---</td>
                           <td>---</td>
                           <td>---</td>
                           </c:otherwise>
                           </c:choose>
                           

                           <td><zy:dic value="${item.modifyType }"/></td>
                           
                           <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                           <td><zy:dic value="${item.status }"/></td>
                           <td>${item.createBy}</td>
                           <td>${item.auditBy}</td>
                           <td>
                                <div class="btn-group" role="group" aria-label="...">
                                    <c:if test="${item.status eq 'AUDIT' }">
                                    <a href="javascript:void(0)" onclick="pass('${item.id }', '${item.modifyType}');" class="btn btn-default btn-sm preview-btn"><i class="fa fa-check"></i>通过</a>
                                    <a href="javascript:void(0)" onclick="reject('${item.id }');" class="btn btn-default btn-sm preview-btn"><i class="fa fa-times"></i>拒绝</a>
                                    </c:if>
                                    <a href="${ctx }/admin/mediaQuote/history/${item.id}" class="btn btn-default btn-sm preview-btn"><i class="fa fa-list"></i>修改记录</a>
                                </div>
                           </td>
                       </tr>
                       </c:forEach>
                   </tbody>
               </table>
               </form>
            <tags:pagination page="${data}" />
        </div>
    </div>
    
    <form id="actionForm" action="" method="post">
       <input type="hidden" id="media-quote-log-id" name="id">
    </form>    
    
    <div class="modal fade modal-sm" id="invoice-modal" tabindex="-1" role="dialog" data-focus-on="input:first" data-width="700" ></div>
    
<script type="text/javascript">

$(function() {
    menu.active('#media-quote-audit');
});

function pass(id, modifyType) {
    $("#media-quote-log-id").val(id);
    $("#actionForm").attr("action", "${ctx}/admin/mediaQuote/audit/pass");
    
	if (modifyType == 'INVOICE') {
	    $("#invoice-modal").loadModal('${ctx}/admin/mediaQuote/audit/detail?ajax&id=' + id);
	} else {
	    var msg = '您确认要通过此报价吗？';
	    bootbox.confirm(msg, function(result) {
	        if (result) {
	       		$("#actionForm").submit();
	        }
	    });
	}
    return false;
}

function reject(id) {
    $("#media-quote-log-id").val(id);
    $("#actionForm").attr("action", "${ctx}/admin/mediaQuote/audit/reject");
	var msg = '您确认要拒绝此报价吗？';
    bootbox.confirm(msg, function(result) {
        if (result) {
            $("#actionForm").submit();
        }
    });
    return false;
}

$("#reject-btn").click(function() {
	if ($("input[name='ids']:checked").size() > 0) {
	    bootbox.confirm("您确定要审核屏蔽选中需求吗?", function(result) {
	        if (result) {
	            $("#audit-form").attr("action", "${ctx}/admin/mediaQuote/audit/rejectes");
	            $("#audit-form").submit();
	        }
	    });
	} else {
		bootbox.alert("请至少选择一个媒体报价!");
	}
});

$("#pass-btn").click(function() {
    if ($("input[name='ids']:checked").size() > 0) {
        bootbox.confirm("您确定要审核通过选中需求吗?", function(result) {
            if (result) {
                $("#audit-form").attr("action", "${ctx}/admin/mediaQuote/audit/passes");
                $("#audit-form").submit();
            }
        });
    } else {
        bootbox.alert("请至少选择一个媒体报价!");
    }
});
</script>        
</body>
</html>
