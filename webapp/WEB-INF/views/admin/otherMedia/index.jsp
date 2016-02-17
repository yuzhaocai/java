<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>其他媒体管理</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">更多媒体</li>
            </ul>
        </div>
        <div class="panel-body">
            <div class="topwrap">
                <form class="form-inline">                             
                    <div class="form-group form-group-sm"> 
                        <label>媒体名称</label>
                        <input name="search_LIKE_name" value="${param.search_LIKE_name }" type="text" class="form-control"> 
                    </div> 
                    <div class="form-group form-group-sm"> 
                        <label>媒体类别</label>
                        <select class="form-control" name="search_EQ_category"> 
                            <option value="">不限</option>
                            <c:forEach items="${categories}" var="item">
                            <option value="${item.itemCode }" <c:if test="${item.itemCode eq param.search_EQ_category }">selected</c:if>>${item.itemName }</option>
                            </c:forEach>
                        </select>
                    </div>                                
                    <div class="form-group form-group-sm"> 
                        <label>发布时间</label>
                        <div class="input-group">
                        <input type="text" class="form-control" id="search_GTE_modifyTime" name="search_GTE_modifyTime" value="${param.search_GTE_modifyTime }" 
                            onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_modifyTime\')}'})">
                        <div class="input-group-addon">至</div>
                        <input type="text" class="form-control" id="search_LTE_modifyTime" name="search_LTE_modifyTime" value="${param.search_LTE_modifyTime }" 
                            onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_modifyTime\')}'})">
                        </div>
                    </div>
                    <div class="form-group form-group-sm">                                
                    <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
                    </div>
                </form>
            </div>
			<table class="table table-bordered table-condensed table-hover" style="table-layout:fixed;word-wrap:break-word;word-break:break-all">
			    <thead>
			        <tr class="thead">
                        <th>发布时间</th>
                        <th>媒体类别</th>
                        <th>媒体名称</th>
		                <th>操作</th>
			        </tr>
			    </thead>
			    <tbody>
			        <c:forEach items="${data.content }" var="item">
			        <tr>
                        <td><fmt:formatDate value="${item.modifyTime }" pattern="yyyy-MM-dd"/></td>
                        <td><zy:dic value="${item.category }"/></td> 			        
			            <td>${item.name }</td>
		                <td>
							<div class="btn-group" role="group" aria-label="...">
								<a data-media-id="${item.id }" class="btn btn-default btn-sm delete-btn"><i class="fa fa-remove"></i></a>
								<a href="${ctx }/admin/otherMedia/update/${item.id }" class="btn btn-default btn-sm preview-btn"><i class="fa fa-edit"></i></a>
							</div>                
		                </td>
			        </tr>
			        </c:forEach>
			    </tbody>
			</table>
			<tags:pagination page="${data}" />
        </div>
    </div>
    <form id="actionForm" action="${ctx}/admin/media/organization/delete" method="post">
    </form>
<script type="text/javascript">

$(function() {
    menu.active('#other-media');
    
    $(".delete-btn").click(function() {
        var id = $(this).attr("data-media-id")
        bootbox.confirm("您确定要删除该媒体吗?", function(result) {
            if (result) {
                $("#actionForm").attr("action", "${ctx}/admin/otherMedia/delete/" + id);
                $("#actionForm").submit();
            }
        });
        return false;
    });    
});

</script>        
</body>
</html>
