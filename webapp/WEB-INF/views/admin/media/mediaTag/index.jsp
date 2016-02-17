<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>媒体标签管理</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">媒体标签管理</li>
          </ul>
        </div>
        <div class="panel-body">
            <form action="${ctx }/admin/media/mediaTag/create" method="post" class="form-inline create-form">
                <input type="hidden" name="hot" value="true" />
            <div class="tag-box tag-box-v4">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group"><label>设置热门标签：</label><input name="name" type="text" class="form-control"></div>
                        <div class="form-group"><button type="submit" class="btn btn-danger">添加</button></div>
                    </div>
                    <div class="col-sm-6">
                        <p class="help-block label-item">
                            <c:forEach items="${tags }" var="tag">
                            <c:if test="${tag.hot }">
                            <span class="label label-blue">${tag.name }<i class="fa fa-remove" media-tag-id="${tag.id }" media-tag-sign="true"></i></span>
                            </c:if>
                            </c:forEach>
                        </p>
                    </div>
                </div>
            </div>
            </form>
            
            <form action="${ctx }/admin/media/mediaTag/create" method="post" class="form-inline create-form">
                <input type="hidden" name="rec" value="true" />
            <div class="tag-box tag-box-v4">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group"><label>设置推荐标签：</label><input name="name" type="text" class="form-control"></div>
                        <div class="form-group"><button type="submit" class="btn btn-danger">添加</button></div>
                    </div>
                    <div class="col-sm-6">
                        <p class="help-block label-item">
                            <c:forEach items="${tags }" var="tag">
                            <c:if test="${tag.rec }">
                            <span class="label label-blue">${tag.name }<i class="fa fa-remove" media-tag-id="${tag.id }" media-tag-sign="false"></i></span>
                            </c:if>
                            </c:forEach>
                        </p>
                    </div>
                </div>
            </div>
            </form>
            
            <form id="mergeForm" action="${ctx }/admin/media/mediaTag/merge" method="post" class="form-inline">
            <!--// tag-box-v4 -->
            <div class="tag-box tag-box-v4">
                <div class="row">
                    <div class="col-sm-6">                                    
                        <label>合并同义标签：</label>
                        <div class="form-group">
                            <label>主词</label>
                            <select id="masterId" name="masterId" class="form-control">
                                <c:forEach items="${tags }" var="tag">
                                <option value="${tag.id }">${tag.name }</option>
                                </c:forEach>
                            </select>
                            <div class="clear"></div>
                            <label class="mt10">副词</label>
                            <select id="slaveId" name="slaveId" class="form-control">
                                <c:forEach items="${tags }" var="tag">
                                <option value="${tag.id }">${tag.name }</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                        <button type="submit" class="btn btn-danger">合并</button>
                        <a href="${ctx}/admin/media/mediaTag/mergeLog" class="btn btn-default">合并记录</a>
                        </div>
                    </div>
                </div>                            
            </div>
            <!--// tag-box-v4 -->                        
            </form>
            <table width="100%" border="0" class="table table-hover">
                <thead>
                  <tr class="thead">
                    <th scope="col">标签被添加次数</th>
                    <th scope="col">标签名称</th>
                  </tr>
                </thead>
                <tbody>
                <c:forEach items="${tags }" var="tag">
                  <tr>
                    <td>${tag.count }次</td>
                    <td>${tag.name }</td>
                  </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>
    </div>

<form id="actionForm" action="" method="post">
   <input type="hidden" id="media-tag-id" name="id">
   <input type="hidden" id="media-tag-sign" name="sign">
</form>

<script type="text/javascript">

$(function() {
    menu.active('#media-mediaTag');
    
    $(".fa-remove").click(function() {
    	var mediaTagId = $(this).attr("media-tag-id");
    	var mediaTagSign = $(this).attr("media-tag-sign");
        bootbox.confirm("您确定要删除该标签吗?", function(result) {
            if (result) {
                $("#media-tag-id").val(mediaTagId);
                $("#media-tag-sign").val(mediaTagSign);
                $("#actionForm").attr("action", "${ctx}/admin/media/mediaTag/delete");
                $("#actionForm").submit();
            }
        });
    });
    
    
    $('.create-form').submit(function() {
        if ($(".form-control", $(this)).val() == "") {
            bootbox.alert("标签名称不能为空!");
            return false;
        }
    });
    
    $('#mergeForm').submit(function() {
    	if ($("#masterId").val() == $("#slaveId").val()) {
    		bootbox.alert("请选择不同的标签进行合并!");
    		return false;
    	}
    });
});

</script>        
</body>
</html>
