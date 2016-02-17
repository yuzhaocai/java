<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>提现申请</title>
</head>
<body>
       <div class="panel panel-default">                    
           <div class="panel-heading">
               <ul class="breadcrumb">
                 <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
                 <li class="active">提现申请</li>
             </ul>
           </div>
           <div class="panel-body">
               <div class="topwrap">
                   <form class="form-inline">                             
                       <div class="form-group form-group-sm"> 
                           <label>申请者</label>
                           <input name="search_LIKE_customer.mobPhone" value="${param['search_LIKE_customer.mobPhone'] }" type="text" class="form-control"> 
                       </div>                             
                       <div class="form-group form-group-sm"> 
                           <label>申请状态</label>
                           <select class="form-control" name="search_EQ_status"> 
                               <option value="">不限</option>
                               <option value="WITHDRAW_S_CREATED" <c:if test="${param['search_EQ_status'] eq 'WITHDRAW_S_CREATED' }">selected</c:if>>未处理</option>
                               <option value="WITHDRAW_S_FINISHED" <c:if test="${param['search_EQ_status'] eq 'WITHDRAW_S_FINISHED' }">selected</c:if>>已处理</option>
                           </select>
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
                           <th>申请者</th>
                           <th>金额</th>
                           <th>支付宝</th>
                           <th>申请时间</th>
                           <th>处理人</th>
                           <th>处理时间</th>
                           <th>操作</th>
                       </tr>
                   </thead>
                   <tbody>
                       <c:forEach items="${data.content }" var="item">
                       <tr>
                           <td>${item.customer.mobPhone }</td>
                           <td>${item.amount }</td>
                           <td>${item.platformAccount }</td>
                           <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                           <td>${item.modifyBy.nickname }</td>
                           <td><fmt:formatDate value="${item.modifyTime }" pattern="yyyy-MM-dd"/></td>
                           <td>
                           <c:if test="${item.status eq 'WITHDRAW_S_CREATED' }">
                               <button data-id="${item.id }" class="btn btn-primary btn-sm process-btn">处理</button>
                           </c:if>
                           </td>
                       </tr>
                       </c:forEach>
                   </tbody>
               </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
<form id="audit-form" action="${ctx }/admin/withdraw/audit/${media.id}" method="post">
</form>    
<script type="text/javascript">

$(function() {
    menu.active('#withdraw-man');
    
    $(".process-btn").click(function() {
    	var id = $(this).attr("data-id");
		bootbox.confirm("您确定要处理该提现申请吗?", function(result) {
		    if (result) {
		    	$("#audit-form").attr("action", "${ctx }/admin/withdraw/audit/" + id);
		    	$("#audit-form").submit();
		    }
		});
    });      
});

</script>        
</body>
</html>
