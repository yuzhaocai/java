<%@page import="org.apache.shiro.SecurityUtils"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>

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
				<span style="color: green">${media.name }</span>的案例
			</h5>
		</div>
		<p class="text-right">
			<button class="btn-u btn-u-sm btn-u-dark" id="btn-create-case">
				<i class="fa fa-plus"></i>创建新案例</button>
			<a class="btn-u btn-u-sm btn-u-dark" href="${ctx }/member/media/list"><i class="fa fa-angle-left"></i>返回</a>
		</p>
		<div class="pad">
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th scope="col" width="15%">标题</th>
						<th scope="col" width="15%">亮点</th>
						<th scope="col" width="45%">案例简介</th>
						<th scope="col" width="10%">展示图片</th>
						<th scope="col" width="15%">操作</th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="case" items="${cases}">
						<tr>
							<td>${case.title }</td>
							<td><span class=""><zy:out value="${case.light }" /></span></td>
							<td><zy:out value="${case.content }" /></td>
							<td>
							<c:if test="${fn:length(case.showPic) ge 1}">
									<a href="javascript:void(0)" onclick="view('${case.id}')">查看</a>
								</c:if> <c:if test="${fn:length(case.showPic) lt 1}">暂无图片</c:if>
							</td>
							<td class="manage"><a href="javascript:void(0)"  onclick="editCase('${case.id}')">修改</a>
								| <a href="javascript:void(0)"
								onclick="deleteMediaCase('${case.id}','${media.id }')">删除</a></td>
						</tr>
					</c:forEach>
					<c:if test="${fn:length(cases) lt 1}">
						<tr>
							<td colspan="5">该媒体暂无案例！</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
	<!-- End Main -->

	<!--新增案例模态框 -->
	<div class="modal fade" id="createCaseModal" tabindex="-1"
		role="dialog" data-width="700" data-height="600" aria-labelledby="myModalLabel"
		data-replace="true">

	</div>
	
	<!--修改案例模态框 -->
	<div class="modal fade" id="editCaseModal" tabindex="-1"
		role="dialog" data-width="700" data-height="600" aria-labelledby="myModalLabel"
		data-replace="true">

	</div>

	<!--查看案例模态框 -->
	<div class="modal fade" id="viewCaseModal" tabindex="-1"
		role="dialog" data-width="700" aria-labelledby="myModalLabel"
		data-replace="true">

	</div>


	<script type="text/javascript">
		
		$(function() {
			menu.active('#my-media');
		});
		
		//弹出新增案例
		$('#btn-create-case').click(function() {
			$('#createCaseModal').loadModal('${ctx}/member/media/case/create/${media.id}?ajax');
		});
		
		/**
		弹出修改案例
		 */
		function editCase(caseId) {
			$('#editCaseModal').loadModal(
					'${ctx}/member/media/case/edit?ajax', {
						caseId : caseId
					});
		}

		

		/**
		删除媒体案例
		 */
		function deleteMediaCase(caseId, mediaId) {
			bootbox
					.confirm(
							"您确定删除该媒体案例吗？",
							function(result) {
								if (result) {
									window.location.href = "${ctx}/member/media/case/delete?caseId="
											+ caseId + "&mediaId=" + mediaId;
								}
							});
		}

		/**
			查看图片
		 */
		function view(caseId) {
			$('#viewCaseModal').loadModal(
					'${ctx}/member/media/case/view?ajax', {
						caseId : caseId
					});
			
		}
	</script>
</body>
</html>
