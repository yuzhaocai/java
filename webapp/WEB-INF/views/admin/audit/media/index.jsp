<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>媒体审核</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">媒体审核</li>
          </ul>
        </div>
        <div class="panel-body">                    
            <div class="subtt">
                <div class="btn-group-sm">
                    <button type="button" class="btn btn-primary" id="pass-btn"><span class="glyphicon glyphicon-ok"></span>通过</button>
                    <button type="button" class="btn btn-danger" id="reject-btn"><span class="glyphicon glyphicon-remove"></span>屏蔽</button>
                </div>
            </div>
        </div>
        <form id="audit-form" method="post">
            <table class="table table-bordered table-condensed table-hover table-photos">
                <thead>
                    <tr class="thead">
                        <th class="text-center"><input type="checkbox" class="chk_group_head" /></th>           
                        <th>头像</th>
                        <th>大类</th>
                        <th>认证类型</th>
                        <th>昵称</th>
                        <th>创建时间</th>
                        <th>粉丝数</th>
                        <th>简介/案例</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${data.content }" var="item">
                    <tr>
                        <td class="text-center"><input type="checkbox" class="chk_group_item" name="ids" value="${item.id }"></td>
                        <td><img src="<zy:fileServerUrl value="${item.showPic }"/>" class="photo"></td>
                        <td><zy:dic value="${item.mediaType }"/></td>
                        <td><zy:dic value="${item.category }"/></td>
                        <td>${item.name }</td>
                        <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                        <td class="tdw-md">
                        	${item.fans } <c:if test="${!empty item.fansNumPic}"><img src="<zy:fileServerUrl value="${item.fansNumPic }"/>" data-id="${item.fansNumPic }" class="photo"></c:if>
                        </td>
                        <td class="tdw-lg"><tags:summaryDetail data="${item.description }" /></td>                            
                        <td><a data-id="${item.id }" class="preview-btn">预览</a></td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </form>
        <tags:pagination page="${data}" />
    </div>
	<div class="modal fade" id="preview" tabindex="-1" role="dialog" data-width="750" data-replace="true">
	</div>
<!-- 媒体粉丝数截图 -->	
<c:forEach items="${data.content }" var="item">	
	<c:if test="${!empty item.fansNumPic}">
		<div class="modal fade" id="${item.fansNumPic }" tabindex="-1" role="dialog"  data-replace="true" data-backdrop="static">
			<a class="fancybox-close" style="top:0px;right:0px;"></a>
			<img src="<zy:fileServerUrl value="${item.fansNumPic }"/>" style="width:100%;height:100%"/>
		</div>
	</c:if>
</c:forEach>	
	
	
<script type="text/javascript">

$(function() {
	
    menu.active('#audit-media');

    $("#pass-btn").click(function() {
        if ($("input[name='ids']:checked").size() > 0) {
            bootbox.confirm("您确定要审核通过选中媒体吗?", function(result) {
                if (result) {
                    $("#audit-form").attr("action", "${ctx}/admin/audit/media/pass");
                    $("#audit-form").submit();
                }
            });
        } else {
            bootbox.alert("请至少选择一个媒体!");
        }
    });    
    
	$("#reject-btn").click(function() {
        if ($("input[name='ids']:checked").size() > 0) {
            bootbox.confirm("您确定要审核屏蔽选中媒体吗?", function(result) {
                if (result) {
                    $("#audit-form").attr("action", "${ctx}/admin/audit/media/reject");
                    $("#audit-form").submit();
                }
            });
        } else {
            bootbox.alert("请至少选择一个媒体!");
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
    
    $(".preview-btn").click(function() {
        $('#preview').loadModal('${ctx}/admin/media/media/view/?ajax', {id: $(this).attr("data-id")}, function() {
            $('#btn-close').click(function() {
                $('#preview').modal('hide');
            });
        });    
    });    
    
    //粉丝数截图
    $('.photo').click(function(){
    	var photoId = $(this).data('id');
    	var $this = $('#'+photoId);
    	$this.modal('show');
    	$('.fancybox-close').click(function(){
    		$this.modal('hide');
    	})
    });
    
});

</script>        
</body>
</html>
