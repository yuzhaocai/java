<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
<%-- 
	<meta name="decorator" content="plain">
--%>
    <title>修改需求</title>
</head>

<body>
        
  <!-- Main -->
  <div class="main-panel">
      <div class="headline">
      	<h5>修改需求</h5>
     	<button type="button" class="btn-u btn-u-sm btn-u-dark pull-right" 
      		onclick="location.replace(document.referrer);"><i class="fa fa-angle-left"></i> 返回</button>
      </div>         
      <!-- mTab v2 -->          
      <div class="pad pd15">
      
	      <form id="create-form" class="form-horizontal" enctype="multipart/form-data"
	          	action="${ctx}/member/req/advertiser/save" method="post">  
              <zy:token/>
	          <input type="hidden" name="id" value="${req.id }">
	      	  <%-- 显示后台验证错误的标签 --%>
			  <tags:fieldErrors commandName="req" />
			  
			  
		  	  
			  <div class="steps form-horizontal">
				<div class="steps-panel">
		            <div class="col-sm-12 subtt">                            
	                  已选择  <span class="mediaCount highlight"  data-req-id="${req.id }">${reqChooseMediaNum }</span> 家媒体
	                  <a href="#" data-toggle="modal" data-target=".selected-media" data-req-id="${req.id }" class="queryMediaCount btn btn-link">查看</a>
	                </div>
		            
					<div class="row">
						<div class="col-sm-9 col-xs-offset-1">
				           <fieldset>                
				               <div class="form-group">
				                   <label class="control-label col-sm-2"><span class="color-red">*</span>项目预算:</label>
				                   <div class="col-sm-4">
					                   <p class="form-control-static">${req.budget } 元</p>
				                   </div>   
				                   <label class="control-label col-sm-2"><span class="color-red">*</span>拟邀媒体数:</label>
				                   <div class="col-sm-4">
				                        <p class="form-control-static"><zy:dic value="${req.inviteNum }"/></p>
				                   </div>                           
				               </div> 
				               
				               <div class="form-group">
				                   <label class="control-label col-sm-2"><span class="color-red">*</span>媒体类别:</label> 
				                   <div class="col-sm-4"><p class="form-control-static"><zy:dic value="${req.mediaTypes }"/> </p></div>
				                   
				                   <label class="control-label col-sm-2"><span class="color-red">*</span>投放地域:</label> 
				                   <div class="col-sm-4"><p class="form-control-static"><zy:area id="${fn:join(req.regions, ',') }"/></p></div>
				               </div>
				               
				               <div class="form-group">
				                   <label class="control-label col-sm-2"><span class="color-red">*</span>服务类别:</label>
				                   <div class="col-sm-4 has-feedback"><p class="form-control-static"><zy:dic value="${req.serviceTypes }"/></p></div>
				                   
				                   <label class="control-label col-sm-2"><span class="color-red">*</span>投放行业:</label>
				                   <div class="col-sm-4 has-feedback"><p class="form-control-static"><zy:dic value="${req.industryTypes }"/></p></div>
				                </div>                                       
				           </fieldset>
				            
				            <fieldset>
				            <div class="form-group form-group-sm">
				                <label class="control-label col-sm-2"><span class="color-red">*</span>标题:</label>
				                <div class="col-sm-10 has-feedback"> 
				                	<input type="text" class="form-control" name="name" value="${req.name }">
				                </div>
				            </div>
				            <div class="form-group">
				                <label class="control-label col-sm-2"><span class="color-red">*</span>需求概述:</label>
				                <div class="col-sm-10 has-feedback"> 
				                	<textarea class="form-control" rows="5" name="summary">${req.summary }</textarea>
				                </div>                    
				            </div>
				            <div class="form-group form-group-sm">
				                <label class="control-label col-sm-2"><span class="color-red">*</span>稿件:</label>
				                <spring:hasBindErrors name="req">
				                	<c:set var="hasError" value="has-error" />
				                </spring:hasBindErrors>
				                <div class="col-sm-4 has-feedback ${hasError }">
				                	<p class="form-control-static"><input type="file" name="articleFile"></p>
					                <p class="text-warning">
					                	<small>仅支持图片和word/pdf文档</small>
					                  	<c:if test="${!empty req.article }">
					                  	<small> | <a href="${ctx}/member/req/download/article/${req.article}" target="_blank" class="btn-u btn-u-xs btn-u-dark">下载稿件</a></small>
					                  	</c:if>
					                </p>
					                <tags:fieldError commandName="req" field="articleFile"/>
				                </div> 
				                <label class="control-label col-sm-2"><span class="color-red">*</span>允许媒体改稿:</label> 
				                <div class="col-sm-4 has-feedback">
					                <label class="radio-inline"><input type="radio" name="allowChange" value="true"  <c:if test="${req.allowChange }">checked</c:if>  />是</label>
		                  			<label class="radio-inline"><input type="radio" name="allowChange" value="false" <c:if test="${!req.allowChange }">checked</c:if> />否</label>
					                <p class="text-warning"><small>媒体修改的稿件在发布前，会经您最后审核！</small></p>
				                </div>
				            </div>   
				                               
				            <div class="form-group form-group-sm">
				                <label class="control-label col-sm-2"><span class="color-red">*</span>发布时间:</label>
				                <div class="col-sm-4 has-feedback">
				                <input type="text" class="form-control Wdate " placeholder="广告活动的预计开始时间" 
				                	id="startTime" name="startTime" value="${req.startTime }"
				                	onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endTime\')}'})">   
				                </div> 
				                
				                <label class="control-label col-sm-2"><span class="color-red">*</span>至:</label>
				                <div class="col-sm-4 has-feedback"> 
				                <input type="text" class="form-control Wdate col-sm-2" placeholder="广告活动的预计结束时间" 
				                	id="endTime" name="endTime" value="${req.endTime }"
				                	onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startTime\')||$dp.$D(\'deadline\')}'})">
				                </div>                            
				            </div>                  
				            <div class="form-group form-group-sm">
				                <label class="control-label col-sm-2"><span class="color-red">*</span>最迟响应时间:</label>
				                <div class="col-sm-4 has-feedback">
					                <input type="text" class="form-control Wdate " placeholder="接单媒体的最迟响应时间" 
					                	id="deadline" name="deadline" value="${req.deadline }"
					                	onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endTime\')}'})">   
				                </div>  
				                <label class="control-label col-sm-2"><span class="color-red">*</span>是否公开:</label>
				                <div class="col-sm-4 has-feedback"> 
					                <label class="radio-inline"><input type="radio" name="isPublic" value="true"  <c:if test="${req.isPublic }">checked</c:if> />是</label>                        
	                 			    <label class="radio-inline"><input type="radio" name="isPublic" value="false" <c:if test="${!req.isPublic }">checked</c:if> />否</label>
					                <p class="text-warning">
					                    <small>若选择公开，将显示在广告悬赏频道并允许其他非邀媒体进行抢单。</small>
					                </p>
				                </div>
				            </div>
				            <div class="form-group form-group-sm">
				                <label class="control-label col-sm-2">资质证明:</label>
				                <div class="col-sm-10"> 
					                <p class="form-control-static"><input type="file" name="certImgFile"></p>                   
					                <p class="text-warning"><small>根据有关规定，若您本次推广的产品属于个人护理、化妆品类，请上传产品许可证等资质证明！</small></p> 
				                </div>
				            </div>
		            		</fieldset>
		              	</div><!-- /col-sm-9 col-xs-offset-1 -->
	              </div>
	             </div>
	             <p class="text-center">
	             	<button type="submit" class="btn-u btn-u-lg btn-u-red w200" id="btn-save" data-loading-text="请稍候...">保 存</button>
	             </p>
          	</form>
        </div>
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
			//common.disabled('#btn-save');
			$('#btn-save').button('loading');
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
				rangelength:[2, 30]
			},
			summary: {
				required:true,
				rangelength:[2, 1000]
			},
			articleFile: {
				required:false
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
			name: {
				rangelength: '请输入2~30个字符'
			},
			summary: {
				rangelength: '请输入2~1000个字符'
			},
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
<%@include file="/WEB-INF/views/member/req/advertiser/queryMediaCount.jspf" %>

</body>
</html>
