<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>垫资审核</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">垫资审核</li>
          </ul>
        </div>
        <div class="panel-body">
            <div class="topwrap">
                <form class="form-inline">                             
                    <div class="form-group form-group-sm"> 
                        <label>订单号</label>
                        <input name="search_LIKE_order.id" value="${param['search_LIKE_order.id'] }" type="text" class="form-control"> 
                    </div>                             
                    <div class="form-group form-group-sm"> 
                        <label>操作员</label>
                        <input name="search_LIKE_createBy.name" value="${param['search_LIKE_createBy.name'] }" type="text" class="form-control"> 
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
                        <th>金额</th>
                        <th>操作员</th>
                        <th>时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${data.content }" var="item">
                    <tr>
                        <td>${item.order.id }</td>
                        <td>${item.order.amount }</td>
                        <td>${item.createBy.name }</td>
                        <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                        <td class="text-center">
                            <button data-id="${item.id }" class="btn btn-primary btn-sm item-approve"><i class="fa fa-check"></i> 批准</button>
                            <button data-id="${item.id }" class="btn btn-primary btn-sm item-reject"><i class="fa fa-times"></i> 拒绝</button>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
    <form id="actionForm" action="${ctx}/admin/order/loan/finish" method="post">
       <input type="hidden" id="data-id" name="id">
    </form>
    
    <div class="modal fade" id="chakan" tabindex="-1" role="dialog" data-width="750" data-replace="true">
    </div>
    
<script type="text/javascript">

$(function() {
	
    menu.active('#order-loan');
    
    $(".item-approve").click(function() {
        $("#data-id", $("#actionForm")).val($(this).attr("data-id"));
        $("#actionForm").attr("action", "${ctx}/admin/order/loan/approve");
        bootbox.confirm("您确认要批准此垫资申请吗?", function(result) {
            if (result) {
                $("#actionForm").submit();
            }
        });
    });

    $(".item-reject").click(function() {
        $("#data-id", $("#actionForm")).val($(this).attr("data-id"));
        $("#actionForm").attr("action", "${ctx}/admin/order/loan/reject");
        bootbox.confirm("您确认要拒绝此垫资申请吗?", function(result) {
            if (result) {
                $("#actionForm").submit();
            }
        });
    });

});

</script>        
</body>
</html>
