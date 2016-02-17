<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>我的退款单</title>
</head>

<body>
        
<!-- Main -->
<div class="main-panel">
    <div class="headline"><h5>退款记录</h5></div>
    <!-- pad -->                
        <div class="pad mtab-v2">
          <div class="topwrap">
		      <form class="form-inline" action="${ctx }/member/refund/list" method="post">
		        <div class="form-group">
                   <input type="text" class="form-control input-sm Wdate " id="search_GTE_createTime" name="search_GTE_createTime" value="${param.search_GTE_createTime }" placeholder="开始日期" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'#F{$dp.$D(\'search_LTE_createTime\')||\'%y-%M-%d\'}'})">
		        </div>
		        <div class="form-group">
                   <input type="text" class="form-control input-sm Wdate " id="search_LTE_createTime" name="search_LTE_createTime" value="${param.search_LTE_createTime }" placeholder="结束日期" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})">
		        </div>

		        <div class="form-group">
		          <button type="submit" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-search"></span> 搜索</button>
		        </div>
	        	
		      </form>
            </div>
            <table class="table table-bordered table-striped" width="100%">
                <thead>
                  <tr>
                    <th scope="col" width="10%">订单号</th>
                    <th scope="col" width="35%">标题</th>
                    <th scope="col" width="10%">订单金额</th>
                    <th scope="col" width="15%">退款时间</th>
                    <th scope="col" width="15%">操作</th>
                  </tr>
                </thead>
                <tbody>
              	<c:forEach items="${data.content }" var="item">
                  <tr>
                    <td>${item.order.id }</td>
                    <td><a href="#" data-id="${item.order.requirement.id }" class="preview-requirement">${item.order.requirement.name }</a></td>
                    <td><span class="color-orange">${item.order.amount }</span></td>
                    <td><fmt:formatDate value="${item.createTime }" pattern="yyyy/MM/dd" /></td>
                    <td class="manage">
                    	<a href="#" class="a-delete" 
                    		data-id="${item.id }" 
                    		data-order-id="${item.order.id }"
                    		data-req-name="${item.order.requirement.name }"
                    		> 删除</a>
                    </td>
                  </tr>
              	</c:forEach>
                  
            <c:if test="${empty data.content }">
				<tr>
					<td colspan="5">
					没有搜索到符合条件的记录！
					</td>
				</tr>
			</c:if>
                </tbody>
            </table>
        </div>
        <!-- End Pad -->
        
        <tags:pagination page="${data}" />
        
</div>


<form id="form1" action="${ctx}/member/refund/delete" method="post">
	<input type="hidden" name="id">
</form>
<!-- 需求详情 -->
<div class="modal fade" id="requirement-view" tabindex="-1" role="dialog" data-width="700" data-replace="true"></div>
<!-- End Main -->

<script type="text/javascript">
$(function() {
	menu.active('#my-refund');
	//需求详情 
	common.showRequirement(".preview-requirement");
	$('.a-delete').click(function() {
		var id = $(this).data('id');
		var orderId = $(this).data('order-id');
		var reqName = $(this).data('req-name');
		var message = "订单号："+ orderId + "，标题：" + reqName;
		common.confirm('您确定要删除下面的记录吗？', message, function() {
			var form = $('#form1')[0];
			form.id.value = id;
			form.submit();
		}, true, 'normal');
	});
});
</script>
   
</body>
</html>
