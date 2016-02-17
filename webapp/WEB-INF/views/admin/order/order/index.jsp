<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>订单查询</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">订单查询</li>
          </ul>
        </div>
        <div class="panel-body">
            <div class="topwrap">
                <form class="form-inline">                             
                    <div class="form-group form-group-sm"> 
                        <label>订单号</label>
                        <input name="search_LIKE_id" value="${param['search_LIKE_id'] }" type="text" class="form-control"> 
                    </div>                             
                    <div class="form-group form-group-sm"> 
                        <label>发布者</label>
                        <input name="search_LIKE_requirement.customer.name" value="${param['search_LIKE_requirement.customer.name'] }" type="text" class="form-control"> 
                    </div>
                    <div class="form-group form-group-sm"> 
                        <label>接单媒体</label>
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
                        <label>发票状态</label>
                        <select class="form-control" name="search_EQ_status"> 
                            <option value="">请选择</option>
                            <c:forEach items="${orderStatus }" var="item">
                            <option value="${item.itemCode }" <c:if test="${item.itemCode eq param.search_EQ_status }">selected</c:if>>${item.itemName }</option>
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
                        <th>订单号</th>
                        <th>支付方式</th>
                        <th>状态</th>
                        <th>大类</th>
                        <th>服务类型</th>
                        <th>金额</th>
                        <th>生成时间</th>
                        <th>发布者</th>
                        <th>接单媒体</th>
                        <th>名称</th>
                        <th>需求概述</th>
                        <th>订单交付</th>
                        <th>稿件</th>
                        <th>发票状态</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${data.content }" var="item">
                    <tr>
                        <td>${item.id }</td>
                        <td><zy:dic value="${item.paymentType }"/></td>                            
                        <td><zy:dic value="${item.status }"/></td>                            
                        <td><zy:dic value="${item.requirement.mediaTypes }"/></td>
                        <td><zy:dic value="${item.requirement.serviceTypes }"/></td>
                        <td>${item.amount }</td>
                        <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                        <td>${item.requirement.customer.name }</td>
                        <td>${item.media.name }</td>
                        <td class="tdw-md">${item.requirement.name }</td>
                        <td class="tdw-lg"><tags:summaryDetail data="${item.requirement.summary }" /></td>                            
                        <td><c:if test="${not empty item.deliverable }"><a data-id="${item.id }" class="view-deliverable">查看</a></c:if></td>
                        <td><c:if test="${item.requirement.hasArticle }"><a href="${ctx }/member/req/download/article/${item.requirement.article}">下载</a></c:if></td>      
                        <td>
                            <c:choose>
                            <c:when test="${item.status ne 'ORDER_S_FINISHED'}">
                                无
                            </c:when>
                            <c:when test="${item.invoice eq 0}">
                                不开发票
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${item.invoices}" var="item">
                                <zy:dic value="${item.status }"/>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </td>
                        <td class="text-center">
<%--                             <button data-id="${item.id }" class="btn btn-primary btn-sm item-freeze"><i class="fa fa-edit"></i> 冻结</button> --%>
                            <shiro:hasPermission name="adminloan:apply">
                            <c:if test="${(item.status eq 'ORDER_S_PROGRESS') and (item.paymentType eq 'PAYMENT_T_ONLINE') and (empty item.loan) }">
                            <button data-id="${item.id }" class="btn btn-primary btn-sm item-loan"><i class="fa fa-edit"></i> 垫资</button>
                            </c:if>
                            <c:if test="${(item.status eq 'ORDER_S_PROGRESS') and (item.paymentType eq 'PAYMENT_T_ONLINE') and (not empty item.loan) }">
                            <button data-id="${item.id }" class="btn btn-primary btn-sm disabled"><i class="fa fa-edit"></i> 审核中</button>
                            </c:if>
                            </shiro:hasPermission>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
    <form id="actionForm" action="${ctx}/admin/order/order/freeze" method="post">
       <input type="hidden" id="order-id" name="id">
    </form>
    
    <div class="modal fade" id="chakan" tabindex="-1" role="dialog" data-width="750" data-replace="false"></div>
<script type="text/javascript">

$(function() {
	
    menu.active('#order-order');
    
    $(".item-freeze").click(function() {
        $("input", $("#actionForm")).val($(this).attr("data-id"));
        bootbox.confirm("您确定要冻结此订单吗?", function(result) {
            if (result) {
                $("#actionForm").submit();
            }
        });
    });
    
    $(".item-loan").click(function() {
        $("input", $("#actionForm")).val($(this).attr("data-id"));
        $("#actionForm").attr("action", "${ctx}/admin/order/order/loan")
        bootbox.confirm("您确认为此订单申请垫资吗?", function(result) {
            if (result) {
                $("#actionForm").submit();
            }
        });
    });
    
    $(".view-deliverable").click(function() {
        $('#chakan').loadModal('${ctx}/admin/order/order/check?ajax', {id: $(this).attr("data-id")});
    });
});

</script>        
</body>
</html>
