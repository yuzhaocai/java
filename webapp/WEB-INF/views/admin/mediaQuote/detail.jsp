<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
        <span aria-hidden="true">&times;</span>
    </button>
    <h4 class="modal-title">修改发票状态</h4>
</div>
<div class="modal-body">
<table class="table table-bordered table-condensed table-hover table-photos">
    <thead>
        <tr class="thead">
            <th>报价类型</th>
            <th>结算价</th>
            <th>报价</th>
            <th>含税</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${mediaQuotes }" var="item">
        <tr>
            <td><zy:dic value="${item.type }"/></td>
            <td>${item.priceMedia }</td>
            <td>${item.price }</td>
            <td>${item.tax }</td>
        </tr>
        </c:forEach>
    </tbody>
</table>
</div>
<div class="modal-footer">
        <button type="button" class="btn-u btn-u-default" id="submit-btn">审核通过</button>
        <button type="button" class="btn-u btn-u-default" data-dismiss="modal">关闭</button>
</div>

<script type="text/javascript">
    $('#submit-btn').click(function(){
        $("#actionForm").submit();
    });
</script>