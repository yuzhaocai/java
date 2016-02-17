<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
<%-- 
	<meta name="decorator" content="plain">
--%>
    <title>查看需求</title>
</head>

<body>
        
  <!-- Main -->
  <div class="main-panel">
      <div class="headline">
      	<h5>查看需求：${req.id }</h5>
     	<button type="button" class="btn-u btn-u-sm btn-u-dark pull-right" 
      		onclick="location.replace(document.referrer);"><i class="fa fa-angle-left"></i> 返回</button>
      </div>         
      <!-- mTab v2 -->          
      <div class="pad pd15">
		  	  
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
				                   <label class="control-label col-sm-2">项目预算:</label>
				                   <div class="col-sm-4">
					                   <p class="form-control-static">${req.budget } 元</p>
				                   </div>   
				                   <label class="control-label col-sm-2">拟邀媒体数:</label>
				                   <div class="col-sm-4">
				                        <p class="form-control-static"><zy:dic value="${req.inviteNum }"/></p>
				                   </div>                           
				               </div> 
				               
				               <div class="form-group">
				                   <label class="control-label col-sm-2">媒体类别:</label> 
				                   <div class="col-sm-4"><p class="form-control-static"><zy:dic value="${req.mediaTypes }"/> </p></div>
				                   
				                   <label class="control-label col-sm-2">投放地域:</label> 
				                   <div class="col-sm-4"><p class="form-control-static"><zy:area id="${req.regions }" /></p></div>
				               </div>
				               
				               <div class="form-group">
				                   <label class="control-label col-sm-2">服务类别:</label>
				                   <div class="col-sm-4 has-feedback"><p class="form-control-static"><zy:dic value="${req.serviceTypes }"/></p></div>
				                   
				                   <label class="control-label col-sm-2">投放行业:</label>
				                   <div class="col-sm-4 has-feedback"><p class="form-control-static"><zy:dic value="${req.industryTypes }"/></p></div>
				                </div>                                       
				           </fieldset>
				            
				            <fieldset>
				            <div class="form-group">
				                <label class="control-label col-sm-2">标题:</label>
				                <div class="col-sm-10 has-feedback"> 
				                	<input type="text" class="form-control" name="name" value="${req.name }" readonly="readonly">
				                </div>
				            </div>
				            <div class="form-group">
				                <label class="control-label col-sm-2">需求概述:</label>
				                <div class="col-sm-10 has-feedback form-control-static"> 
<%-- 				                	<textarea class="form-control" rows="5" name="summary">${req.summary }</textarea> --%>
<%-- 				                	<zy:out value="${req.summary }" escapeXml="false" ellipsis="false" br="true" /> --%>
				                	<p class="pre">${req.summary }</p>
				                </div>                    
				            </div>
				            <div class="form-group">
				                <label class="control-label col-sm-2">稿件:</label>
				                <div class="col-sm-4 has-feedback radio-inline">
				                  	<c:if test="${!empty req.article }">
				                  		<a href="${ctx}/member/req/download/article/${req.article}" target="_blank" class="btn-u btn-u-xs btn-u-dark">下载稿件</a>
				                  	</c:if>
				                </div> 
				                <label class="control-label col-sm-2">允许媒体改稿:</label> 
				                <div class="col-sm-4 has-feedback">
				               		<label class="radio-inline">
					                	<c:choose>
											<c:when test="${req.allowChange }">
												是
											</c:when>
											<c:otherwise>否</c:otherwise>
										</c:choose>
									</label>
<%-- 					                <label class="radio-inline"><input type="radio" name="allowChange" value="true"  <c:if test="${req.allowChange }">checked</c:if>  disabled="disabled"/>是</label> --%>
<%-- 		                  			<label class="radio-inline"><input type="radio" name="allowChange" value="false" <c:if test="${!req.allowChange }">checked</c:if> disabled="disabled"/>否</label> --%>
				                </div>
				            </div>   
				                               
				            <div class="form-group">
				                <label class="control-label col-sm-2">发布时间:</label>
				                <div class="col-sm-4 has-feedback">
				                    <input type="text" class="form-control Wdate " placeholder="广告活动的预计开始时间" id="startTime" name="startTime" 
					                  value='<fmt:formatDate value="${req.startTime }" pattern="yyyy-MM-dd" />' readonly="readonly"> 
				                </div> 
				                
				                <label class="control-label col-sm-2">至:</label>
				                <div class="col-sm-4 has-feedback"> 
					                <input type="text" class="form-control Wdate col-sm-2" placeholder="广告活动的预计结束时间" id="endTime" name="endTime"
	                  					value="<fmt:formatDate value="${req.endTime }" pattern="yyyy-MM-dd" />" readonly="readonly">
				                </div>                            
				            </div>                  
				            <div class="form-group">
				                <label class="control-label col-sm-2">最迟响应时间:</label>
				                <div class="col-sm-4 has-feedback">
					                <input type="text" class="form-control Wdate " placeholder="接单媒体的最迟响应时间" id="deadline" name="deadline"
                						value="<fmt:formatDate value="${req.deadline }" pattern="yyyy-MM-dd" />" readonly="readonly"> 
				                </div>  
				                <label class="control-label col-sm-2">是否公开:</label>
				                <div class="col-sm-4 has-feedback"> 
				                	<label class="radio-inline">
					                	<c:choose>
											<c:when test="${req.isPublic }">
												是
											</c:when>
											<c:otherwise>否</c:otherwise>
										</c:choose>
									</label>
<%-- 						                <label class="radio-inline"><input type="radio" name="isPublic" value="true"  <c:if test="${req.isPublic }">checked</c:if> disabled="disabled"/>是</label>                         --%>
<%-- 		                 			    <label class="radio-inline"><input type="radio" name="isPublic" value="false" <c:if test="${!req.isPublic }">checked</c:if> disabled="disabled"/>否</label> --%>
				                </div>
				            </div>
				            <c:if test="${!empty req.certImg }">
					            <div class="form-group">
					                <label class="control-label col-sm-2">资质证明:</label>
					                <div class="col-sm-10 form-control-static"> 
					                	
					                  		<a href="${ctx}/member/req/download/article/${req.certImg}" target="_blank" class="btn-u btn-u-xs btn-u-dark"> 下载</a>
					                  	
					                </div>
					            </div>
				            </c:if> 
		            		</fieldset>
		              	</div><!-- /col-sm-9 col-xs-offset-1 -->
	              </div>
	             </div>
        </div>
      </div><!-- End Pad -->
    </div>
	<!-- End Main -->

<script type="text/javascript">

$(function() {
	menu.active('#my-req');
});

</script>
<%@include file="/WEB-INF/views/member/req/advertiser/queryMediaCount.jspf" %>

</body>
</html>
