<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>${opinionTitle }</title>
</head>
<body>
       <div class="panel panel-default">                    
           <div class="panel-heading">
               <ul class="breadcrumb">
                 <li><span class="glyphicon glyphicon-home"></span> 意见管理</li>
                 <li class="active">${opinionTitle }</li>
             </ul>
           </div>
           <div class="panel-body">
           	   <ul class="nav nav-tabs">
	                  <li class="active"><a>未处理</a></li>
	                  <li class=""><a href="${ctx}/admin/opinion/list/${opinionActive }?search_EQ_status=true&sort=DESC_handleTime" >已处理</a></li>
	           </ul>                
	           <div class="tab-content">
	           
	               <div class="topwrap">
	                   <form class="form-inline">   
	                       <div class="form-group form-group-sm"> 
	                           <label>反馈时间</label>
	                           <div class="input-group">
	                           <input type="text" class="form-control" id="search_GTE_createTime" name="search_GTE_createTime" value="${param.search_GTE_createTime }" 
	                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_createTime\')}'})">
	                           <div class="input-group-addon">至</div>
	                           <input type="text" class="form-control" id="search_LTE_createTime" name="search_LTE_createTime" value="${param.search_LTE_createTime }" 
	                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})">
	                           </div>
	                       </div>                          
	                       <div class="form-group form-group-sm"> 
	                           <label>手机号</label>
	                           <input name="search_LIKE_phone" value="${param['search_LIKE_phone'] }" type="text" class="form-control" maxlength="11"> 
	                       </div>                             
	                       <div class="form-group form-group-sm"> 
	                           <label>反馈者身份</label>
	                           <select class="form-control" name="search_EQ_userType"> 
	                               <option value="">请选择</option>
	                               <option value="广告主" <c:if test="${param.search_EQ_userType eq '广告主'}">selected</c:if>>广告主</option>
	                               <option value="媒体" <c:if test="${param.search_EQ_userType eq '媒体'}">selected</c:if>>媒体</option>
	                               <option value="游客" <c:if test="${param.search_EQ_userType eq '游客'}">selected</c:if>>游客</option>
	                           </select>
	                       </div>
	                       <div class="form-group form-group-sm">                                
	                       <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
	                       </div>
	                   </form>
	               </div>
	
	               <table class="table table-bordered table-condensed table-hover table-photos">
	                   <thead>
	                       <tr class="thead">
	                           <th>意见编号</th>
	                           <th>手机号</th>
	                           <th>反馈者身份</th>
	                           <th>反馈内容</th>
	                           <th>反馈时间</th>
	                           <th>操作</th>
	                           <c:if test="${opinionActive eq 'OPINION_TECHNOLOGY' }">
	                           	<th>附件</th>
	                           </c:if>
	                       </tr>
	                   </thead>
	                   <tbody>
	                       <c:forEach items="${data.content }" var="item">
	                       <tr>
	                           <td>${item.id }</td>
	                           <td>${item.phone }</td>
	                           <td>${item.userType }</td>
	                           <td class="tdw-lg"><tags:summaryDetail data="${item.feedbackContent }" /></td>
	                           <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
	                           <td>
	                           		<button data-id="${item.id }" class="btn btn-danger btn-sm item-invalid"><i class="fa fa-minus-circle"></i> 标记为无效</button>
	                           		<button data-id="${item.id }" class="btn btn-primary btn-sm item-result"><i class="fa fa-edit"></i> 录入结果</button>
							   </td>
							   <c:if test="${opinionActive eq 'OPINION_TECHNOLOGY' }">
	                           		<td><c:if test="${!empty item.feedbackAttachment }"><a href="${ctx }/member/req/download/article/${item.feedbackAttachment}">下载</a></c:if></td>
	                       	   </c:if>
	                       </tr>
	                       </c:forEach>
	                   </tbody>
	               </table>
               </div>
            <tags:pagination page="${data}" />
        </div>
    </div>
    <!-- 处理框 -->
	<div class="modal fade" id="handleModal" tabindex="-1" role="dialog" data-replace="false" data-backdrop="static">
		<form id="handleModal-form" action="${ctx}/admin/opinion/mark" method="post">
		      <input type="hidden" name="id">
		      <input type="hidden" name="feedbackType" value="${opinionActive }">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title">处理结果</h4>
		      </div>
		      <div class="modal-body has-feedback">
		      		<textarea class="form-control" placeholder="请输入..." id="handleResult" name="handleResult"></textarea>
		      </div>
		      <div class="modal-footer">
		      	<button class="btn-u btn-u-red" type="submit" 
		      		id="btn-refuse-ok"
		      		data-loading-text="正在处理...">确定</button>
		      </div>
		</form>
	</div>
<script type="text/javascript">

$(function() {
    menu.active('#'+"${opinionActive}"+'');
    
    $('.item-invalid').click(function(){
    	var id = $(this).data('id');
    	bootbox.confirm("您确定要标记编号：<span style='color:red'>" +id+ "</span>为无效信息吗？",
				function(result) {
					if (result) {window.location.href = "${ctx}/admin/opinion/mark?id="+ id +"&handleResult=无效信息&feedbackType=${opinionActive}";
				}
		})
    })
    
    $('.item-result').click(function(){
    	var id = $(this).data('id');
    	$("#handleModal input[name='id']").val(id);
    	$('#handleModal').showModal();
    })
    
    $('#handleModal-form').validate({
		submitHandler: function(form) {
			form.submit();
		}, 
		rules: {
			handleResult: {
				required: true
			}
		},
		messages: {
			handleResult: {
				required: '请输入处理结果。'
			}
		}
	});
});
</script>        
</body>
</html>
