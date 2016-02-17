<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>
<!doctype html>
<html>
<head>
<title>修改需求-媒介主</title>
<style>
#mediaLabels .checkbox-inline {
	margin-left: 10px;
}
.multiple.fit-width.multiple-md .list-inline li{
	width: 30%;
	margin-left: 15px;
}
</style>
</head>

<body class="c-body">
	<!--=== Header v1 ===-->
	<!--=== End Header v1 ===-->
	<!--=== Content ===-->
	<!-- Main -->
	<div class="main-panel">
		<div class="headline">
			<h5>修改媒体</h5>
			<a class="btn-u btn-u-sm btn-u-dark pull-right"
				href="${ctx }/member/media/list"><i class="fa fa-angle-left"></i>返回</a>
		</div>
		<!-- pad -->
		<div class="pad pd15">
			<form id="form_editMedia" class="form-horizontal form-create"
				method="post" enctype="multipart/form-data"
				action="${ctx}/member/media/weibo/save">
				<zy:token />
				<input type="hidden" value="${media.id}" name="id">
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2"><span
						class="color-red">*</span>媒体类别:</label>
					<div class="col-xs-4">
						<h4 style='color: green'>
							<zy:dic value="${media.mediaType }" />
						</h4>
					</div>
					<input type="hidden" value="${media.mediaType }" name="mediaType" class="mediaType">
				</div>
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2"><span
						class="color-red">*</span>LOGO:</label>
						<c:if test="${ showPicFile eq true }">
			                	<c:set var="hasError1" value="has-error" />
			                </c:if>
					<div class="col-xs-3 has-feedback ${hasError1 }">
						<input type="file" class="form-control" name="showPicFile" value="${media.showPic }" />
						<tags:fieldError commandName="weibo" field="showPicFile"/>
					</div>
					<div class="col-xs-1"><img height="30" alt="" src="<zy:fileServerUrl value="${media.showPic }" />"></div>

					<label class="control-label col-xs-1">二维码:</label>
					<c:if test="${ qrCodeFile eq true }">
						<c:set var="hasError2" value="has-error" />
					</c:if>
					<c:if test="${media.qrCode eq null }">
						<div class="col-xs-5 has-feedback ${hasError2 }">
							<input type="file" class="form-control" name="qrCodeFile">
							<tags:fieldError commandName="weibo" field="qrCodeFile" />
						</div>
					</c:if>
					<c:if test="${media.qrCode ne null }">
						<div class="col-xs-4 has-feedback ${hasError2 }">
							<input type="file" class="form-control" name="qrCodeFile">
							<tags:fieldError commandName="weibo" field="qrCodeFile" />
						</div>
						<div class="col-xs-1">
							<img height="30" alt=""
								src="<zy:fileServerUrl value="${media.qrCode }" />">
						</div>
					</c:if>
				</div>
				<div class="form-group form-group-sm">
					<div class="weibo">
						<label class="control-label col-xs-2"><span
							class="color-red">*</span>微博昵称:</label>
						<div class="col-xs-4 has-feedback">
							<input id="checkMediaName" type="text" class="form-control" name="name"
								value="${media.name}">
							<input type="hidden" value="${media.name}" class="oldName">
						</div>
					</div>
					<label class="control-label col-xs-1"><span
						class="color-red">*</span>认证类别:</label>
					<div class="weibo">
						<div class="col-xs-5 has-feedback">
							<c:forEach var="weiboCategory" items="${weiboCategory.dicItems}">
								<label class="radio-inline"> <input
									<c:if test="${media.category eq weiboCategory.itemCode }">checked="checked"</c:if>
									type="radio" name="category" value="${weiboCategory.itemCode}"
									id="${weiboCategory.itemCode}">${weiboCategory.itemName}</label>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2"><span
						class="color-red">*</span>行业类型:</label>
					<div class="col-xs-4 has-feedback">
						<select class="hide select-modal" name="industryType"
							id="industryTypes" multiple="multiple" data-title="选择行业类型">
							<option value="ALL" <c:if test="${media.industryType eq 'ALL' }">selected="selected"</c:if>>全部</option>
							<zy:options items="${industryTypes }" itemLabel="itemName"
								itemValue="itemCode" selecteds="${media.industryType }"/>
						</select>
					</div>
					<label class="control-label col-xs-1"><span
						class="color-red">*</span>地区:</label>
					<div class="col-xs-5 has-feedback">
						<select name="region" id="regions" class="regions" multiple>
						<option value="ALL" <c:if test="${media.region eq 'ALL' }">selected="selected"</c:if>>全国</option>
						<zy:options items="${regions }" itemLabel="name" itemValue="id"
								selecteds="${media.region }" />
						</select>
					</div>
				</div>
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2">粉丝方向:</label>
					<div class="col-xs-4">
						<div class="weibo">
							<select class="hide select-modal" name="fansDir"
								id="" multiple="multiple" data-title="请选择粉丝方向">
								<zy:options items="${weiboFans }" itemLabel="itemName"
									itemValue="itemCode" selecteds="${media.fansDir }"/>
							</select>
						</div>
					</div>

					<label class="control-label col-xs-1"><span
						class="color-red">*</span>粉丝数:</label>
					<div class="col-xs-5 has-feedback">
						<input type="text" class="form-control" name="fans" id="fans"
							value="${media.fans }">
					</div>
				</div>
				
				<div class="form-group form-group-sm">
					<div class="weixin">
						<label class="control-label col-xs-2"><span
							class="color-red">*</span>适用产品:</label>
						<div class="col-xs-4 has-feedback">
							<select class="hide select-modal" name="products"
								id="" multiple="multiple" data-title="选择适用产品" >
								<zy:options items="${weixinFitProduct }" itemLabel="itemName"
									itemValue="itemCode" selecteds="${media.products}"/>
							</select>
						</div>
					</div>
				</div>
				
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2"><span
						class="color-red">*</span>标签:</label>
					<div class="col-xs-10">
						<p id="mediaLabels" class="has-feedback">
							<input type="hidden" value="${media.tags}" id="tagsValue">
							<c:forEach items="${tags }" var="tag">
							<c:if test="${tag ne null }">
								<label class="checkbox-inline"><input type="checkbox"
									name="tags" value="${tag.id}" data-name="${tag.name }">${tag.name}
								</label>
							</c:if>
							</c:forEach>
						</p>
						<div class="input-group col-xs-3">
							<input type="text" class="form-control" placeholder="自定义标签"
								id="tag" maxlength="14"> <span class="input-group-btn"><button
									type="button" class="btn btn-sm btn-default"
									data-loading-text="处理中..." onclick="addLabel(this)">添加</button></span>
						</div>
					</div>
				</div>
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2"><span
						class="color-red">*</span>详情:</label>
					<div class="col-xs-10 has-feedback">
						<textarea class="form-control" rows="3" name="description">${media.description }</textarea>
					</div>
				</div>
				<hr class="hr-md">
				<p class="text-center">
					<button type="submit" class="btn-u btn-u-red w200" id="editMedia">确认修改</button>
				</p>
			</form>
		</div>
		<!-- End Pad -->
	</div>
	<!-- End Main -->
	<!--=== End Content ===-->
	<!-- JS Global Compulsory -->
	<%@include file="/WEB-INF/views/member/media/weibo/edit-media-script.jspf"%>
	<%@include
		file="/WEB-INF/views/member/media/create-common-script.jspf"%>
</body>
</html>

