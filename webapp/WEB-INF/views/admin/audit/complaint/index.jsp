<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>举报管理</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">举报管理</li>
          </ul>
        </div>
        <div class="panel-body">
            <table class="table table-bordered table-condensed table-hover table-photos">
                <thead>
                    <tr class="thead">
                        <th>订单号</th>
                        <th>订单名称</th>
                        <th>接单媒体</th>
                        <th>预算</th>
                        <th>广告主</th>
                        <th>需求概述</th>
                        <th>稿件</th>
                        <th>交付情况</th>
                        <th>举报理由</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${data.content }" var="item">
                    <tr>
                        <td>${item.id }</td>
                        <td>${item.order.requirement.name }</td>
                        <td>${item.order.media.name }</td>
                        <td>${item.order.amount }</td>
                        <td>${item.order.reqOwner.name }</td>
                        <td class="tdw-lg"><tags:summaryDetail data="${item.order.requirement.summary }" /></td>
                        <td><c:if test="${item.order.requirement.hasArticle }"><a href="${ctx }/member/req/download/article/${item.order.requirement.article}">下载</a></c:if></td>      
                        <td><c:if test="${not empty item.order.deliverable }"><a data-id="${item.order.id }" class="view-deliverable">查看</a></c:if></td>
                        <td class="tdw-lg"><tags:summaryDetail data="${item.reason }" /></td>                            
                        <td class="text-center">
                            <div class="btn-group btn-group-sm">
                                <button class="btn btn-primary" onclick="deal('${item.id}')"><span class="glyphicon glyphicon-ok"></span>处理</button>
                            </div>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <tags:pagination page="${data}" />
        </div>
    </div>
    <form id="audit-form" method="post">
        <input type="hidden" name="id" id="id" />
        <input type="hidden" name="comment" id="comment" />
    </form>
    
<div class="modal fade" id="chakan" tabindex="-1" role="dialog" data-width="750" data-replace="true">
</div>
    
<script type="text/javascript">

function deal(id) {
    bootbox.prompt("您确定要处理吗?", function(result) {
    	if (result === null) {
        } else if (result == ""){
        	bootbox.alert("请说明处理原因!");
        } else {
            $("#id").val(id);
            $("#comment").val(result);
            $("#audit-form").attr("action", "${ctx}/admin/audit/complaint/deal");
            $("#audit-form").submit();
        }
    });
};

$(function() {
    menu.active('#audit-complaint');

    $(".view-deliverable").click(function() {
        $('#chakan').loadModal('${ctx}/admin/audit/complaint/check?ajax', {id: $(this).attr("data-id")}, function() {
        	$('#btn-yanshou-pass').html("关闭");
            $('#btn-yanshou-pass').click(function() {
            	$('#chakan').modal('hide');
            });
        });    
    });
});

</script>        
</body>
</html>
