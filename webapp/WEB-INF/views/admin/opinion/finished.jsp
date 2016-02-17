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
	                  <li class=""><a href="${ctx}/admin/opinion/list/${opinionActive }">未处理</a></li>
	                  <li class="active"><a>已处理</a></li>
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
	                           <label>审核人</label>
	                           <input name="search_LIKE_handleBy.nickname" value="${param['search_LIKE_handleBy.nickname'] }" type="text" class="form-control"> 
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
	                           <label>手机号</label>
	                           <input name="search_LIKE_phone" value="${param['search_LIKE_phone'] }" type="text" class="form-control" maxlength="11"> 
	                       </div>
	                       <div class="form-group form-group-sm"> 
	                           <label>信息状态</label>
	                           <select class="form-control" name="search_EQ_handleResult"> 
	                               <option value="" >请选择</option>
	                               <option value="已处理" <c:if test="${param.search_EQ_handleResult eq '已处理'}">selected</c:if>>已处理</option>
	                               <option value="无效信息" <c:if test="${param.search_EQ_handleResult eq '无效信息'}">selected</c:if>>无效信息</option>
	                           </select>
	                       </div>
	                       <!-- 已处理页面条件 -->
	                       <input name="search_EQ_status" value="true" type="hidden" class="form-control"> 
	                       <input name="sort" value="DESC_handleTime" type="hidden" class="form-control"> 
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
	                           <th>处理结果</th>
	                           <th>审核人</th>
	                           <th>处理时间</th>
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
	                           <td class="tdw-lg"><tags:summaryDetail data="${item.handleResult }" /></td>
	                           <td>${item.handleBy.nickname }</td>
	                           <td><fmt:formatDate value="${item.handleTime }" pattern="yyyy-MM-dd"/></td>
	                       </tr>
	                       </c:forEach>
	                   </tbody>
	               </table>
               </div>
            <tags:pagination page="${data}" />
        </div>
    </div>
<script type="text/javascript">

$(function() {
    menu.active('#'+"${opinionActive}"+'');
    
});
</script>        
</body>
</html>
