<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>
<!doctype html>
<html>
<head>
<title>创建新需求-媒介主</title>
</head>
<body class="c-body">
	<!-- Main -->
	<div class="main-panel">
		<div class="headline">
			<h5>创建媒体</h5>
			<a class="btn-u btn-u-sm btn-u-dark pull-right"
				href="${ctx }/member/media/list"><i class="fa fa-angle-left"></i>返回</a>
		</div>
		<!-- pad -->
		<div class="pad pd15">
			<div>
				<form action="${ctx}/member/media/createStep2" method="get"
					class="form-horizontal form-create" id="chooseMediaType">
					<div>
						<div class="row has-feedback">
								<c:forEach var="mediaCategory" items="${mediaType.dicItems}">
									<div class="col-xs-2 ">
										<label class="radio-inline"><input type="radio"
											name="mediaType" value="${mediaCategory.itemCode}"
											id="${mediaCategory.itemCode}"><strong>${mediaCategory.itemName}</strong>
											</label>
									</div>
								</c:forEach>
						</div>
					</div>
					<p class="text-center">
						<button type="submit" class="btn-u btn-u-red w200"
							id="btn-chooseMediaType">下一步</button>
					</p>
				</form>
			</div>
			<!-- End Pad -->
		</div>
		<!-- End Main -->
	</div>
		<script type="text/javascript" language="javascript">
			$(function() {
				menu.active('#my-media');
				$("input[name=mediaType]:eq(0)").attr('checked','checked'); 
			});

		</script>
		<script type="text/javascript"
			src="${ctx }/static/assets/js/custom.js"></script>
</body>
</html>

