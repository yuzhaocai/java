<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>添加广告</title>
</head>
<body>
    <div class="panel panel-default">                    
        <div class="panel-heading">
            <ul class="breadcrumb">
              <li><span class="glyphicon glyphicon-home"></span> 广告管理</li>
              <li class="active">添加广告</li>
          </ul>
        </div>
        <div class="panel-body">
        	<div class="col-sm-4">                    
				<form id="advSetting-form" action="${ctx}/admin/advSetting/save"
					method="post" enctype="multipart/form-data">
					<div class="modal-body">
						<div class="form-group form-group-sm">
							<label class=""><span class="color-red">*</span>类型:</label>
							<div class="has-feedback">
							   <select class="form-control" name="type" id="adType"> 
	                                <option value="">请选择</option>
									<option value="像素墙A1">像素墙A1</option>
									<option value="像素墙A2">像素墙A2</option>
									<option value="像素墙A3">像素墙A3</option>
									<option value="像素墙A4">像素墙A4</option>
									<option value="像素墙A5">像素墙A5</option>
									<option value="像素墙B1">像素墙B1</option>
									<option value="像素墙B2">像素墙B2</option>
									<option value="像素墙B3">像素墙B3</option>
									<option value="像素墙B4">像素墙B4</option>
									<option value="像素墙C1">像素墙C1</option>
									<option value="像素墙C2">像素墙C2</option>
									<option value="像素墙C3">像素墙C3</option>
									<option value="像素墙C4">像素墙C4</option>
									<option value="像素墙C5">像素墙C5</option>
									<option value="像素墙D1">像素墙D1</option>
									<option value="像素墙D2">像素墙D2</option>
									<option value="像素墙D3">像素墙D3</option>
									<option value="像素墙D4">像素墙D4</option>
									<option value="首页海报">首页海报</option>
									<option value="优媒推荐">优媒推荐</option>
									<option value="名企客户">名企客户</option>
									<option value="微信频道-推荐媒体">微信频道-推荐媒体</option>
									<option value="微博频道-推荐媒体">微博频道-推荐媒体</option>
									<option value="合作媒体">合作媒体</option>
									<option value="友情链接">友情链接</option>
									<option value="营销经典图文头条">营销经典图文头条</option>
									<option value="小编推荐">小编推荐</option>
	                           </select>
							</div>
						</div>
						<div id="adNormal">
							<div class="form-group form-group-sm">
								<label class=""><span class="color-red">*</span>标题:</label>
								<div class="has-feedback">
									<input type="text" name="title" class="form-control"
										placeholder="广告标题" id="title">
								</div>
							</div>	
							<div class="form-group form-group-sm">
								<label class=""><span class="color-red">*</span>链接:</label>
								<div class="has-feedback">
									<input type="text" name="link" class="form-control"
										placeholder="广告链接，如果没有链接请填#" id="link">
								</div>
							</div>
							<div class="form-group form-group-sm">
								<label class="">图片:</label>
								<div class="row img">
									<div class="col-xs-6 has-feedback">
										<div class="fileinput fileinput-new" data-provides="fileinput">
											<div class="fileinput-new thumbnail"
												style="width: 200px; height: 130px;">
												<img data-src="" alt="..."
													src="${ctx}/static/assets/img/main/img1.jpg">
											</div>
											<div class="fileinput-preview fileinput-exists thumbnail"
												style="width: 200px; height: 130px;"></div>
											<div>
												<span class="btn btn-default btn-file"><span
													class="fileinput-new">选择图片</span><span class="fileinput-exists">更改图片</span><input
													type="file" name="images"></span>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div id="adMediaId">
							<div class="form-group form-group-sm">
								<label class=""><span class="color-red">*</span>媒体ID:</label>
								<div class="has-feedback">
									<input type="text" name="mediaId" class="form-control"
										placeholder="媒体ID" id="mediaId">
								</div>
							</div>
						</div>
						<div id="editorRec">
							<div class="form-group form-group-sm">
								<label class="">概要:</label>
								<div class="has-feedback">
									<textarea class="form-control" name="outLine"></textarea>
								</div>
							</div>
						</div>
						<div class="form-group form-group-sm">
							<label class=""><span class="color-red">*</span>权重:</label>
							<div class="has-feedback">
								<input type="text" name="weight" class="form-control"
									placeholder="权重数值越高，排序越靠前" id="weight" maxlength="2">
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button class="btn-u btn-u-red" type="submit" style="float:left" id="upLoad">提交</button>
					</div>
				</form>
        	</div>
        </div>
    </div>

<script type="text/javascript">

var showMediaId = ['优媒推荐','微信频道-推荐媒体','微博频道-推荐媒体'];

$(function() {
	
    menu.active('#add-adv');
    showColumn();
    
	/**
	表单效验
	 */
	$('#advSetting-form').validate({
		debug : false,
		submitHandler : function(form) {
			common.disabled('#upLoad');
			form.submit();
		},
		rules : {
			title : {
				required :{
					depends: !isShowMediaId
				}
			},
			link : {
				required : {
					depends: !isShowMediaId
				}
			},
			images : {
				required : {
					depends: !isShowMediaId
				}
			},
			mediaId :{
				required : {
					depends: isShowMediaId
				}
			},

			type : {
				required : true
			},
			weight : {
				required : true
			}
		},
		messages : {
			title : {
				required : '请填写标题'
			},
			link : {
				required : '请填写链接'
			},
			images : {
				required : '请添加图片'
			},
			mediaId :{
				required : '请填写媒体ID'
			},
			type : {
				required : '请选择类型'
			},
			weight : {
				required : '请填写权重'
			}
		}
	});
	
	//更换列
	$('#adType').change(function(){
		showColumn();
	})
    
});

//显示媒体ID列
function isShowMediaId(){
	return $.inArray($('#adType  option:selected').val(),showMediaId )!=-1;
}

//根据select选中的值显示列
function showColumn(){
	$('#editorRec').hide();
	if($.inArray($('#adType  option:selected').val(),showMediaId )!=-1){
		$('#adNormal').hide();
		$('#adMediaId').show();
	}else{
		if($('#adType  option:selected').val()=='小编推荐'){
			$('#editorRec').show();
		}
		$('#adMediaId').hide();
		$('#adNormal').show();
	}
	//清空form下input框数据
	$('#advSetting-form input').val('');
}

</script>        
</body>
</html>