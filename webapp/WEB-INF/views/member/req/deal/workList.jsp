<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

<!doctype html>
<html>
<head>
<title>待处理需求</title>
</head>

<body>
	<!-- Main -->
	<div class="main-panel">
		<div class="headline">
			<h5>待处理需求</h5>
		</div>
		<!-- Pad -->
		<div class="pad">
			<div class="topwrap">
				<form class="form-inline">
					<label class="control-label">创建时间</label>
					<div class="form-group form-group-sm">
						<input class="form-control input-sm Wdate "
							id="search_GTE_createTime" name="search_GTE_createTime"
							value="${param.search_GTE_createTime }" placeholder="开始日期"
							onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'#F{$dp.$D(\'search_LTE_createTime\')||\'%y-%M-%d\'}'})"
							type="text">
					</div>
					<div class="form-group form-group-sm">
						<input class="form-control input-sm Wdate "
							id="search_LTE_createTime" name="search_LTE_createTime"
							value="${param.search_LTE_createTime }" placeholder="结束日期"
							onfocus="WdatePicker({firstDayOfWeek:1,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'search_GTE_createTime\')}'})"
							type="text">
					</div>
					<button class="btn-u btn-u-sm btn-u-dark">
						<i class="fa fa-search"></i> 查询
					</button>
					<a class="btn-u btn-u-sm btn-u-dark pull-right" href=""
						data-toggle="modal" data-target="#jilu">查看应邀记录</a>
				</form>
			</div>
			<table width="100%" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th scope="col" width="17%">需求名称</th>
						<th scope="col" width="8%">预算金额</th>
						<th scope="col" width="13%">服务类别</th>
						<th scope="col" width="20%">受邀媒体</th>
						<th scope="col" width="12%">最迟响应时间</th>
						<th scope="col" width="23%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="req" items="${data.content }">
						<tr>
							<td><a data-id="${req.requirement.id }"
								class="preview-requirement" href="#"><zy:out
										value="${req.requirement.name }" len="15" /></a></td>
							<td><span class="color-orange">${req.price }</span></td>
							<td><zy:dic value="${req.quoteType }"/></td>
							<td><a data-id="${req.media.id }"  class="preview-media" href="#">${req.media.name }</a></td>
							<td><fmt:formatDate value="${req.requirement.deadline }"
									pattern="yyyy/MM/dd" /></td>
							<td class="manage">
								<c:if	test="${req.fbStatus eq 'MEDIA_FB_NULL' }">
									<c:if test="${req.requirement.allowChange eq true}">
										<a href="javascript:void(0)" onclick="changedAndInvitation('${req.id}',this)">改稿应邀</a> |
									</c:if>
									<a href="#" onclick="yingyao('${req.id}','${req.requirement.name }',this)">应邀</a> |
								</c:if> <c:if test="${req.fbStatus eq 'MEDIA_FB_ACCEPT' }">
									<c:choose>
										<c:when test="${req.inviteType eq 'INVITE_T_PASSIVE' }">
											<span class="alert-success">已应邀</span> |
										</c:when>
										<c:when test="${req.inviteType eq 'INVITE_T_ACTIVE' }">
											<span class="alert-success">已抢单</span> |
										</c:when>
									</c:choose>
									
								</c:if> <c:if test="${req.fbStatus eq 'MEDIA_FB_NULL' }">
									<a href="javascript:void(0)" onclick="refuseInvi('${req.id}')">拒绝  </a> |
								</c:if> <c:if test="${req.fbStatus eq 'MEDIA_FB_REFUSE' }">
									<span class="alert-danger">已拒绝</span> |
								</c:if>
								<a data-id="${req.requirement.id }" class="preview-requirement" href="#">详情</a>
								</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- End Pad -->
		<tags:pagination page="${data}" />
	</div>
	<!-- End Main -->


	<!-- 拒绝 -->
	<div class="modal fade jujue" id="refuseModal" tabindex="-1" role="dialog"
		data-width="640" aria-labelledby="myModalLabel" data-replace="true">
		
	</div>

	<!-- 改稿应邀 -->
	<div class="modal fade gaigao" id="changeModal" tabindex="-1" role="dialog" data-width="640"
		aria-labelledby="myModalLabel" data-replace="true">
		
	</div>

	<!-- 应邀记录 -->
	<div class="modal fade" id="jilu" tabindex="-1" role="dialog" data-width="640"
		aria-labelledby="myModalLabel" data-replace="true">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">成功应邀记录</h4>
				</div>
				<div class="modal-body">
					<p class="alert alert-warning">请耐心等待广告商最后的确认！</p>
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th>需求名称</th>
								<th>发布时间</th>
								<th>广告主名称</th>
								<th>应邀时间</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="req" items="${data.content }">
								<c:if test="${req.fbStatus eq 'MEDIA_FB_ACCEPT' }">
									<tr>
										<td>${req.requirement.name }</td>
										<td><fmt:formatDate
												value="${req.requirement.createTime }"
												pattern="yyyy/MM/dd" /></td>
										<td>${req.requirement.customer.name }</td>
										<td><fmt:formatDate value="${req.fbTime }"
												pattern="yyyy/MM/dd" /></td>
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
				</div>
	</div>
	
	<!-- 需求详情 -->
	<div class="modal fade" id="requirement-view" tabindex="-1" role="dialog" data-width="700" data-replace="true">
    </div>
	
	<!-- 媒体详情 -->
	<div class="modal fade" id="media-view" tabindex="-1" role="dialog" data-width="900" data-replace="true">
    </div>
    
<script type="text/javascript">
		
		$(function() {
			menu.active('#deal-req');
		});
		
		common.showRequirement(".preview-requirement");
		common.showMedia(".preview-media");
		
		/**
			应邀需求
		*/
		function yingyao(id,reqName,thiz){
			$this = $(thiz);
			//最迟响应时间
			var deadlineStr=$this.parent().prev().text();
			if(common.testDeadLine(deadlineStr)){
				bootbox.confirm("您确定应邀需求：<span style='color:red'>"+reqName+"</span> 吗？",function(result){
					if(result)
						window.location.href="${ctx}/member/req/deal/accept?reqMediaId="+id;
				});
			}else{
				common.showMessage("应邀失败，已超过最迟响应时间","warn");
			}
		}	
		
		
	/**
	弹出拒绝应邀
	 */
	function refuseInvi(id) {
		$('#refuseModal').loadModal('${ctx}/member/req/deal/preRefuse?ajax', {
			id : id
		});
	}

	/**
	弹出改稿应邀
	 */
	function changedAndInvitation(id,thiz) {
		$this = $(thiz);
		//最迟响应时间
		var deadlineStr=$this.parent().prev().text();
		if(common.testDeadLine(deadlineStr)){
			$('#changeModal').loadModal('${ctx}/member/req/deal/preChange?ajax', {
				id : id
			});
		}else{
			common.showMessage("应邀失败，已超过最迟响应时间","warn");
		}
	}
</script>

</body>
</html>
