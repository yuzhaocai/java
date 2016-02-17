<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
<%-- 
	<meta name="decorator" content="plain">
--%>
    <title>创建需求</title>
</head>

<body>
        
  <!-- Main -->
  <div class="main-panel">
      <div class="headline">
      	<h5>创建新需求</h5>
      	<button type="button" class="btn-u btn-u-sm btn-u-dark pull-right" 
      		onclick="location.replace(document.referrer);"><i class="fa fa-angle-left"></i> 返回</button>
      </div>         
      <!-- mTab v2 -->          
      <div class="pad pd15">
          <form id="create-form" class="form-horizontal" enctype="multipart/form-data"
          	action="${ctx}/member/req/advertiser/doCreate" method="post">  
              <zy:token/>
	          <input type="hidden" name="hasArticle" value="${param.hasArticle }">
	          <input type="hidden" name="medias" value="${param.medias }">
	          <input type="hidden" name="prices" value="${param.prices }">
	          <input type="hidden" name="services" value="${param.services }">
	          
              <div class="subtt">                            
                  已选择 <span class="mediaCount highlight">${fn:length( fn:split(param.medias, ",") )}</span> 家媒体
                  <a href="#" data-toggle="modal" data-target=".selected-media" class="btn btn-link">查看</a>
              </div>
              
              <%@include file="/WEB-INF/views/member/req/advertiser/create-common-fields.jspf" %>
              
              <div class="form-group form-group-sm">
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>标题:</label>
                  <div class="col-sm-3 has-feedback"> 
                  <input type="text" class="form-control w300" name="name">
                  </div>
              </div>
              <div class="form-group">
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>需求概述:</label>
                  <div class="col-sm-10 has-feedback"> 
                  <textarea class="form-control" rows="3" name="summary"></textarea>
                  </div>                               
              </div>
              <div class="form-group form-group-sm">
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>稿件:</label>  
                  <div class="col-sm-4 has-feedback">
                  <input type="file" class="form-control w300" name="articleFile"> 
                  <p><small>仅支持图片和word/pdf文档</small></p>  
                  </div> 
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>允许媒体改稿:</label> 
                  <div class="col-sm-4 has-feedback"> 
                  <label class="radio-inline"><input type="radio" name="allowChange" value="true" />是</label>                        
                  <label class="radio-inline"><input type="radio" name="allowChange" value="false" />否</label>
                  <p class="help-block highlight"><small>媒体修改的稿件在发布前，会经您最后审核！</small></p>
                  </div>
              </div>                      
              <div class="form-group form-group-sm">
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>开始时间:</label>
                  <div class="col-sm-4 has-feedback">
                  <input type="text" class="form-control Wdate " placeholder="广告活动的预计开始时间" id="startTime" name="startTime"
                  	onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd HH:mm', minDate:'%y-%M-%d', maxDate:'#F{$dp.$D(\'endTime\')}'})">   
                  </div> 
                  
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>结束时间:</label>
                  <div class="col-sm-4 has-feedback"> 
                  <input type="text" class="form-control Wdate col-sm-2" placeholder="广告活动的预计结束时间" id="endTime" name="endTime"
                  	onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd HH:mm', minDate:'#F{$dp.$D(\'startTime\')}'})">
                  </div>                            
              </div>                  
              <div class="form-group form-group-sm">
                  <label class="control-label col-sm-2 w150">资质证明:</label>
                  <div class="col-sm-7"> 
                  <input type="file" class="form-control w300" name="certImgFile">                        
                      <p class="help-block highlight">
                      <small>根据有关规定，若您本次推广的产品属于个人护理、化妆品类，请上传产品许可证等资质证明！ </small>     
                      </p> 
                  </div>                              
              </div>
                                     
              <div class="form-group form-group-sm">
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>最迟响应时间:</label>
                  <div class="col-sm-4 has-feedback">
                  <input type="text" class="form-control Wdate " placeholder="接单媒体的最迟响应时间" id="deadline" name="deadline"
                  	onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd HH:mm', minDate:'#F{$dp.$D(\'startTime\')}', maxDate:'#F{$dp.$D(\'endTime\', {H:-2})}'})">   
                  </div>  
                  <label class="control-label col-sm-2 w150"><span class="color-red">*</span>是否公开:</label>
                  <div class="col-sm-4 has-feedback"> 
                  <label class="radio-inline"><input type="radio" name="isPublic" value="true" />是</label>                        
                  <label class="radio-inline"><input type="radio" name="isPublic" value="false" />否</label>
                  <p class="help-block highlight">
                      <small>若选择公开，将显示在广告悬赏频道并允许其他非邀媒体进行抢单。</small>
                  </p>
                  </div>
                                              
              </div>
              <p class="text-center"><button type="submit" class="btn-u btn-u-red" id="btn-save">发 布</button></p>
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
			summary: {
				required:true,
				minlength:10,
				maxlength:500
			},
			articleFile: {
				required:true
			},
			startTime: {
				required:true
			},
			endTime: {
				required:true
			},
			deadline: {
				required:true
			},
			allowChange: {
				required:true
			},
			isPublic: {
				required:true
			}
		},
		messages: {
			allowChange: {
				required: '请选择是否允许接单媒体修改稿件'
			},
			isPublic: {
				required: '请选择是否公开此需求'
			}
		}
	});
    
});

</script>

<%@include file="/WEB-INF/views/member/req/advertiser/create-common-script.jspf" %>

</body>
</html>
