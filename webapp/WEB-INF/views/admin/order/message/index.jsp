<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>留言管理</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">留言管理</li>
          </ul>
        </div>
        <div class="panel-body">
            <div class="topwrap">
                <form class="form-inline">   
                 	<div class="form-group form-group-sm"> 
                        <label>发布者</label>
                        <input name="search_LIKE_speaker" value="${param['search_LIKE_speaker'] }" type="text" class="form-control"> 
                    </div>                          
                    <div class="form-group form-group-sm"> 
                        <label>所属订单号</label>
                        <input name="search_LIKE_order.id" value="${param['search_LIKE_order.id'] }" type="text" class="form-control"> 
                    </div>                             
                    <div class="form-group form-group-sm"> 
                        <label>关键词</label>
                        <input name="search_LIKE_content" value="${param['search_LIKE_content'] }" type="text" class="form-control"> 
                    </div>
                    <div class="form-group form-group-sm"> 
                        <label>留言状态</label>
                        <select class="form-control" name="search_EQ_display"> 
                            <option value="" <c:if test="${empty param.search_EQ_display }">selected</c:if>>请选择</option>
							<option value="true" <c:if test="${'true'  eq param.search_EQ_display }">selected</c:if>>显示</option>
							<option value="false" <c:if test="${'false'  eq param.search_EQ_display }">selected</c:if>>屏蔽</option>
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
                    <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button> 
                    </div>                              
                </form>
            </div>
        </div>            
        <div class="panel-body">
            <table class="table table-bordered table-condensed table-hover">
                <thead>
                    <tr class="thead">
                        <th>所属订单</th>
                        <th>发布时间</th>
                        <th>发布者</th>
                        <th>接收者</th>
                        <th>留言详情</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${data.content }" var="item">
                    <tr <c:if test="${item.display eq false}">style="background:#CCC"</c:if>>
                        <td width="15%">${item.order.id }</td>
                        <td width="15%"><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>                            
                        <td width="15%">${item.speaker }</td>
                        <td width="15%">
                        	<c:choose>
                            <c:when test="${item.order.reqOwner.name eq item.speaker}">
                            	${item.order.media.name}
                            </c:when>
                            <c:when test="${item.order.media.name eq item.speaker}">
                             	${item.order.reqOwner.name}
                            </c:when>
                            </c:choose>
                        </td>
                        <td width="40%"><tags:summaryDetail data="${item.content }" /></td>
                        <td>
                            <c:choose>
                            <c:when test="${item.display eq true}">
                           	 	<button data-id="${item.id }" class="btn btn-danger btn-sm display"><i class="fa fa-minus-circle"></i>屏蔽</button>
                            </c:when>
                            <c:when test="${item.display eq false}">
                             	<button data-id="${item.id }" class="btn btn-primary btn-sm display"><i class="fa fa-edit"></i>显示</button>
                            </c:when>
                            </c:choose>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
    <form id="actionForm" action="${ctx}/admin/order/order/freeze" method="post">
       <input type="hidden" id="req-id" name="id">
    </form>
    
    <div class="modal fade" id="chakan" tabindex="-1" role="dialog" data-width="750" data-replace="true">
    </div>
    
<script type="text/javascript">

$(function() {
    menu.active('#order-orderMessage');
    $('.display').click(function() {
    	var $this = $(this);
    	var $tr = $this.closest('tr');
    	var id =  $this.data('id');
		var display = false;
		if($this.text()=='显示'){
			display = true;
		}
    	var data = {id:id,display:display}
    	$.post('${ctx}/admin/order/orderMessage/display?ajax', data, function(data) {
			data = $.parseJSON(data);
			common.showMessage(data.content);
			if(display){
				$this.removeClass('btn-primary');
				$this.addClass('btn-danger');
				$this.html('<i class="fa fa-minus-circle"></i>屏蔽');
				$tr.attr("style","background:");
			}else{
				$this.removeClass('btn-danger');
				$this.addClass('btn-primary');
				$this.html('<i class="fa fa-edit"></i>显示');
				$tr.attr("style","background:#CCC");
			}
		});
    });
});

</script>        
</body>
</html>
