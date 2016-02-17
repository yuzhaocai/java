<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	<h4 class="modal-title">用户信息</h4>
</div>
  
<div class="modal-body">
    <table width="100%" border="0" class="table table-bordered">
    <c:if test="${not provider }">
      <tr>
        <td>客户名称</td>
        <td><span class="color-orange">${customer.name }</span></td>
      </tr>
    </c:if>
      <tr>
        <td>联系人</td>
        <td><span class="color-orange">${customer.linkman }</span></td>
      </tr>
      <tr>
        <td>手机</td>
        <td><span class="color-orange">${customer.mobPhone }</span></td>
      </tr>
      <tr>
        <td>邮箱</td>
        <td><span class="color-orange">${customer.email }</span></td>
      </tr>
      <tr>
        <td>QQ</td>
        <td><span class="color-orange">${customer.qq }</span></td>
      </tr>
    </table>
</div>
  
<div class="modal-footer">
    <button class="btn-u btn-u-red" type="button" id="btn-close">关闭</button>
</div>
<style type="text/css">
.navbar-brand {width:500px;line-height: 70px;padding-left:170px;color:#fff;}
.breadcrumb {
  margin: 0;
  padding:0;
  background-color: transparent;
}

/*以下是新增样式*/
.sys-media .pull-left {
    margin-right:10px;
}
.sys-media .media-avatar {
    width:100px;
    height:100px;
}
.sys-media .media-body {
    display:block;
    width:auto;
}
.sys-media .media-body .row{
    padding:5px 0;
}

.border {
    border:1px solid #ddd;
}
.border-dotted {
    border:1px dotted #ddd;
}
.p-xs {
    padding:10px;
}
.m-b-xs {
    margin-bottom:10px;
}
.bg-grey {
    background-color:#eee;
}

</style>
