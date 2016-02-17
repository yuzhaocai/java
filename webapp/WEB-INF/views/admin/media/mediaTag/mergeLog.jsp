<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>媒体标签合并日志</title>
</head>
<body>
<div id="maincontent" class="col-md-10">
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">媒体标签合并日志</li>
          </ul>
        </div>
            <table width="100%" border="0" class="table table-hover table-bordered">
                <thead>
                  <tr class="thead">
                    <th scope="col">主词名称</th>
                    <th scope="col">主词被添加次数</th>
                    <th scope="col">副词名称</th>
                    <th scope="col">副词被添加次数</th>
                    <th scope="col">合并时间</th>
                  </tr>
                </thead>
                <tbody>
                <c:forEach items="${data.content }" var="log">
                  <tr>
                    <td>${log.masterName }</td>
                    <td>${log.masterCount }</td>
                    <td>${log.slaveName }</td>
                    <td>${log.slaveCount }</td>
                    <td><fmt:formatDate value="${log.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                  </tr>
                </c:forEach>
                </tbody>
            </table>
        <div class="panel-body">
        <tags:pagination page="${data}" />
        </div>
    </div>
</div>



<script type="text/javascript">

$(function() {
    menu.active('#media-mediaTag');
    
    $(".radio input").change(function() {
        var id = $(this).attr("media-id");
        var level = $(this).attr("media-level");
        $.post("${ctx}/admin/media/media/assignLevel?ajax",{id: id, level: level},function(result){
            window.location.reload();
        });
    });
});

function disable(id) {
    bootbox.confirm("您确定要删除该标签吗?", function(result) {
        if (result) {
		    $.post("${ctx}/admin/media/mediaTag/delete",{id: id},function(result){
		        window.location.reload();
		    });
        }
    });
    return false;
}

function asignLevel(id, level) {
    $.post("${ctx}/admin/media/media/assignLevel?ajax",{id: id, level: level},function(result){
        window.location.reload();
    });
}

</script>        
</body>
</html>
