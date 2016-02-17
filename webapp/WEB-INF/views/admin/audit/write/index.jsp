<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>撰稿需求</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">撰稿需求</li>
            </ul>
        </div>
        <div class="panel-body">
        	<div class="topwrap">
				<form class="form-inline">
					<div class="form-group form-group-sm">
						<label>处理状态</label> <select class="form-control"
							name="search_EQ_status">
							<option value="">--请选择--</option>
							<option value="1"<c:if test="${param.search_EQ_status eq '1' }">selected</c:if>>待处理</option>
							<option value="2"<c:if test="${param.search_EQ_status eq '2' }">selected</c:if>>已完成</option>
						</select>
					</div>
					<div class="form-group form-group-sm">
						<button type="submit" class="btn btn-primary">
							<i class="fa fa-search"></i> 查询
						</button>
						<button type="button" class="btn btn-primary" id="finish-btn"><span class="glyphicon glyphicon-ok"></span>处理</button>
					</div>
				</form>

			</div>
           
         <form id="audit-form" method="post">
	<table class="table table-bordered table-condensed table-hover" style="table-layout:fixed;word-wrap:break-word;word-break:break-all">
	    <thead>
	        <tr class="thead">
	            <th class="text-center"><input type="checkbox" class="chk_group_head" /></th>           
	            <th>大类</th>
	            <th>服务类型</th>
	            <th>预算</th>
	            <th>发布时间</th>
	            <th>发布者</th>
	            <th>手机号</th>
	            <th>名称</th>
	            <th>稿件要求</th>
	            <th>稿件素材</th>
	            <th>状态</th>
	        </tr>
	    </thead>
	    <tbody>
	        <c:forEach items="${data.content }" var="item">
	        <tr>
	            <td class="text-center">
	            	<c:if test="${!item.isFinished }">
	            		<input type="checkbox" class="chk_group_item" name="ids" value="${item.id }">
	            	</c:if>
            	</td>
	            <td><zy:dic value="${item.mediaTypes }"/></td>
	            <td><zy:dic value="${item.serviceTypes }"/></td>
	            <td>${item.budget }</td>
	            <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
	            <td>${item.customer.name }</td>
	            <td>${item.customer.mobPhone }</td>
	            <td class="tdw-md">${item.name }</td>
	            <td class="tdw-lg"><tags:summaryDetail data="${item.summary }" /></td>
	            <td><c:if test="${not empty item.articleMatter }"><a href="${ctx }/member/req/download/article/${item.articleMatter}">下载</a></c:if></td>
	            <td>
          	         <c:choose>
				         <c:when test="${item.isFinished }" >
				           		 已完成
				         </c:when>
				         <c:otherwise>
				            <zy:dic value="${item.status }"/>
				         </c:otherwise>
			         </c:choose>
	            </td>      
	        </tr>
	        </c:forEach>
	    </tbody>
	</table>
         </form>
         <tags:pagination page="${data}" />
        </div>
    </div>

<script type="text/javascript">

$(function() {
	
    menu.active('#audit-write');
    
	$("#finish-btn").click(function() {
        if ($("input[name='ids']:checked").size() > 0) {
            bootbox.confirm("您确定要处理选中撰稿需求吗?", function(result) {
                if (result) {
                    $("#audit-form").attr("action", "${ctx}/admin/audit/write/finish");
                    $("#audit-form").submit();
                }
            });
        } else {
            bootbox.alert("请至少选择一个撰稿需求!");
        }
	});
	
    $(".chk_group_head").click(function() {
    	var checked = $(this).is(":checked");
   		$(".chk_group_item", $(".chk_group_head").closest("form")).each(function() {
            $(this)[0].checked = checked;
   		});
    });
    
    $(".chk_group_item").change(function() {
        var checked = true;
        $(".chk_group_item", $(".chk_group_head").closest("form")).each(function() {
        	checked = checked && $(this)[0].checked;
        });
        $(".chk_group_head")[0].checked = checked;
    });
});

</script>        
</body>
</html>
