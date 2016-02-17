<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jspf/common.jsp"%>
<!doctype html>
<html>
<head>
<title>创建新需求-媒介主</title>
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
	<!--=== Content ===-->
	<!-- Main -->
	<div class="main-panel">
		<div class="headline">
			<h5>创建媒体</h5>
			<a class="btn-u btn-u-sm btn-u-dark pull-right" href="#"
				onclick="location.replace(document.referrer);"><i
				class="fa fa-angle-left"></i>返回</a>
		</div>
		<!-- pad -->
		<div class="pad pd15">
		<%-- 显示后台验证错误的标签 --%>
			<form id="create-media" class="form-horizontal form-create"
				action="${ctx}/member/media/weixin/save" method="post"
				enctype="multipart/form-data">
				<zy:token />
				<input type="hidden" value="${media.id}" name="id">
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2"><span
						class="color-red">*</span>媒体类别:</label>
					<div class="col-xs-4">
						<h4 style='color: green'>
							<zy:dic value="${mediaType }" />
						</h4>
						<input type="hidden" value="${mediaType }" name="mediaType" class="mediaType">
					</div>
				</div>
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2"><span
						class="color-red">*</span>LOGO:</label>
						<c:if test="${ showPicFile eq true }">
			                	<c:set var="hasError1" value="has-error" />
			            </c:if>
					<div class="col-xs-4 has-feedback ${hasError1 }">
						<input type="file" class="form-control" name="showPicFile">
						<tags:fieldError commandName="weixin" field="showPicFile"/>
					</div>

					<label class="control-label col-xs-1">二维码:</label>
						<c:if test="${ qrCodeFile eq true }">
			                	<c:set var="hasError2" value="has-error" />
			            </c:if>
					<div class="col-xs-5 has-feedback ${hasError2 }">
						<input type="file" class="form-control" name="qrCodeFile">
						<tags:fieldError commandName="weixin" field="qrCodeFile"/>
					</div>
				</div>
				<div class="form-group form-group-sm ">
					<div class="weixin ">
						<label class="control-label col-xs-2"><span
							class="color-red">*</span>微信昵称:</label>
						<div class="col-xs-4 has-feedback">
							<input id="checkMediaName" type="text" class="form-control" name="name"
								value="${param.name}">
						</div>
					</div>
					<label class="control-label col-xs-1"><span
						class="color-red">*</span>微信号:</label>
					<div class="col-xs-5 has-feedback">
						<input id="checkMediaAccount" type="text" class="form-control" name="account" value="${param.account}">
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
									itemValue="itemCode" />
							</select>
						</div>
					</div>
					<label class="control-label col-xs-1"><span
						class="color-red">*</span>认证类别:</label>
					<div class="weixin">
						<div class="col-xs-5 has-feedback">
							<c:forEach var="weixinCategory"
								items="${weixinCategory.dicItems}">
								<label class="radio-inline"> <input type="radio" 
									name="category" value="${weixinCategory.itemCode}"
									id="${weixinCategory.itemCode}">${weixinCategory.itemName}</label>
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
							<option value="ALL">全部</option>
							<zy:options items="${industryTypes }" itemLabel="itemName"
								itemValue="itemCode" />
						</select>
					</div>
					<label class="control-label col-xs-1"><span
						class="color-red">*</span>地区:</label>
					<div class="col-xs-5 has-feedback">
						<select name="region" id="regions" class="regions">
						</select>
					</div>
				</div>

				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2">粉丝方向:</label>
					<div class="weixin">
						<div class="col-xs-4 has-feedback">
							<select class="hide select-modal" name="fansDir"
								id="" multiple="multiple" data-title="请选择粉丝方向">
								<zy:options items="${weixinFans }" itemLabel="itemName"
									itemValue="itemCode" />
							</select>
						</div>
					</div>
					<label class="control-label col-xs-1"><span
						class="color-red">*</span>粉丝数:</label>
					<div class="col-xs-5 has-feedback">
						<input type="text" class="form-control" name="fans" id="fans"
							value="${param.fans }">
					</div>
				</div>
				
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2"><span
						class="color-red">*</span>粉丝数截图:</label>
						<c:if test="${ fansNumFile eq true }">
			                	<c:set var="hasError3" value="has-error" />
			            </c:if>
					<div class="weixin">
						<div class="col-xs-4 has-feedback ${hasError3 }">
							<input type="file" class="form-control" name="fansNumFile">
							<tags:fieldError commandName="weixin" field="fansNumFile"/>
						</div>
					</div>
				</div>
				
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2"><span
						class="color-red">*</span>标签:</label>
					<div class="col-xs-10">
						<p id="mediaLabels" class="has-feedback">
							<c:forEach items="${tagList }" var="tag">
								<label class="checkbox-inline"><input type="checkbox"
									name="tags" value="${tag.id}" data-name="${tag.name }">${tag.name} </label>
							</c:forEach>
						</p>
						<div class="input-group col-xs-3">
							<input type="text" class="form-control" placeholder="自定义标签"
								id="tag" maxlength="14"> <span class="input-group-btn"><button
									type="button" class="btn btn-sm btn-default" data-loading-text="处理中..."
									onclick="addLabel(this)">添加</button></span>
						</div>
					</div>
				</div>
				<div class="form-group form-group-sm">
					<label class="control-label col-xs-2"><span
						class="color-red">*</span>详情:</label>
					<div class="col-xs-10 has-feedback">
						<textarea class="form-control" rows="3" name="description">${param.description}</textarea>
					</div>
				</div>
				<hr class="hr-md">
				<p class="text-center">
					<button type="submit" class="btn-u btn-u-red w200"
						id="btn-createMedia">发布</button>
				</p>
			</form>
		</div>
		<!-- End Pad -->
	</div>
	<!-- End Main -->	
	<%@include file="/WEB-INF/views/member/media/create-media-script.jspf"%>
	<%@include
		file="/WEB-INF/views/member/media/create-common-script.jspf"%>
</body>
</html>

