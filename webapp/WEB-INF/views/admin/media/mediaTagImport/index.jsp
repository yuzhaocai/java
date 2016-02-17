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
              <li class="active">媒体标签导入</li>
          </ul>
        </div>
        <div class="panel-body">
            <form id= "weixinImp-form" action="${ctx }/admin/media/mediaTagImport/import" method="post" enctype="multipart/form-data" class="form-inline create-form">
            	<input type="hidden" name="mediaType" value="MEDIA_T_WEIXIN" />
            <div class="tag-box tag-box-v4">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group"><label>微信导入：</label><input name="file" type="file" class="form-control" accept=".xls, .xlsx"></div>
                        <div class="form-group"><button type="submit" class="btn btn-danger btn-tags-import">导入</button></div>
                    </div>
                </div>
            </div>
            </form>
            
            <form id="weiboImp-form" action="${ctx }/admin/media/mediaTagImport/import" method="post" enctype="multipart/form-data" class="form-inline create-form">
                <input type="hidden" name="mediaType" value="MEDIA_T_WEIBO" />
            <div class="tag-box tag-box-v4">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group"><label>微博导入：</label><input name="file" type="file" class="form-control" accept=".xls, .xlsx"></div>
                        <div class="form-group"><button type="submit" class="btn btn-danger btn-tags-import">导入</button></div>
                    </div>
                </div>
            </div>
            </form>
            
        </div>
    </div>

<script type="text/javascript">

$(function() {
    menu.active('#media-mediaTagImport');
    
    $('#weiboImp-form').validate({
    	debug: false,
		submitHandler: function(form) {
			common.disabled('.btn-tags-import');
			form.submit();
		}, 
		rules: {
			file :{
				required: true
			}
		},
		messages: {
			file :{
				required: '请上传导入文件'
			}
		}
    });
    $('#weixinImp-form').validate({
    	debug: false,
		submitHandler: function(form) {
			common.disabled('.btn-tags-import');
			form.submit();
		}, 
		rules: {
			file :{
				required: true
			}
		},
		messages: {
			file :{
				required: '请上传导入文件'
			}
		}
    });
});
</script>        
</body>
</html>
