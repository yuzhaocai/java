<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>消息中心</title>
</head>

<body>
        
  <!-- Main -->
  <div class="main-panel record-msg">

	<div class="headline"><h4>消息中心</h4></div>
	
	<c:choose>
		<c:when test="${'0' eq param.search_EQ_readFlag }"><c:set var="active0" value="class='active'" /></c:when>
		<c:when test="${'1' eq param.search_EQ_readFlag }"><c:set var="active1" value="class='active'" /></c:when>
		<c:otherwise><c:set var="active0" value="class='active'" /></c:otherwise>
	</c:choose>
	
	<ul class="nav nav-pills nav-pills-red" role="tablist">
	  <li role="presentation" ${active0 } >
	  	<a href="${ctx}/member/message?search_EQ_readFlag=0">未读消息 <%-- <span class="badge badge-red rounded-x msg-badge">0</span>--%></a>
	  </li>
	  <li role="presentation" ${active1 } >
	  	<a href="${ctx}/member/message?search_EQ_readFlag=1">已读消息</a>
	  </li>
	</ul>

	<div class="pad panel panel-default">
	<c:if test="${!empty active0 }">      
        <div class="topwrap">
        	<button type="button" class="btn-u btn-u-sm btn-u-dark btn-flag-all">全部标记为已读</button>
        	<button type="button" class="btn-u btn-u-sm btn-u-dark btn-flag">标记为已读</button>
        </div>
    </c:if>
     	<table class="table" width="100%" >
             <thead>
               <tr>
                 <th scope="col" class="ck"><input type="checkbox" class="select-all"></th>
                 <th scope="col" class="tp">类别</th>
                 <th scope="col" class="tt">内容</th>
                 <th scope="col" class="dt">时间</th>
               </tr>
             </thead>
             <tbody>
             <c:forEach items="${data.content }" var="item" varStatus="stat">
               <tr>
                 <td class="ck" style="vertical-align:middle"><input name="id" type="checkbox" data-id="${item.id }"></td>
                 <td class="tp" style="vertical-align:middle">${item.type } </td>
                 <td class="tt" style="vertical-align:middle">${item.content } </td>
                 <td class="dt" style="vertical-align:middle"><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/> </td>
               </tr>
             </c:forEach>
             </tbody>
         </table>
         
     </div>
                		
	<tags:pagination page="${data}" />
      
</div>
<!-- End Main -->

<form id="action-form" action="" method="post">
	<input type="hidden" name="ids" />
</form>

<script type="text/javascript">
$(function() {
	menu.active('#message-center');
	
	function setReadFlag(ids) {
		var form = $('#action-form')[0];
		form.action = '${ctx}/member/message/setReadFlag';
		form.ids.value = ids.join(',');
		form.submit();
	}
	
	function getSelected() {
		var ids = [];
		$('input[name="id"]:checked').each(function() {
			ids.push($(this).data('id'));
		});
		return ids;
	}
	
	function getAll() {
		var ids = [];
		$('input[name="id"]').each(function() {
			ids.push($(this).data('id'));
		});
		return ids;
	}
	
	$('input.select-all').click(function() {
		if ( this.checked ) {
			$('input[name="id"]').each(function() {
				this.checked=true;
			});
		} else {
			$('input[name="id"]').each(function() {
				this.checked=false;
			});
		}
	});
	
	$('.btn-flag-all').click(function() {
		var ids = getAll();
		setReadFlag(ids);
	});
	
	$('.btn-flag').click(function() {
		var ids = getSelected();
		setReadFlag(ids);
	});
});
</script>
   
</body>
</html>
