<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>报价修改记录</title>
</head>
<body>
       <div class="panel panel-default">                    
           <div class="panel-heading">
               <ul class="breadcrumb">
                 <li><span class="glyphicon glyphicon-home"></span> 系统管理</li>
                 <li class="active">报价修改记录</li>
             </ul>
           </div>
           <div class="panel-body">
               <div class="topwrap">
                   <form class="form-inline">                             
                       <div class="form-group form-group-sm"> 
                           <label>提交时间</label>
                           <div class="input-group">
                           <input type="text" class="form-control" id="search_GTE_createTime" name="search_GTE_createTime" value="${param.search_GTE_createTime }" 
                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_createTime\')}'})">
                           <div class="input-group-addon">至</div>
                           <input type="text" class="form-control" id="search_LTE_createTime" name="search_LTE_createTime" value="${param.search_LTE_createTime }" 
                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})">
                           </div>
                       </div>
                       <div class="form-group form-group-sm">                                
                       <button type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
                       </div>
                   </form>
               </div>

               <table class="table table-bordered table-condensed table-hover table-photos">
                   <thead>
                       <tr class="thead">
                           <th>提交时间</th>
                           <th>修改方式</th>
                           <th>报价类型</th>
                           <th>结算价</th>
                           <th>报价</th>
                           <th>含税</th>
                           <th>提交人</th>
                           <th>审核人</th>
                           <th>审核状态</th>
                       </tr>
                   </thead>
                   <tbody>
                       <c:forEach items="${data.content }" var="item">
                       <tr>
                           <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
                           <td><zy:dic value="${item.modifyType }"/></td>
                           <td><zy:dic value="${item.type }"/></td>

                           
                           <c:choose>
                           <c:when test="${item.modifyType eq 'ADMIN_QUOTE'}">
                           <td>${item.priceMedia }</td>
                           <td><b>${item.price }</b></td>
                           <td>---</td>
                           </c:when>
                           
                           <c:when test="${(item.modifyType eq 'ADMIN_MEDIAQUOTE') or (item.modifyType eq 'MEDIA')}">
                           <td><b>${item.priceMedia }</b></td>
                           <td>${item.price }</td>
                           <td>
                           <c:choose>
                           <c:when test="${item.media.provideInvoice}">---</c:when>
                           <c:otherwise>${item.tax}</c:otherwise>
                           </c:choose>
                           </td>
                           </c:when>
                           
                           <c:otherwise>
                           <td>---</td>
                           <td>---</td>
                           <td>---</td>
                           </c:otherwise>
                           </c:choose>
                           <td>${item.createBy}</td>
                           <td>${item.auditBy}</td>
                           <td><zy:dic value="${item.status }"/></td>
                       </tr>
                       </c:forEach>
                   </tbody>
               </table>
            <tags:pagination page="${data}" />
        </div>
    </div>
    
<script type="text/javascript">

$(function() {
    menu.active('#media-quote-audit');
});

</script>        
</body>
</html>
