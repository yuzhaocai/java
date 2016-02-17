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
              <li><span class="glyphicon glyphicon-home"></span> 更多媒体管理</li>
              <li class="active">查看订单</li>
          </ul>
        </div>
        <div class="panel-body">
        	<div class="topwrap">
                <form class="form-inline">                             
                    <div class="form-group form-group-sm"> 
                        <label>提交时间</label>
                        <div class="input-group">
                        <input type="text" class="form-control" id="search_GTE_createTime" name="search_GTE_createTime" value="${param.search_GTE_createTime }" 
                            onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_createTime\')}'})">
                        <div class="input-group-addon">至</div>
                        <input type="text" class="form-control" id="search_LTE_createTime" name="search_LTE_createTime" value="${param.search_LTE_createTime }" 
                            onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})">
                        </div>
                    </div>
                    <div class="form-group form-group-sm"> 
                        <label>处理人</label>
                        <input name="search_LIKE_handler.nickname" value="${param['search_LIKE_handler.nickname'] }" type="text" class="form-control"> 
                    </div> 
                    <div class="form-group form-group-sm"> 
                        <label>对接人</label>
                        <input name="search_LIKE_custManager" value="${param['search_LIKE_custManager'] }" type="text" class="form-control"> 
                    </div> 
                    <div class="form-group form-group-sm"> 
                        <label>类别</label>
                        <select class="form-control" name="search_EQ_target.category"> 
                            <option value="">不限</option>
                            <c:forEach items="${otherMediaCategories}" var="item">
                            <option value="${item.itemCode }" <c:if test="${item.itemCode eq param.search_EQ_target.category }">selected</c:if>>${item.itemName }</option>
                            </c:forEach>
                        </select>
                    </div>                                
                    <div class="form-group form-group-sm"> 
                        <label>处理状态</label>
                        <select class="form-control" name="search_EQ_status"> 
                            <option value="">不限</option>
                            <c:forEach items="${inventionStatus}" var="item">
                            <option value="${item.itemCode }" <c:if test="${item.itemCode eq param.search_EQ_status }">selected</c:if>>${item.itemName }</option>
                            </c:forEach>
                        </select>
                    </div>                         
                    <div class="form-group form-group-sm">                                
                    	<button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
                    </div>
                </form>
            </div>
            <table class="table table-bordered table-condensed table-hover">
                <thead>
                    <tr class="thead">
                        <th>提交时间</th>
                        <th>类别</th>
                        <th>所在媒体</th>
                        <th>处理状态</th>
                        <th>处理信息</th>
                        <th>客户电话</th>
                        <th>客户称呼</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${data.content }" var="item">
                    <tr>
                        <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                        <td><zy:dic value="${item.target.category }"/></td>                            
                        <td><c:out value="${item.target.name}" /></td>
                        <td><zy:dic value="${item.status }"/></td>
                        <td><c:if test="${item.status != 'INTENTION_S_PENDING' }"><a data-id="${item.id }" class="view-intention">查看</a></c:if> </td>
                        <td> <c:out value="${item.custPhone }" /></td>
                        <td> <c:out value="${item.custName }" /></td>
                        <td class="text-center">
                        	<c:if test="${item.status eq 'INTENTION_S_PENDING' }">
								<button data-id="${item.id }" class="btn btn-primary btn-sm item-deal"><i class="fa fa-edit"></i> 处理</button>
							</c:if>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
    
    <div class="modal fade" id="intention-view" tabindex="-1" role="dialog" data-width="750" data-replace="true">
    </div>
    
    <div class="modal fade" id="intention-deal" tabindex="-1" role="dialog" data-width="750" data-replace="true">
    </div>
    
<script type="text/javascript">

$(function() {
	
    menu.active('#othermedia-intention');
    
    $(".item-deal").click(function() {
    	$('#intention-deal').loadModal('${ctx}/admin/intention/deal?ajax', {id:$(this).attr("data-id")});
    });
    
    $(".view-intention").click(function() {
        $('#intention-view').loadModal('${ctx}/admin/intention/view?ajax', {id: $(this).attr("data-id")});    
    });
});

</script>        
</body>
</html>
