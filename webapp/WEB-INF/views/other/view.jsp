<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jspf/common.jsp" %>

<!doctype html>
<html>
<head>
    <title>采媒在线</title>
</head>

<body>

<div class="breadcrumbs">
	<div class="container">
    	<div class="row">
        <ul class="breadcrumb">
            <li>当前位置：</li>
            <li><a href="${ctx}/">首页</a></li>
            <li>更多媒体</li>
        </ul>
        </div>
    </div>
</div>

<!--=== Container ===-->
<div class="container">
	<div class="row">
	    <div class="left-panel">
	        <div class="sidebar">
	            <div class="panel panel-default">
	                <div class="panel-heading sidebar-menu-title">联系方式</div>
	                <div class="panel-body">
	                    <ul class="list-group list-unstyled">
	                        <li>联系专员：林先生</li>
	                        <li>联系电话：15313671759</li>
	                        <li>服务邮箱：vipservice@cnmei.com</li>
	                        <li>公司地址 : 北京市海淀区四道口路大钟寺8号院8号楼</li>
	                    </ul>
	                </div>
	            </div>
	            <div class="panel panel-default">
	                <div class="panel-heading sidebar-menu-title">提交订单</div>
	                <div class="panel-body">
	                <form id="create-form" class="form-horizontal" action="${ctx}/other/createIntention" method="post">
	                	<zy:token/>
	                	<input type="hidden" name="mediaId" value="${media.id }">
	                	<input type="hidden" name="targetTab" value="${media.mediaTabs[0].title }">
	                    <div class="form-group">
	                        <label class="col-xs-4 control-label">联系人:</label>
	                        <div class="col-xs-8 has-feedback">
	                        <input type="text" class="form-control" name="custName" maxlength="4">
	                        </div>
	                    </div>
	                    <div class="form-group">
	                        <label class="col-xs-4 control-label">电话:</label>
	                        <div class="col-xs-8 has-feedback">
	                        <input type="text" class="form-control" name="custPhone" maxlength="11">
	                        </div>
	                    </div>
	                    <div class="form-group">
	                        <div class="col-xs-12 text-right">
	                          <button type="submit" class="btn-u btn-u-dark" id="intention-submit" >提交</button>
	                        </div>
	                    </div>
	                </form>
	                </div>
	            
	            </div>
	        </div>     
	    </div>
	    <div class="right-panel">
	    
	        <div class="tab-v1">
	            <ul class="nav nav-tabs">
	                <c:forEach items="${media.mediaTabs }" var="item" varStatus="status">
	                <li class="<c:if test="${status.first }">active</c:if> mediatab-select" data-value="${item.title }"><a href="#${item.id}" data-toggle="tab">${item.title }</a></li>
	                </c:forEach>
	            </ul>
	            <div class="tab-content">
	                <div class="topwrap">
	                    <h4>${media.name }广告刊例</h4>
	                    <c:choose>
		                    <c:when test="${not empty media.attachment }">
		                    <div class="text-right">
					            <shiro:hasRole name="advertiser">
					            	<a class="btn-u btn-u-sm btn-u-dark"  id="btn-favorites" data-id="${media.id}" >取消收藏</a>
					            </shiro:hasRole>
		                      <shiro:authenticated>
		                       <a href="${ctx }/member/req/download/article/${media.attachment}" class="btn-u btn-u-sm btn-u-dark">下载刊例</a>
	                            </shiro:authenticated>
		                       <shiro:guest>
		                       <a href="javascript:void(0)" onClick="$('#loginBtn').click()" class="btn-u btn-u-sm btn-u-dark">下载刊例</a>
	                            </shiro:guest>
		                    </div>
		                    </c:when>
		                    <c:otherwise>
		                    	<div class="text-right">
		                    		<shiro:hasRole name="advertiser">
					            	<a class="btn-u btn-u-sm btn-u-dark"  id="btn-favorites" data-id="${media.id}" >取消收藏</a>
					            	</shiro:hasRole>
		                    	</div>
		                    </c:otherwise>
	                    </c:choose>
	                </div>
                    <c:forEach items="${media.mediaTabs }" var="item" varStatus="status">
	                <div class="tab-pane <c:if test="${status.first }">active</c:if>" id="${item.id }">
	                   <div class="plugin-wrap">
                    ${item.content }
	                   </div>
	                </div>
                    </c:forEach>
	            </div>
	        </div>
	    
	    </div>
	</div>
</div>
<!--=== End Container ===-->
<script type="text/javascript">
$(function(){
	
	function isFavorites(){
		var url = '${ctx}/member/favoritesMedia/isFavorites?ajax';
		var fid = '${media.id}';
		var	type = 'FAVORITES_OTHERMEDIA';
		$.post(url, {mediaId:fid,type:type}, function(rsp) {
			rsp = $.parseJSON(rsp);
			if (rsp.favorites){
				$('#btn-favorites').text("取消收藏");
				$('#btn-favorites').data().url = '${ctx}/member/favoritesMedia/delete';
			} else {
				$('#btn-favorites').text("收藏");
				$('#btn-favorites').data().url = '${ctx}/member/favoritesMedia/add';
			}
		});
	}
	isFavorites();
	$('#btn-favorites').click(function() {
		var url = $('#btn-favorites').data().url;
		var fid = '${media.id}';
		var	type = 'FAVORITES_OTHERMEDIA';
		var favorites;
		var text = $(this).text();
		var msg;
		if (text == '收藏'){
			favorites = false;
		} else {
			favorites = true;
		}
		if (favorites == true) {
			bootbox.confirm("您确定要取消收藏？",function(result){
				if(result){
					$.post(url, {mediaId:fid,type:type}, function(rsp) {
						rsp = $.parseJSON(rsp);
						if (rsp.result){
							$('#btn-favorites').text("收藏");
							$('#btn-favorites').data().url = '${ctx}/member/favoritesMedia/add';
						} else {
							
						}
					});
				}
			});
		} else {
			$.post(url, {mediaId:fid,type:type}, function(rsp) {
				rsp = $.parseJSON(rsp);
				if (rsp.result){
					$('#btn-favorites').text("取消收藏");
					$('#btn-favorites').data().url = '${ctx}/member/favoritesMedia/delete';
					common.showMessage("收藏成功");
				} else {
					
				}
			});
		}
	});
	$(".nav-tabs").delegate("li.mediatab-select", "click", function(){
		var value = $(this).data("value");
		$('#create-form').find('[name="targetTab"]').val(value);
	});
	
	$("#create-form").validate({
		debug: false,
		submitHandler: function(form) {
			common.disabled('#intention-submit');
			form.submit();
		}, 
		
		rules: {
			custName:{
				required: true, 
				rangelength : [2,4]
			},
			custPhone:{
				required: true, 
				digits:true,
				minlength:11
			},
		},
		messages: {
			custName:{
				required: "请您输入联系人", 
				rangelength: "请输入正确的联系人", 
			},
			custPhone:{
				required: "请您输入联系电话", 
				minlength : '请输入11位数字的手机号码'
			},
		}
	});
	
});
</script>


</body>
</html>

