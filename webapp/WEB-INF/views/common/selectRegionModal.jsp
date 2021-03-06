<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

    <div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    	<h4 class="modal-title">选择地区 </h4>
    </div>
    
    <div class="modal-body">
                
    	<div class="multiple multiple-md">
            <ul class="nav nav-tabs">
	            <c:forEach items="${regions }" var="region" varStatus="stat">
	            	<c:set var="active"></c:set>
	            	<c:if test="${stat.count == 1 }">
	            		<c:set var="active">class="active"</c:set>
	            	</c:if>
	                <li ${active}><a href="#${region.name }" role="tab" data-toggle="tab">${region.name }</a></li>
	            </c:forEach>
            </ul>
            
            <div class="tab-content">
            
            <c:forEach items="${regions }" var="region" varStatus="stat"> <%-- 大区 pane --%>
            	<c:set var="active"></c:set>
            	<c:if test="${stat.index == 0 }">
            		<c:set var="active">active</c:set>
            	</c:if>
                <div class="tab-pane ${active }" id="${region.name }">
                <ul class="list-inline">
                <c:forEach items="${region.provinces }" var="province" varStatus="ps"><%-- 省份 --%>
                	<c:if test="${ps.index > 0 }">
                		<li class="divider"></li>
                	</c:if>
                    <li class="header"><label class="radio-inline">${province.name }：</label></li>
                    <c:forEach items="${province.cities }" var="city"> <%-- 城市 --%>
                    	<li class="city-${province.id }"><label class="radio-inline"><input type="radio" name="region" value="${city.id }" data-label="${city.name }" >${city.name }</label></li>
                    </c:forEach>
                </c:forEach>
                </ul>
                </div>
            </c:forEach>
            </div><!-- //.tab-content --> 
                               
        </div><!-- //.region -->
    
    </div><!-- //.modal-body -->
    <div class="modal-footer"> 
        <button type="button" class="btn-u btn-u-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn-u btn-u-red" id="btn-ok">确定</button>
    </div>
