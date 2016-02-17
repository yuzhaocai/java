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
    	<h5>创建新需求 - 填写需求详情</h5>
    </div>
    <!-- mTab v2 -->          
    <div class="pad pd15">
    	<%-- 显示后台验证错误的标签 --%>
		<tags:fieldErrors commandName="req" />
		
        <form id="create-form" class="form-horizontal" enctype="multipart/form-data"
        	action="${ctx}/member/req/advertiser/create/finish" method="post">
            <zy:token/>
			<input type="hidden" name="hasArticle" value="true">
			<input type="hidden" name="budget" value="${req.budget }" >
			<input type="hidden" name="inviteNum" value="${req.inviteNum }" >
			<c:forEach items="${req.mediaTypes }" var="item" varStatus="stat">
			<input type="hidden" name="mediaTypes[${stat.index }]" value="${item }" >
			</c:forEach>
			<c:forEach items="${req.serviceTypes }" var="item" varStatus="stat">
			<input type="hidden" name="serviceTypes[${stat.index }]" value="${item }" >
			</c:forEach>
			<c:forEach items="${req.regions }" var="item" varStatus="stat">
			<input type="hidden" name="regions[${stat.index }]"    value="${item }" >
			</c:forEach>
			<c:forEach items="${req.industryTypes }" var="item" varStatus="stat">
			<input type="hidden" name="industryTypes[${stat.index }]" value="${item }" >
			</c:forEach>
			<c:forEach items="${req.quotes }" var="item" varStatus="stat">
			<input type="hidden" class="quotes" name="quotes[${stat.index }].media.id"    value="${item.media.id }" >
			<input type="hidden" class="quotes" name="quotes[${stat.index }].type"    value="${item.type }" >
			<input type="hidden" class="quotes" name="quotes[${stat.index }].price"    value="${item.price }" >
			<input type="hidden" class="quotes" name="quotes[${stat.index }].priceMedia"    value="${item.priceMedia }" >
			</c:forEach>

		<div class="steps">
			<div class="steps-panel">
	            <div class="col-sm-12 subtt">                            
	                已选择 <span class="mediaCount highlight" id="inviteMediaCount">${fn:length(req.quotes)}</span> 家媒体
	                <button type="button" class="btn btn-link" id="queryInviteMedia">查看</button>
	            </div>
				<div class="row">
					<div class="col-sm-9 col-xs-offset-1">
			           <fieldset>                
			               <div class="form-group">
			                   <label class="control-label col-sm-2"><span class="color-red">*</span>项目预算:</label>
			                   <div class="col-sm-4">
				                   <p class="form-control-static">${req.budget } 元</p>
				                   <input type="hidden" name="budget" value="${req.budget }" />
			                   </div>   
			                   <label class="control-label col-sm-2"><span class="color-red">*</span>拟邀媒体数:</label>
			                   <div class="col-sm-4">
			                        <p class="form-control-static"><zy:dic value="${req.inviteNum }"/> </p>
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
			                	<input type="text" class="form-control" name="name" value="${param.name }">
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="control-label col-sm-2"><span class="color-red">*</span>需求概述:</label>
			                <div class="col-sm-10 has-feedback"> 
			                	<textarea class="form-control" rows="5" name="summary">${param.summary }</textarea>
			                </div>                    
			            </div>
			            <div class="form-group form-group-sm">
			                <label class="control-label col-sm-2"><span class="color-red">*</span>稿件:</label>
			                <spring:hasBindErrors name="req">
			                	<c:set var="hasError" value="has-error" />
			                </spring:hasBindErrors>
			                <div class="col-sm-4 has-feedback ${hasError }">
			                	<p class="form-control-static"><input type="file" name="articleFile" value="${param.articleFile }"></p>
				                <p class="text-warning"><small>仅支持word/pdf文档/图片</small></p>
				                <tags:fieldError commandName="req" field="articleFile"/>
			                </div> 
			                <label class="control-label col-sm-2"><span class="color-red">*</span>允许媒体改稿:</label> 
			                <div class="col-sm-4 has-feedback">
			                	
				                <label class="radio-inline"><input type="radio" name="allowChange" value="true"  <tags:checked field="${param.allowChange }" value="true" /> />是</label>                        
				                <label class="radio-inline"><input type="radio" name="allowChange" value="false" <tags:checked field="${param.allowChange }" value="false" /> />否</label>
				                <p class="text-warning"><small>媒体修改的稿件在发布前，会经您最后审核！</small></p>
			                </div>
			            </div>                      
			            <div class="form-group form-group-sm">
			                <label class="control-label col-sm-2"><span class="color-red">*</span>发布时间:</label>
			                <div class="col-sm-4 has-feedback">
			                <input type="text" class="form-control Wdate " placeholder="广告活动的预计开始时间" 
			                	id="startTime" name="startTime" value="${param.startTime }"
			                	onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d', maxDate:'#F{$dp.$D(\'endTime\')}'})">   
			                </div> 
			                
			                <label class="control-label col-sm-2"><span class="color-red">*</span>至:</label>
			                <div class="col-sm-4 has-feedback"> 
			                <input type="text" class="form-control Wdate col-sm-2" placeholder="广告活动的预计结束时间" 
			                	id="endTime" name="endTime" value="${param.endTime }"
			                	onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startTime\')||$dp.$D(\'deadline\')||\'%y-%M-%d\'}'})">
			                </div>                            
			            </div>                  
			                                   
			            <div class="form-group form-group-sm">
			                <label class="control-label col-sm-2"><span class="color-red">*</span>最迟响应时间:</label>
			                <div class="col-sm-4 has-feedback">
				                <input type="text" class="form-control Wdate " placeholder="接单媒体的最迟响应时间" 
				                	id="deadline" name="deadline" value="${param.deadline }"
				                	onfocus="WdatePicker({firstDayOfWeek:1, dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d', maxDate:'#F{$dp.$D(\'endTime\')}'})">   
			                </div>  
			                <label class="control-label col-sm-2"><span class="color-red">*</span>是否公开:</label>
			                <div class="col-sm-4 has-feedback"> 
				                <label class="radio-inline"><input type="radio" name="isPublic" value="true"  <tags:checked field="${param.isPublic }" value="true" /> />是</label>                        
				                <label class="radio-inline"><input type="radio" name="isPublic" value="false" <tags:checked field="${param.isPublic }" value="false" /> />否</label>
				                <p class="text-warning">
				                    <small>若选择公开，将显示在广告悬赏频道并允许其他非邀媒体进行抢单。</small>
				                </p>
			                </div>
			            </div>
			            <div class="form-group form-group-sm">
			                <label class="control-label col-sm-2">资质证明:</label>
			                <div class="col-sm-10"> 
				                <p class="form-control-static"><input type="file" name="certImgFile" value="${param.certImgFile }"></p>                   
				                <p class="text-warning"><small>根据有关规定，若您本次推广的产品属于个人护理、化妆品类，请上传产品许可证等资质证明！(仅支持jpg/png/bmp格式)</small></p> 
			                </div>
			            </div>
	            
	            		</fieldset>
	              	</div><!-- /col-sm-9 col-xs-offset-1 -->
              </div>
             </div>
            </div>
            <p class="text-center"><button type="submit" class="btn-u btn-u-lg btn-u-red w200" id="btn-save">发 布</button></p>
        </form>
    </div><!-- End Pad -->
  </div>
