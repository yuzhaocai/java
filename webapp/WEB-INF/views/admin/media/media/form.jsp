<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>修改媒体</title>
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
        <li>媒体管理</li>
        <li class="active">
          修改媒体
        </li>
    </ul>
  </div><!-- / 右侧标题 -->
  
  <div class="panel-body"><!-- 右侧主体内容 -->
  
	<form id="inputForm" action="${ctx }/admin/media/media/update" method="post" class="form-horizontal">
    <zy:token/>
		<input type="hidden" name="id" value="${media.id}"/>
		
		<fieldset>
	    <div class="form-group form-group-sm">
         <label for="name" class="col-md-3 control-label"><span class="text-red">* </span>昵称:</label>
         <div class="col-md-6 has-feedback">
           <input type="text" class="form-control" id="name" name="name" value="${media.name}" />
         </div>
      </div>
      
      <div class="form-group form-group-sm">
         <label for="mediaType" class="col-md-3 control-label"><span class="text-red">* </span>认证类型:</label>
         <div class="col-md-6 has-feedback">
           <select class="form-control" name="category"> 
               <c:forEach items="${categories}" var="item">
               <option value="${item.itemCode }" <c:if test="${item.itemCode eq media.category }">selected</c:if>>${item.itemName }</option>
               </c:forEach>
           </select>
         </div>
      </div>
      
      <div class="form-group form-group-sm">
         <label for="description" class="col-md-3 control-label"><span class="text-red">* </span>简介:</label>
         <div class="col-md-6 has-feedback">
           <textarea class="form-control" id="description" name="description" style="height: auto;" rows="3">${media.description }</textarea>
         </div>
      </div>
      
		</fieldset>
		
		<br/>
		
		 <div class="form-group">
			<div class="col-md-offset-3 col-md-2">
			   <a class="btn btn-default btn-block" href="${ctx}/admin/media/media"><span class="glyphicon glyphicon-remove"></span> 返回</a>
			</div>
			<div class="col-md-2">
			  <button type="submit" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-ok"></span> 保存</button>
			</div>
		</div>

	</form>

  </div>
	
</div>

<script type="text/javascript">
$(function() {
	menu.active('#media-media');
	
	$('#inputForm').validate({
		rules: {
			name: {
				<c:if test="${media.mediaType eq 'MEDIA_T_WEIBO'}">
                remote: {
                    url: "${ctx}/admin/media/media/checkWeiboName?ajax",
                    data: {
                        oldName: "${media.name}"
                    }
                },				
				</c:if>
                required: true
            },
            budget: {
                required: true
            },
            summary: {
                required: true
            }
		},
		messages: {
		    name: {
		        remote: "微博名称重复，请重新输入！"
		    }
	    }
	});
});
</script>
</body>
</html>
