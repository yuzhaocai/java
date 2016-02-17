<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>创建新素材</title>
	
	<script type="text/javascript" charset="utf-8" src="${ctx }/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx }/static/ueditor/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="utf-8" src="${ctx }/static/ueditor/ueditor.lczy.js"> </script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="${ctx }/static/ueditor/lang/zh-cn/zh-cn.js"></script>

</head>

<body>
        
  <!-- Main -->
  <div class="main-panel">     

      <div class="headline"><h4>创建新素材</h4></div>
      
	  <form id="article-form" class="form-horizontal text-center" action="${ctx }/member/article/create" method="POST">
	  	<zy:token />
        <div style="text-align:left;">
            
            <div class="form-group">
                <label class="col-md-2 control-label"><span class="color-red">*</span>素材标题:</label>
                <div class="col-md-10 has-feedback">
                     <input type="text" class="form-control" id="title" name="title" placeholder="请输入一个便于查找的名称">
                </div>
            </div>
            
            <div class="form-group">
                <label class="col-md-2 control-label"><span class="color-red">*</span>图文内容:</label>
                <div class="col-md-10 has-feedback">
                	<script id="contentUEditor" type="text/plain"></script>
                    <input type="hidden" id="content" name="content" />
                </div>
            </div>

        </div>
        
        <input type="hidden" id="action" name="action" value="PUBLISH" />
        <div class="form-group">
            <label class="col-md-2 control-label"><span class="color-red"></span></label>
            <div class="col-md-10">
		        <a class="btn btn-default" id="btn-cancel" href="${ctx }/member/article/list"><span class="glyphicon glyphicon-remove" ></span> 取 消</a>
		        <button type="submit" id="btn-draft" class="btn btn-success"><span class="glyphicon glyphicon-edit"></span> 保存草稿</button>
		        <button type="submit" id="btn-publish" class="btn btn-danger"><span class="glyphicon glyphicon-thumbs-up"></span> 直接发布</button>
		        <%-- 
		        <button type="button" id="btn-preview" class="btn btn-primary"><span class="glyphicon glyphicon-eye-open" ></span> 预 览</button>
		        --%>
            </div>
        </div>
    </form>
    
    <br/>
    <br/>
</div>
<!-- End Main -->

<script type="text/javascript">

var contentUEditor;

$(function() {
	menu.active('#my-article');
	
	contentUEditor = UE.getEditor('contentUEditor', {
	 	toolbars: [
	 	        	[	'cleardoc','|',
	 	        		'bold', 'italic', 'underline','|',
	 	        	 	'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', '|',
	 	        		'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
	 	        		'fontsize', '|',
	 	        		'justifyleft', 'justifycenter', 'justifyright', '|',
	 	        		'imageleft', 'imageright', 'imagecenter', '|',
	 	        		'inserttable', 'deletetable', '|',
	 	        		'simpleupload', /*'insertimage',*/'map'
	 	        	]
	 	]
	 	, initialFrameHeight: 320
	 	, autoHeightEnabled: false
	 	, enableAutoSave: false
	});
	
	$('#article-form').validate({
		debug: false,
		submitHandler: function(form) {
			if (contentUEditor.getContentTxt().length == 0) {
				alert('请输入内容！')
				return false;
			}
			
			common.disabled('#btn-draft');
			common.disabled('#btn-publish');		
			
			form.content.value = contentUEditor.getContent();
			
			$(window).unbind('beforeunload');
			
			form.submit(); 
			//alert('submit');
		}, 
		rules: {
			title: {
				required:true,
				minlength:3,
				maxlength:30
			},
			content: {
				required:true,
				minlength:10
			}
		}
	});
	
	$('button:submit').click(function() {
		if (this.id === 'btn-draft') {
			$('#action').val('DRAFT');
		} else if (this.id === 'btn-publish') {
			$('#action').val('PUBLISH');
		}
	});
	
	$(window).on('beforeunload', function(){
		  return '您输入的数据尚未保存！';
	});
	
	$('#btn-cancel').click(function(){
		$(window).unbind('beforeunload');
	});
});
</script>
   
</body>
</html>
