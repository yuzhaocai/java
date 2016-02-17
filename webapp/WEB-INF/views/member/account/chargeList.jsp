<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>会员中心-充值记录</title>
    
</head>

<body>
        
	<!-- Main -->
	<div class="main-panel">
	    
	    <%@include file="/WEB-INF/views/member/account/accountInfo.jsp" %>
	     
	     <!-- Pad -->
         <div class="pad mtab-v2">
             <ul class="nav nav-tabs">
                 <li class=""><a href="${ctx}/member/account/transaction">交易明细</a></li>
                 <li class="active"><a href="#" >充值记录</a></li>
                 <shiro:hasRole name="provider">
	             	<li class=""><a href="${ctx}/member/account/withdrawList" >提现记录</a></li>
                 </shiro:hasRole>
             </ul>
             <div class="tab-content">
                 <div class="tab-pane active" id="recharge-records">
                   	<div class="topwrap">
                        <form class="form-inline" action="${ctx }/member/account/chargeList">
                            <div class="form-group form-group-sm">
                                <input class="form-control input-sm Wdate " id="search_GTE_createTime" name="search_GTE_createTime" value="" placeholder="开始日期" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'#F{$dp.$D(\'search_LTE_createTime\')||\'%y-%M-%d\'}'})" type="text">                             
                            </div>
                            <div class="form-group form-group-sm">
                                <input class="form-control input-sm Wdate " id="search_LTE_createTime" name="search_LTE_createTime" value="" placeholder="结束日期" onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})" type="text">                              
                            </div>
                            <button type="submit" class="btn-u btn-u-sm btn-u-dark"><i class="fa fa-search"></i> 查询</button>
                        </form>
                     </div>
                     
                    <table class="table table-bordered table-striped" width="100%">
                     	<thead>
                           <tr>
                             <th scope="col" width="30%">流水号</th>
                             <th scope="col" width="30%">充值时间</th>
                             <th scope="col" width="20%">充值金额</th>
                             <th scope="col" width="20%">支付平台</th>
                           </tr>
                         </thead>
                         <tbody>
                         <c:forEach items="${data.content }" var="item" varStatus="stat">
                           <tr>
                             <td>${item.id }</td>
                             <td><fmt:formatDate value="${item.createTime }" pattern="yyyy/MM/dd HH:mm:ss"/> </td>
                             <td><span class="color-orange">${item.amount }</span></td>
                             <td><zy:dic value="${item.platform }"/> </td>
                           </tr>
                         </c:forEach>
                         <c:if test="${empty data.content }">
                           <tr>
                             <td colspan="4">没有查询到任何记录。</td>
                           </tr>
						 </c:if>
                         </tbody>
                     </table>

                 </div>
             </div>
         </div>
         <!-- End Pad --> 
         
         <tags:pagination page="${data }" />
         
	</div>
	<!-- End Main -->

	<script type="text/javascript">
	$(function() {
		menu.active('#my-account');
	});
	</script>
        
</body>
</html>
