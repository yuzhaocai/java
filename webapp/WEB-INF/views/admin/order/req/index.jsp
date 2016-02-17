<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>需求查询</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">需求查询</li>
          </ul>
        </div>
        <div class="panel-body">
            <div class="topwrap">
                <form class="form-inline">                             
                    <div class="form-group form-group-sm"> 
                        <label>编号</label>
                        <input name="search_LIKE_id" value="${param['search_LIKE_id'] }" type="text" class="form-control"> 
                    </div>                             
                    <div class="form-group form-group-sm"> 
                        <label>发布者</label>
                        <input name="search_LIKE_customer.name" value="${param['search_LIKE_customer.name'] }" type="text" class="form-control"> 
                    </div>
                    <div class="form-group form-group-sm"> 
                        <label>类别</label>
                        <select class="form-control" name="search_EQ_mediaTypes"> 
                            <option value="">请选择</option>
                            <c:forEach items="${mediaTypes}" var="item">
                            <option value="${item.itemCode }" <c:if test="${item.itemCode eq param.search_EQ_mediaTypes }">selected</c:if>>${item.itemName }</option>
                            </c:forEach>                                
                        </select>
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
                           <label>状态</label>
                           <select class="form-control" name="search_EQ_status"> 
                               <option value="">不限</option>
                               <c:forEach items="${reqStatus}" var="reqStatus">
                               <option value="${reqStatus.itemCode }" <c:if test="${reqStatus.itemCode eq param.search_EQ_status }">selected</c:if>>${reqStatus.itemName }</option>
                               </c:forEach>
                           </select>
                       </div>
                    <div class="form-group form-group-sm">                               
                    <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button> 
                    </div>                              
                </form>
            </div>
        </div>            
        <div class="panel-body">
            <table class="table table-bordered table-condensed table-hover">
                <thead>
                    <tr class="thead">
                        <th>编号</th>
                        <th>大类</th>
                        <th>服务类型</th>
                        <th>预算</th>
                        <th>发布时间</th>
                        <th>发布者</th>
                        <th>名称</th>
                        <th>需求概述</th>
                        <th>拟邀</th>
                        <th>应邀</th>
                        <th>抢单</th>
                        <th>稿件</th>
                        <th>生成订单</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${data.content }" var="item">
                    <tr <c:if test="${item.status eq 'REQ_S_DISABLED' }">style="background:#ccc"</c:if>>
                        <td>${item.id }</td>
                        <td><zy:dic value="${item.mediaTypes }"/></td>
                        <td><zy:dic value="${item.serviceTypes }"/></td>
                        <td>${item.budget }</td>
                        <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                        <td>${item.customer.name }</td>
                        <td class="tdw-md">${item.name }</td>
                        <td class="tdw-lg"><tags:summaryDetail data="${item.summary }" /></td>                            
                        <td><zy:dic value="${item.inviteNum }"/></td>
                        <td>${item.passiveNum }</td>
                        <td>${item.activeNum }</td>
                        <td><c:if test="${item.hasArticle }"><a href="${ctx }/member/req/download/article/${item.article}">下载</a></c:if></td>      
                        
                        <td>${fn:length(item.orders) }</td>
                        <td class="text-center">
                            <button data-id="${item.id }" class="btn btn-primary btn-sm item-edit"><i class="fa fa-edit"></i> 修改</button>
                            <!-- 撰稿需求没有屏蔽功能 -->
                            <c:if test="${item.hasArticle }">
	                            <c:choose>
								   <c:when test="${item.status eq 'REQ_S_DISABLED' }">
								   		<button onclick="return setStatus('${item.id}','${item.status}');" class="btn btn-success btn-sm">
			                               <i class="fa fa-edit"></i>开放
										</button>
								   </c:when>
								   <c:otherwise>
										<button onclick="return setStatus('${item.id}','${item.status}');" class="btn btn-danger btn-sm">
											<i class="fa fa-minus-circle"></i> 屏蔽
										</button>
								   </c:otherwise>
							    </c:choose>
						    </c:if>
						    <button data-id="${item.id }" class="btn btn-primary btn-sm preview-btn">查看邀请媒体</button>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
    <div class="modal fade" id="preview" tabindex="-1" role="dialog" data-width="750" data-replace="true">
    </div>  
    <form id="actionForm" action="${ctx}/admin/order/req/reject" method="post">
       <input type="hidden" id="req-id" name="id">
    </form>
<script type="text/javascript">

$(function() {
	
    menu.active('#order-req');
    
//     $(".item-reject").click(function() {
//         $("input", $("#actionForm")).val($(this).attr("data-id"));
//         bootbox.confirm("您确定要屏蔽选中需求吗?", function(result) {
//             if (result) {
//                 $("#actionForm").submit();
//             }
//         });
//     });
    
    $(".item-edit").click(function() {
        window.location.href = "${ctx}/admin/order/req/update/" + $(this).attr("data-id");    	
    });
    
    
    $(".preview-btn").click(function() {
        $('#preview').loadModal('${ctx}/admin/order/req/viewInvite/?ajax', {id: $(this).attr("data-id")}, function() {
            $('#btn-close').click(function() {
                $('#preview').modal('hide');
            });
        });
        return false;
    });  
    
});
function setStatus(id,status) {
	var msg = '';
	if(status=='REQ_S_DISABLED'){
		msg = '开放后需重新审核需求，您确定要开放该需求吗?';
	}else{
		msg = '您确定要屏蔽选中需求吗?';
	}
    bootbox.confirm(msg, function(result) {
        if (result) {
            $.post("${ctx}/admin/order/req/setStatus?ajax",{id: id,status:status},function(result){
                window.location.reload();
            });
        }
    });
    return false;
}

</script>        
</body>
</html>
