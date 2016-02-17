<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>媒体录入</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
              <li class="active">媒体导入</li>
            </ul>
        </div>
        <div class="panel-body">
            <div class="subtt">
                <div class="btn-group-sm">
                    <a class="btn btn-primary" href="${ctx }/admin/mediaInput/create?mediaType=MEDIA_T_WEIBO"><i class="fa fa-plus"></i> 创建微博媒体</a>
                    <a class="btn btn-primary" href="${ctx }/admin/mediaInput/create?mediaType=MEDIA_T_WEIXIN"><i class="fa fa-plus"></i> 创建微信媒体</a>
                </div>
            </div>
			<table class="table table-bordered table-condensed table-hover" style="table-layout:fixed;word-wrap:break-word;word-break:break-all">
			    <thead>
			        <tr class="thead">
		                <th>媒体名称</th>
		                <th>地区</th>
		                <th>手机号</th>
			            <th>大类</th>
			            <th>服务类型</th>
		                <th>粉丝数</th>
		                <th>操作</th>
			        </tr>
			    </thead>
			    <tbody>
			        <c:forEach items="${data }" var="item">
			        <tr>
			            <td>${item.name }</td>
		                <td><zy:area id="${item.region }"/></td>
			            <td>${item.mobPhone }</td>
		                <td><zy:dic value="${item.mediaType }"/></td>
		                <td><zy:dic value="${item.category }"/></td>
			            <td>${item.fans }</td>
		                <td>
							<div class="btn-group" role="group" aria-label="...">
								<a href="${ctx }/admin/mediaInput/view/${item.id }" class="btn btn-default btn-sm preview-btn"><i class="fa fa-eye"></i></a>
								<a href="${ctx }/admin/mediaInput/update/${item.id }" class="btn btn-default btn-sm preview-btn"><i class="fa fa-edit"></i></a>
							</div>                
		                </td>
			        </tr>
			        </c:forEach>
			    </tbody>
			</table>
        </div>
    </div>

<script type="text/javascript">

$(function() {
    menu.active('#media-input');
});

</script>        
</body>
</html>
