<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>主管机构-订单记录</title>
</head>

<body>
        
   <!-- Main -->
        <div class="main-panel">
            <div class="headline">
            	<h4>${mediaName }-订单记录</h4>
    		    <a class="btn-u btn-u-sm btn-u-dark pull-right" href="${ctx }/org/media" type="button">
					<i class="fa fa-angle-left"></i>
					返回
				</a>
            </div>
         	<div class="row"><!-- 查询条件 -->
				<div class="col-md-12">
				  
			      <form class="form-inline" action="${ctx }/org/mediaOperation/orderRecord?id=${id }&mediaName=${mediaName }" method="post">
			      
			        <div class="form-group">
			          <input type="text" class="form-control input-sm" id="search_LIKE_requirement.name" name="search_LIKE_requirement.name"
			          	value="${param.search_LIKE_requirement.name }" placeholder="需求名称">
			        </div>
			        <div class="form-group">
			          <button type="submit" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-search"></span> 搜索</button>
			        </div>
			        	        	
			      </form>
				
				</div>
        	</div><!-- /查询条件 -->
            <table width="100%" class="table table-bordered table-striped">
            	<thead>
                  <tr>
                    <th scope="col" width="10%">订单号</th>
                    <th scope="col" width="15%">需求名称</th>
                    <th scope="col" width="11%">开始时间</th>
                    <th scope="col" width="10%">订单金额</th>
                  </tr>
                </thead>
                <tbody>
	                <c:forEach items="${data.content }" var="order">
		                <tr>
		                    <td>${order.id }</td>
							<td>${order.requirement.name }</td>
							<td><fmt:formatDate value="${order.requirement.startTime }" pattern="yyyy/MM/dd"/></td>
							<td><span class="color-orange">${order.amount }</span></td>
						</tr>
					</c:forEach>
					<c:if test="${empty data.content }">
						<tr>
							<td colspan="8">
							没有搜索到符合条件的记录！
							</td>
						</tr>
					</c:if>
                </tbody>
            </table>
            <tags:pagination page="${data}" />
        </div>
        <!-- End Main -->

	<script type="text/javascript">
		$(function() {
			menu.active('#org-media');
		});
	</script>
</body>
</html>
