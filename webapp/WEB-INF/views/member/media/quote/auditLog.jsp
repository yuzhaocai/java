<%@page import="org.apache.shiro.SecurityUtils"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>
<%@ include file="/WEB-INF/jspf/taglib.jsp"%>

<!doctype html>
<html>
<head>
<title>会员中心</title>
</head>
<body>
	<!-- Main -->
	<div class="main-panel">
		<div class="headline">
			<h5>
				<span style="color: green">${media.name }</span>的审核记录
			</h5>
		</div>
		<div class="pad">
			<div class="topwrap">
				<form class="form-inline">
					<div class="form-group form-group-sm">
						   <label>创建时间</label>
                           <div class="input-group">
                           <input type="text" class="form-control" id="search_GTE_createTime" name="search_GTE_createTime" value="${param.search_GTE_createTime }" 
                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'search_LTE_createTime\')}'})">
                           <div class="input-group-addon">至</div>
                           <input type="text" class="form-control" id="search_LTE_createTime" name="search_LTE_createTime" value="${param.search_LTE_createTime }" 
                               onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})">
                           </div>
					</div>
					<button class="btn-u btn-u-sm btn-u-dark">
						<i class="fa fa-search"></i>
						查询
					</button>
					<a class="btn-u btn-u-sm btn-u-dark pull-right" href="${ctx }/member/media/quote?mediaId=${media.id}">
						返回
					</a>
				</form>
			</div>
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>提交时间</th>
						<th>修改方式</th>
						<th>报价类型</th>
						<th>价格</th>
						<th>审核状态</th>
						<th>操作</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="quoteLog" items="${data.content}">
						<tr>
							<td><fmt:formatDate value="${quoteLog.createTime }" pattern="yyyy-MM-dd" /></td>
							<td><zy:dic value="${quoteLog.modifyType }" /></td>
							<td><zy:dic value="${quoteLog.type }" /></td>
							<td>${quoteLog.priceMedia }</td>
							<td><zy:dic value="${quoteLog.status }" /></td>
							<td class="manage">
								<c:if test="${quoteLog.status eq 'AUDIT' }">
									<a href="javascript:void(0)" data-id="${quoteLog.id}" data-media="${media.id}" class="revoke">撤销</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${fn:length(data.content) lt 1}">
						<tr>
							<td colspan="6">该媒体暂无修改记录！</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
		<tags:pagination page="${data }" />
	</div>
	<!-- End Main -->
	<script type="text/javascript">
		$(function(){
			$('.revoke').click(function(){
				var id =$(this).data('id');
				var mediaId = $(this).data('media');
				bootbox.confirm("您确定撤销这条申请吗？",function(result) {
					if (result) {
						window.location.href="${ctx }/member/media/quote/revoke?logId="+id+"&mediaId="+mediaId;
					}
				});
			})
		})
	
	</script>

</body>
</html>
