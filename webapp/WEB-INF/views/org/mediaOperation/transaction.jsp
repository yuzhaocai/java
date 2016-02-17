<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>主管机构-资金流水记录</title>
    
</head>

<body>
        
	<!-- Main -->
	<div class="main-panel">
	    <div class="headline">
	    	<h4>${mediaName }-资金流水记录</h4>
	      	<a class="btn-u btn-u-sm btn-u-dark pull-right" href="${ctx }/org/media" type="button">
				<i class="fa fa-angle-left"></i>
				返回
			</a>
	    </div>	
                     
        <table class="table table-bordered table-striped" width="100%">
        	<thead>
              <tr>
                <th scope="col" width="20%">交易时间</th>
                <th scope="col" width="20%">交易类型</th>
                <th scope="col" width="20%">金额</th>
                <th scope="col" width="40%">备注</th>
              </tr>
            </thead>
            <tbody>
	            <c:forEach items="${data.content }" var="item" varStatus="stat">
	            	<tr>
	            		<td><fmt:formatDate value="${item.createTime }" pattern="yyyy.MM.dd"/></td>
	            		<td><zy:dic value="${item.type }"/></td>
	            		<td>${item.amount }</td>
	            		<td>${item.remark }</td>
	            	</tr>
	            </c:forEach>
	            <c:if test="${empty data.content }">
	              <tr>
	                <td colspan="4">没有查询到任何记录。</td>
	              </tr>
				</c:if>
            </tbody>
        </table>

         <tags:pagination page="${data }" />
         
	</div>
	<!-- End Main -->

	<script type="text/javascript">
		$(function() {
			menu.active('#org-media');
		});
	</script>
        
</body>
</html>
