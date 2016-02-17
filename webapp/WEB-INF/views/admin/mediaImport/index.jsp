<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>媒体导入</title>
</head>

<body>

<div class="panel panel-default">

  <div class="panel-heading"><!-- 右侧标题 -->
    <ul class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
        <li class="active">
          <zy:dic value="${mediaType }"/>媒体导入
        </li>
    </ul>
  </div><!-- / 右侧标题 -->
  
  <div class="panel-body"><!-- 右侧主体内容 -->
  
	<form id="inputForm" action="${ctx }/admin/mediaImport/import" method="post" class="form-horizontal" enctype="multipart/form-data">
        <zy:token/>
		<fieldset>
            <div class="form-group form-group-sm">
                <label for="caseTitle" class="col-md-3 control-label">媒体列表:</label>
                <div class="col-md-6 has-feedback">
                    <input type="file" class="form-control" id="media" name="media" />
                </div>
            </div>
      
            <div class="form-group form-group-sm">
                <label for="caseTitle" class="col-md-3 control-label">媒体LOGO:</label>
                <div class="col-md-6 has-feedback">
                    <input type="file" class="form-control" id="logo" name="logo" />
                </div>
            </div>
		</fieldset>
		
		<br/>
		
		 <div class="form-group">
			<div class="col-md-offset-3 col-md-2">
			  <button type="submit" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-ok"></span> 导入</button>
			</div>
		</div>

	</form>
  </div>
</div>

<script type="text/javascript">
$(function() {
	menu.active('#media-import');
	
	$('#inputForm').validate({
		rules: {
			media: {
				required: true
			},
		    logo: {
		        required: true
		    }
			
		}
	});
	
});
</script>
</body>
</html>
