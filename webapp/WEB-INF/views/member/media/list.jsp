<%@page import="org.apache.shiro.SecurityUtils"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>
<%@ include file="/WEB-INF/jspf/taglib.jsp" %>

<!doctype html>
<html>
<head>
    <title>会员中心</title>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('#leftBar > #my-media').addClass('list-group-item-info');
	});

	/**
	修改媒体
	 */
	function updateMedia(mediaId) {
		window.location.href = '${ctx}/member/media/edit?mediaId=' + mediaId;
	}

	/**
	 删除媒体
	 */
	
	function deleteMedia(mediaId, mediaType) {
		bootbox.confirm("您确定要删除该媒体吗？", function(result) {
			if (result)
				window.location.href = "${ctx}/member/media/del?mediaId="
						+ mediaId + "&mediaType=" + mediaType;
		});
	}
	
	
</script>
</head>

<body>
<!-- Main -->
        <div class="main-panel">
    		<div class="headline"><h5>我的媒体列表</h5></div>
            <div class="pad">
            		<div class="topwrap">
				<form class="form-inline">
					<div class="form-group form-group-sm">
						<input class="form-control input-sm"
							id="search_LIKE_name" name="search_LIKE_name"
							value="${param.search_LIKE_name }" placeholder="在媒体列表中搜索"
							type="text">
					</div>
					<button class="btn-u btn-u-sm btn-u-dark">
						<i class="fa fa-search"></i> 查询
					</button>
					<a class="btn-u btn-u-sm btn-u-dark pull-right" href="${ctx }/member/media/createStep1"><i class="fa fa-plus"></i>创建新媒体</a>
				</form>
			</div>
                <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>媒体名称</th>
                        <th>大类</th>
                        <th>小类</th>
                        <th>创建时间</th>
                        <th>行业类型</th>
                        <th>服务及报价</th>
                        <th>待处理需求</th>
                        <th>操作</th>
                    </tr>
                </thead>
            
                <tbody>
                <c:forEach var="media" items="${data.content }">
                <tr>
                    <td><a data-id="${media.id }"  class="preview-media" href="#">${media.name }</a></td>
                    <td>
                    	<zy:dic value="${media.mediaType}"/>
                    </td>
                    <td>
                    <zy:dic value="${media.category}"/>
                    </td>
                    <td><fmt:formatDate value="${media.createTime }" pattern="yyyy/MM/dd HH:mm" /></td>
                    <td style="max-width:500px;">
                    	<c:forEach
									items="${fn:split(media.industryType,',')}" var="industry">
									<zy:dic value="${industry}" />
								</c:forEach>
							</td>
                    <td><c:if
									test="${fn:length(media.mediaQuotes) gt 0}">
									<a href="${ctx }/member/media/quote?mediaId=${media.id}"> ${fn:length(media.mediaQuotes)} 种</a>
								</c:if> <c:if test="${fn:length(media.mediaQuotes) lt 1}">暂无</c:if>
							</td>
                    <td>
                    <c:if test="${media.pendingNum gt 0}">
									<a href="${ctx }/member/req/deal">${media.pendingNum }
										个</a>
								</c:if> <c:if test="${media.pendingNum lt 1}">
                    		暂无
                    </c:if></td>
							<td class="manage minWidthFive">
							<a	href="${ctx }/member/media/case?mediaId=${media.id}">案例</a>  |  
							<a	href="${ctx }/member/media/quote?mediaId=${media.id}">报价</a>  | 
							<c:if test="${media.mediaType == 'MEDIA_T_WEIXIN'}">
							<a	href="${ctx }/member/media/edit?mediaId=${media.id}">修改</a>  | 
							</c:if>
							<c:if test="${media.mediaType == 'MEDIA_T_WEIBO'}">
							<a	href="${ctx }/member/media/edit?mediaId=${media.id}">修改</a>  | 
							</c:if>
								     <a 
								href="javascript:void(0)" onclick="deleteMedia('${media.id}','${media.mediaType}')">删除</a> | 
								<a data-id="${media.id }" class="preview-media" href="#">详情</a>
								</td>
						</tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
		<tags:pagination page="${data }" />
	</div>
        <!-- End Main -->
        
     <input type="hidden" id="isDel" value="${notDel}">   
    <div class="modal fade" id="media-view" tabindex="-1" role="dialog" data-width="900" data-replace="true">
    </div>
    
    <script type="text/javascript">
    common.showMedia(".preview-media");
    $(function(){
					var isDel = $('#isDel').val();
						if (isDel) {
							bootbox.alert("该媒体已绑定预约单或订单，无法删除！");
						} 
					});
				</script>
</body>
</html>
