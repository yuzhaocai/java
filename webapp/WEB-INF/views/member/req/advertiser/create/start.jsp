<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>创建需求</title>
</head>

<body>
        
<!-- Main -->
   <div class="main-panel">
       <div class="headline"><h5>创建新需求 - 填写预算</h5><button class="btn-u btn-u-sm btn-u-dark pull-right hide"><i class="fa fa-caret-left"></i> 返回</button></div>
       <!-- pad -->         
       <div class="pad">
           <div class="topwrap">
           	<form id="create-form" class="form-horizontal" action="${ctx}/member/req/advertiser/create/next" method="post">
                 <div class="steps">
	                 <div class="steps-panel clearfix"> 
	                 
	                 	<div class="row">
	                     	<div class="col-sm-8">
	                                 <div class="form-group">
	                                     <label class="control-label col-sm-2"><span class="color-red">*</span>项目预算:</label>
	                                     <div class="col-sm-4 has-feedback">
	                                         <div class="input-group">
	                                             <input type="text" class="form-control" placeholder="请输入..." name="budget">
	                                             <span class="input-group-addon">元</span>
	                                         </div>
	                                     </div>   
	                                     <label class="control-label col-sm-2"><span class="color-red">*</span>拟邀媒体数:</label>
	                                     <div class="col-sm-4 has-feedback">
	                                          <select class="form-control" id="inviteNum" name="inviteNum" >
	                                             <option value="">请选择...</option>
	                                             <zy:options items="${inviteNums }" itemLabel="itemName" itemValue="itemCode" />
	                                         </select>
	                                     </div>
	                                 </div>
	                                 
	                                 <div class="form-group">
	                                     <label class="control-label col-sm-2"><span class="color-red">*</span>媒体类别:</label> 
	                                     <div class="col-sm-10 has-feedback">
	                                     		<select class="select-modal hide" name="mediaTypes" multiple="multiple"
	                                     			data-placeholder="请选择..." data-title="选择媒体类别" >
	                                     			<zy:options items="${mediaTypes }" itemLabel="itemName" itemValue="itemCode" selecteds="${param.mediaTypes }"/>
	                                     		</select>
	                                     </div>
	                                 </div>
	                                 
	                                 <div class="form-group">  
	                                     <label class="control-label col-sm-2"><span class="color-red">*</span>投放地域:</label> 
	                                     <div class="col-sm-10 has-feedback">
	                                     	<select name="regions" id="regions" class="regions">
	                                     	</select>	                                         
	                                     </div>                           
	                                 </div>
	                                 <div class="form-group">  
		                                <label class="control-label col-sm-2"><span class="color-red">*</span>投放行业:</label> 
		                                <div class="col-sm-10 has-feedback">
						                   <select class="hide select-modal" name="industryTypes" id="industryTypes"
						                    	multiple="multiple"
					                  			data-title="选择行业类型">
						                   		<option value="ALL">全部</option>
					                   			<zy:options items="${industryTypes }" itemLabel="itemName" itemValue="itemCode"/>
					                   		</select>
		                                </div>                      
	                                 </div>
	                                 
	                                 <div class="form-group">
	                                     <label class="control-label col-sm-2"><span class="color-red">*</span>服务类别:</label>
	                                     <div class="col-xs-10 has-feedback">
	                                         <label class="radio-inline"><input type="radio" name="hasArticle" value="true" >投放需求(须自备稿件)</label>
	                                         <label class="radio-inline"><input type="radio" name="hasArticle" value="false">定制服务(无稿件)</label>  
	                                     </div>
	                                 </div>
	                             </div><!--//col-sm-8-->
	                         </div><!--//row-->
	                     </div><!--//steps-panel-->                          
	                     <div class="actions clearfix">
	                         <a class="btn-u btn-u-default" href="${ctx}/member/req/advertiser/list" >取消</a>
	                         <button type="submit" class="btn-u btn-u-red" >下一步</button>
	                     </div>
	                 </div>
                </form>
           </div>

       </div><!-- End Tab v2 -->
   </div>
   <!-- End Main -->
 
<script type="text/javascript">

$(function() {
	menu.active('#my-req');
	
	//- 校验表单
	$.validator.setDefaults({ ignore: ":hidden:not('select')" });
	$('#create-form').validate({
		debug: false,
		submitHandler: function(form) {
			//common.disabled('#btn-next');
			form.submit(); 
		}, 
		rules: {
			budget: {
				required:true,
				digits:true,
				min: 10,
				max: 9999999999,
			},
			inviteNum: {
				required:true
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
			hasArticle: {
				required: true
			}
		},
		messages: {
			budget: {
				required:'请输入本次需求的预算',
				digits: '只能输入整数',
				min:'请输入2~10个数字',
				max:'请输入2~10个数字',
			},
			inviteNum: {
				required:'请选择拟邀的媒体数量'
			},
			mediaTypes: {
				required:'请选择媒体类型'
			},
			serviceTypes: {
				required: '请选择服务类别'
			},
			industryTypes: {
				required:'请选择行业类型'
			},
			regions: {
				required:'请选择地区'
			},
			hasArticle: {
				required: '请选择是否有成稿?'
			}
		}
	});
	//- /校验表单
	
	$('.select-modal').selectModal();
	
	$('.regions').regionModal();
});

</script>

</body>
</html>