<!-- End Main -->
<div class="inviteMediaForm modal container fade" tabindex="-1" role="dialog"  data-backdrop="static">
	<%-- 已邀请接单媒体 --%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title">已邀请接单媒体</h4>
</div>
<div class="modal-body">
	<table width="100%" border="0" class="table table-bordered">
	  <tr>
	    <th scope="col">媒体名称</th>
	    <th scope="col">大类</th>
	    <th scope="col">认证类型</th>
	    <th scope="col">粉丝数</th>
	    <th scope="col">服务类型</th>
	    <th scope="col">价格</th>
	  </tr>
	  <c:forEach items="${req.quotes }" var="item">
		  <tr>
	  	    <td>${item.media.name }</td>
	    	<td><zy:dic value="${item.media.mediaType }"/></td>
	    	<td><zy:dic value="${item.media.category }"/></td>
	    	<td>${item.media.fans }</td>
	    	<td><zy:dic value="${item.type }"/></td>
	    	<td><zy:dic value="${item.price }"/></td>
		  </tr>
	  </c:forEach>
	</table>
</div>
</div>
<script type="text/javascript">

//-弹出已邀约媒体对话框
$('#queryInviteMedia').click(function() {
	if($('#inviteMediaCount').text()!=0){
		$('.inviteMediaForm').showModal();
	}else{
		common.alert("此需求尚未选择接单媒体!");
	}
});

$(function() {
	menu.active('#my-req');
	
	$('#create-form').validate({
		debug: false,
		submitHandler: function(form) {
			common.disabled('#btn-save');
			form.submit();
		}, 
		rules: {
			name: {
				required:true,
				rangelength:[2, 30]
			},
			summary: {
				required:true,
				rangelength:[2, 1000]
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

</body>
</html>
