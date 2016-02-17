<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>

    <title>创建需求</title>
    <script type="text/javascript" src="${ctx}/static/assets/js/regions.json"></script>
</head>

<body>
        
  <!-- Main -->
  <div class="main-panel">
      <div class="headline"><h5>创建新需求</h5></div>         
      <!-- mTab v2 -->          
      <div class="pad pd15">
          <form id="create-form" class="form-horizontal" enctype="multipart/form-data"
          	action="${ctx}/member/req/advertiser/doCreate" method="post">
              <zy:token/>
	          <input type="hidden" name="hasArticle" value="${req.hasArticle }">
	          
              <%@include file="/WEB-INF/views/member/req/advertiser/create-common-fields.jspf" %>
                 
              <div class="form-group form-group-sm">
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>标题:</label>
                  <div class="col-sm-4 has-feedback"> 
                  	<input type="text" class="form-control w300" name="name">
                  </div>

                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>稿费:</label>
                  <div class="col-sm-4 has-feedback"> 
                      <div class="input-group w200">
                          <input type="text" class="form-control" name="articlePrice" >
                          <span class="input-group-addon">元</span>
                      </div> 
                  </div>
              </div>
              <div class="form-group">
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>稿件要求:</label>
                  <div class="col-sm-9 has-feedback">
                  	<textarea class="form-control" rows="3" name="summary"></textarea>            	
                      <p class="help-block"><small>您的需求和素材将会受到隐私保护，不会公开到网站上，采媒在线专业文案策划团队将与您联系！</small></p>
                  </div>
              </div>
              <div class="form-group form-group-sm">
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>稿件素材:</label>
                  <div class="col-sm-4 has-feedback">
                      <input type="file" class="form-control" name="articleMatterFile">                  	
                      <p class="help-block highlight">
                      <small>仅支持图片和word/pdf文档</small>     
                      </p>
                  </div>
              </div>
              <div class="form-group form-group-sm">
                  <label class="control-label col-sm-2 w150">资质证明:</label>
                  <div class="col-sm-4">
                      <input type="file" class="form-control" name="certImgFile">
                      <p class="help-block highlight">
                      <small>根据有关规定，若您本次推广的产品属于个人护理、化妆品类，请上传产品许可证等资质证明！ </small>     
                      </p>
                  </div>
              </div>
              <div class="form-group form-group-sm">
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>最迟响应时间:</label>
                  <div class="col-sm-4 has-feedback">
                      <input type="text" class="form-control Wdate " placeholder="最迟响应时间" id="deadline" name="deadline"
                  		onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd HH:mm', minDate:'%y-%M-%d'})"> 
                  </div>
              </div>
              
              <p class="text-center">
              	<button class="btn-u btn-u-red w200" type="submit"> 完 成 </button>
              </p>

          </form>
      </div><!-- End Pad -->
    </div>
	<!-- End Main -->

<script type="text/javascript">

$(function() {
	menu.active('#my-req');
	
	$.validator.setDefaults({ ignore: ":hidden:not(select)" }); //解决 chosen 插件冲突的问题
	
	$('#create-form').validate({
		debug: false,
		submitHandler: function(form) {
			common.disabled('#btn-save');
			
			form.submit();
		}, 
		rules: {
			budget: {
				required:true,
				digits:true
			},
			inviteNum: {
				required:true,
				digits:true
			},
			mediaTypes: {
				required: true
			},
			serviceTypes: {
				required: true
			},
			industryTypes: {
				required:true
			},
			regions: {
				required:true
			},
			name: {
				required:true,
				minlength:3,
				maxlength:30
			},
			articlePrice: {
				required:true,
				digits:true,
				range:[1, 999999]
			},
			summary: {
				required:true,
				minlength:10,
				maxlength:500
			},
			articleMatterFile: {
				required:true
			},
			deadline: {
				required:true
			}
		}
	});
    
});

</script>

<%@include file="/WEB-INF/views/member/req/advertiser/create-common-script.jspf" %>

</body>
</html>
