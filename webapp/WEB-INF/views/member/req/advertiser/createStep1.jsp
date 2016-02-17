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
     <div class="headline"><h5>创建新需求</h5>
     <button type="button" class="btn-u btn-u-sm btn-u-dark pull-right" 
      		onclick="location.replace('${ctx}/member/req/advertiser');"><i class="fa fa-angle-left"></i> 返回</button>
     </div>
     <!-- pad -->         
     <div class="pad">
         <div class="topwrap">
             <form id="create-form" class="form-horizontal" action="${ctx}/member/req/advertiser/createStep2" method="get">
				
				<%@include file="/WEB-INF/views/member/req/advertiser/create-common-fields.jspf" %>
				
                 <div class="form-group form-group-sm">
                     <label class="control-label col-sm-2 w150"><span class="color-red">*</span>有无成稿:</label>
                     <div class="col-xs-10 has-feedback">
                         <label class="radio-inline"><input type="radio" name="hasArticle" value="true" >有</label>
                         <label class="radio-inline"><input type="radio" name="hasArticle" value="false">无，需协助撰写</label>  
                     </div>
                 </div>
                 <p class="text-center">
                    <button type="submit" class="btn-u btn-u-sm btn-u-red" id="btn-next">下一步</button>
                 </p>
             </form>
         </div>

     </div><!-- End Tab v2 -->
    </div>
	<!-- End Main -->

<script type="text/javascript">

$(function() {
	menu.active('#my-req');	
	
	//- 校验表单
	$.validator.setDefaults({ ignore: ":hidden:not(select)" });
	$('#create-form').validate({
		debug: false,
		submitHandler: function(form) {
			//common.disabled('#btn-next');
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
			hasArticle: {
				required: true
			}
		},
		messages: {
			budget: {
				required:'请输入本次需求的预算',
				digits: '只能输入整数'
			},
			inviteNum: {
				required:'请输入拟邀的媒体数',
				digits:'只能输入整数'
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
});

</script>

<%@include file="/WEB-INF/views/member/req/advertiser/create-common-script.jspf" %>

</body>
</html>
