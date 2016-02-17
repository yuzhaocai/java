<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>媒体查询</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">机构管理</li>
          </ul>
        </div>                    
        <div class="panel-body">
            <div class="topwrap">
                <form class="form-inline">                             
                    <div class="form-group form-group-sm"> 
                        <label>手机</label>
                        <input name="search_LIKE_mobPhone" value="${param.search_LIKE_mobPhone }" type="text" class="form-control" maxlength="11"> 
                    </div>                             
                    <div class="form-group form-group-sm"> 
                        <label>名称</label>
                        <input name="search_LIKE_name" value="${param.search_LIKE_name }" type="text" class="form-control"> 
                    </div>
                    <div class="form-group form-group-sm"> 
                        <label>类别</label>
                        <select class="form-control" name="search_EQ_orgType"> 
                            <option value="">请选择</option>
                            <c:forEach items="${organizationTypes}" var="orgType">
                            <option value="${orgType.itemCode }" <c:if test="${orgType.itemCode eq param.search_EQ_orgType }">selected</c:if>>${orgType.itemName }</option>
                            </c:forEach>                                
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
                    <a class="btn btn-primary" href="${ctx }/admin/media/organization/create"><i class="fa fa-plus"></i> 创建机构</a>
                    </div>                              
                </form>
            </div>
            <table class="table table-condensed table-bordered table-hover table-photos" id="table1">
                <thead>
                    <tr class="thead">
                        <th>类别</th>
                        <th>机构名称</th>
                        <th>创建时间</th>
                        <th>下属媒体</th>
                        <th>联系方式</th>
                        <th>简介</th>
                        <th class="text-center">操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${data.content }" var="item">
                    <tr>
                        <td><zy:dic value="${item.orgType }"/></td> 
                        <td>${item.name }</td>
                        <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                        <td><a class="mediaNum" data-id="${item.id }" data-name="${item.name }"><tags:countMediaByOrgId orgId="${item.id}" />个</a></td>
                        <td>${item.mobPhone }</td>
                        <td class="tdw-lg"><tags:summaryDetail data="${item.orgSummary }" /></td>                            
                        <td class="text-center">
                            <button data-org-id="${item.id }" class="btn btn-danger btn-sm org-delete"><i class="fa fa-remove"></i> 删除</button>
                            <button data-org-id="${item.id }" class="btn btn-primary btn-sm org-edit"><i class="fa fa-edit"></i> 修改</button>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="panel-body">
            <tags:pagination page="${data}" />
        </div>
    </div>
	<form id="actionForm" action="${ctx}/admin/media/organization/delete" method="post">
	   <input type="hidden" id="organization-id" name="id">
	</form>
	<div class="modal container fade" id="subMedia" tabindex="-1" role="dialog"  data-backdrop="static"></div>
<script type="text/javascript">

$(function() {
    menu.active('#media-organization');
    
    $(".org-edit").click(function() {
    	window.location.href = "${ctx}/admin/media/organization/update/" + $(this).attr("data-org-id");
    });
    
    $(".org-delete").click(function() {
    	var id = $(this).attr("data-org-id")
        bootbox.confirm("您确定要删除该机构吗?", function(result) {
            if (result) {
                $("#organization-id").val(id);
                $("#actionForm").submit();
            }
        });
        return false;
    });
    
    $('.mediaNum').click(function(){
    	$('#subMedia').loadModal('${ctx}/admin/media/organization/subMedia?ajax', {id:$(this).data('id'),name:$(this).data('name')});
    });
});


function disable(id) {
    bootbox.confirm("您确定要删除该机构吗?", function(result) {
        if (result) {
		    $.post("${ctx}/admin/media/organization/delete/" + id,{},function(result){
		        window.location.reload();
		    });
        }
    });
    return false;
}

</script>        
</body>
</html>
