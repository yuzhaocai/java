<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>交易流水</title>
</head>
<body>
       <div class="panel panel-default">                    
           <div class="panel-heading">
               <ul class="breadcrumb">
                 <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
                 <li class="active">交易流水</li>
             </ul>
           </div>
           <div class="panel-body">
               <div class="topwrap">
                   <form class="form-inline">                             
                       <div class="form-group form-group-sm"> 
                           <label>用户名</label>
                           <input name="search_LIKE_customer.mobPhone" value="${param['search_LIKE_customer.mobPhone'] }" type="text" class="form-control"> 
                       </div>                             
                       <div class="form-group form-group-sm"> 
                           <label>申请时间</label>
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

               <table class="table table-bordered table-condensed table-hover table-photos">
                   <thead>
                       <tr class="thead">
                           <th>用户名</th>
                           <th>操作</th>
                           <th>交易金额</th>
                           <th>交易时间</th>
                           <th>订单号</th>
                       </tr>
                   </thead>
                   <tbody>
                       <c:forEach items="${data.content }" var="item">
                       <tr>
                           <td>${item.customer.mobPhone }</td>
                           <td><zy:dic value="${item.type }" /></td>
                           <td>${item.amount }</td>
                           <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                           <td>${item.order.id }${item.offerLine.id }</td>
                       </tr>
                       </c:forEach>
                   </tbody>
               </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
<script type="text/javascript">

$(function() {
    menu.active('#transaction-man');
});

</script>        
</body>
</html>
