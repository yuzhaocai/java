<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>需求审核</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">需求审核</li>
          </ul>
        </div>
        <div class="panel-body">                    
            <div class="subtt">
                <div class="btn-group-sm">
                    <button type="button" class="btn btn-primary" id="pass-btn"><span class="glyphicon glyphicon-ok"></span>通过</button>
                    <button type="button" class="btn btn-danger" id="reject-btn"><span class="glyphicon glyphicon-remove"></span>屏蔽</button>
                </div>
            </div>
         <form id="audit-form" method="post">
             <table class="table table-bordered table-condensed table-hover">
                 <thead>
                     <tr class="thead">
                         <th class="text-center"><input type="checkbox" class="chk_group_head" /></th>           
                         <th>大类</th>
                         <th>服务类型</th>
                         <th>预算</th>
                         <th>发布时间</th>
                         <th>发布者</th>
                         <th>名称</th>
                         <th>需求概述</th>
                         <th>稿件</th>
                         <th></th>
                     </tr>
                 </thead>
                 <tbody>
                     <c:forEach items="${data.content }" var="item">
                     <tr>
                         <td class="text-center"><input type="checkbox" class="chk_group_item" name="ids" value="${item.id }"></td>
                         <td><zy:dic value="${item.mediaTypes }"/></td>
                         <td><zy:dic value="${item.serviceTypes }"/></td>
                         <td>${item.budget }</td>
                         <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                         <td>${item.customer.name }</td>
                         <td class="tdw-md">${item.name }</td>
                         <td class="tdw-lg"><tags:summaryDetail data="${item.summary }" /></td>                            
                         <td><c:if test="${item.hasArticle }"><a href="${ctx }/member/req/download/article/${item.article}">下载</a></c:if></td>
                         <td><button data-id="${item.id }" class="btn btn-primary btn-sm preview-btn">查看邀请媒体</button></td>
                     </tr>
                     </c:forEach>
                 </tbody>
             </table>
         </form>
         <tags:pagination page="${data}" />
        </div>
    </div>
    <div class="modal fade" id="preview" tabindex="-1" role="dialog" data-width="750" data-replace="true">
    </div>  
<script type="text/javascript">

$(function() {
	
    menu.active('#audit-req');
    
	$("#reject-btn").click(function() {
		if ($("input[name='ids']:checked").size() > 0) {
		    bootbox.confirm("您确定要审核屏蔽选中需求吗?", function(result) {
		        if (result) {
		            $("#audit-form").attr("action", "${ctx}/admin/audit/req/reject");
		            $("#audit-form").submit();
		        }
		    });
		} else {
			bootbox.alert("请至少选择一个需求!");
		}
	});
	
    $("#pass-btn").click(function() {
        if ($("input[name='ids']:checked").size() > 0) {
            bootbox.confirm("您确定要审核通过选中需求吗?", function(result) {
                if (result) {
                    $("#audit-form").attr("action", "${ctx}/admin/audit/req/pass");
                    $("#audit-form").submit();
                }
            });
        } else {
            bootbox.alert("请至少选择一个需求!");
        }
    });
    
    $(".preview-btn").click(function() {
        $('#preview').loadModal('${ctx}/admin/order/req/viewInvite/?ajax', {id: $(this).attr("data-id")}, function() {
            $('#btn-close').click(function() {
                $('#preview').modal('hide');
            });
        });
        return false;
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
